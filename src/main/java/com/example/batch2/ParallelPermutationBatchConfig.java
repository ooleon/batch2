package com.example.batch2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.*;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.core.step.*;
import org.springframework.batch.core.step.builder.*;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.launch.support.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.util.*;

@Configuration
@EnableBatchProcessing
public class ParallelPermutationBatchConfig {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    public PermutationItemReader reader;
    @Autowired
    public ParallelPermutationProcessor processor;
    @Autowired
    public PermutationItemWriter writer;

//    @Autowired
    private JobBuilder jobBuilder;
    @Autowired
    private JobRepository jobRepository;
//    @Autowired
    private PlatformTransactionManager transactionManager;

/*

    @Autowired
    private JobFactory jobFactory;

    @Autowired
    private JobStepBuilder jobStepBuilder;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
*/


    @Bean
    public Job parallelPermutationJob() {
//        return jobBuilderFactory.get("parallelPermutationJob")
//        System.out.println("\nparallelPermutationJob\n");
        logger.info("\nparallelPermutationJob\n");

        jobRepository = new ResourcelessJobRepository();
        transactionManager = new AbstractPlatformTransactionManager() {
            @Override
            protected Object doGetTransaction() throws TransactionException {
                return null;
            }

            @Override
            protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {

            }

            @Override
            protected void doCommit(DefaultTransactionStatus status) throws TransactionException {

            }

            @Override
            protected void doRollback(DefaultTransactionStatus status) throws TransactionException {

            }
        };
        return new JobBuilder("parallelPermutationJob",jobRepository)
//                .incrementer(new RunIdIncrementer())
                .start(permutationStep())
                .build();
    }

    @Bean
    public Step permutationStep() {


//        System.out.println("\nStepBuilder\n");
        logger.info("\nStepBuilder\n");
        return new StepBuilder("permutationStep", jobRepository)
                .<String, List<String>>chunk(1000,transactionManager)

                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .startLimit(1)
                .build();
    }

    /*

    @Bean
    public Job parallelPermutationJob() {
//        return jobBuilderFactory.get("parallelPermutationJob")
        return jobBuilderFactory.get("parallelPermutationJob")
                .incrementer(new RunIdIncrementer())
                .start(permutationStep())
                .build();
    }

    @Bean
    public Step permutationStep() {

        return stepBuilderFactory.get("permutationStep")
                .<String, List<String>>chunk(1000)
//                .reader(permutationItemReader())
//                .processor(parallelPermutationProcessor())
//                .writer(permutationItemWriter())
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .throttleLimit(8)
                .build();
    }
*/
    @Bean
    public TaskExecutor taskExecutor() {
//        System.out.println("\ntaskExecutor\n");
        logger.info("\ntaskExecutor\n");
        logger.info(reader);
        /*
        try {
            processor.process("s");
            System.out.println();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        */

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("permutation-worker-");
        executor.initialize();
        return executor;
    }
}