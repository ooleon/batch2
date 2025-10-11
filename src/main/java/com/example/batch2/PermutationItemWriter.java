package com.example.batch2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermutationItemWriter implements ItemWriter<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(PermutationItemWriter.class);

    @Override
    public void write(Chunk<? extends List<String>> chunks) throws Exception {
        chunks.forEach(permutations -> {
            logger.info("Generated {} permutations", permutations.size());
            permutations.forEach(permutation ->
                    logger.debug("Permutation: {}", permutation));
        });
    }
}