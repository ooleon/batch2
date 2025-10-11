package com.example.septillones;

import org.springframework.batch.core.*;
import org.springframework.batch.core.job.*;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.step.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Configuration
@EnableBatchProcessing
public class AnagramBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public AnagramBatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job anagramJob() {
        return new JobBuilder("anagramJob", jobRepository)
                .start(masterStep())
                .build();
    }

    @Bean
    public Step masterStep() {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner("workerStep", partitioner())
                .step(workerStep())
                .gridSize(8) // Número de hilos/particiones
                .build();
    }

    @Bean
    @JobScope
    public Partitioner partitioner() {
        return gridSize -> {
            Map<String, ExecutionContext> partitions = new HashMap<>();

            // Input: todas las letras (ej: "abcdefghijklmnopqrstuvwxyz")
            String input = "abcdefghijklmnopqrstuvwxyz"; // o inyectado desde properties

            List<Character> chars = input.chars()
                    .mapToObj(c -> (char) c)
                    .sorted()
                    .collect(Collectors.toCollection(ArrayList::new));

            // Dividir por primera letra → cada worker se encarga de un subconjunto
            for (int i = 0; i < chars.size(); i++) {
                ExecutionContext context = new ExecutionContext();
                context.putString("prefixChar", String.valueOf(chars.get(i)));
                int finalI = i;
                context.putString("remainingChars", chars.stream()
                        .filter(c -> c != chars.get(finalI))
                        .map(String::valueOf)
                        .collect(Collectors.joining()));
                partitions.put("partition-" + i, context);
            }

            return partitions;
        };
    }

    @Bean
    @JobScope
    public Step workerStep() {
        return new StepBuilder("workerStep", jobRepository)
                .<String, String>chunk(1000, transactionManager) // Escribe cada 1000 permutaciones
                .reader(anagramItemReader(null, null))
                .writer(anagramItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<String> anagramItemReader(
            @Value("#{stepExecutionContext['prefixChar']}") String prefixChar,
            @Value("#{stepExecutionContext['remainingChars']}") String remainingChars
    ) {
        // Generador iterativo de permutaciones para este prefijo
        Iterator<String> iterator = new AnagramIterator(prefixChar, remainingChars);
        return new IteratorItemReader<>(iterator);
    }

    @Bean
    @StepScope
    public ItemWriter<String> anagramItemWriter(
            @Value("#{stepExecutionContext['prefixChar']}") String prefixChar
    ) {
        return items -> {
            // Archivo por partición para evitar race conditions
            String filename = "anagrams_" + prefixChar + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                for (String item : items) {
                    writer.write(item);
                    writer.newLine();
                }
            }
        };
    }
}