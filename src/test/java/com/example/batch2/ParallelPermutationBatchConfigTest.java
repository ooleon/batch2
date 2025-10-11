package com.example.batch2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParallelPermutationBatchConfigTest {

    @Autowired
    ParallelPermutationBatchConfig parallelPermutation;

    @BeforeEach
    void setUp() {
    }

    @Test
    void parallelPermutationJob() {
    }

    @Test
    void permutationStep() {
    }

    @Test
    void taskExecutor() {
        var stringToApplay = "R,T";
        PermutationItemReader reader = new PermutationItemReader(stringToApplay);
        parallelPermutation.reader=reader;
        parallelPermutation.taskExecutor();

    }

    @Test
    void test01() {
//        List<String> result = IntStream.range(0, 5)
//                .collect(Collectors.toCollection(
//                ));

//        List<String> result = IntStream.range(0, factorial(n))
//                .parallel()
//                .mapToObj(i -> generatePermutation(i, chars))
//                .collect(Collectors.toCollection(() -> new ArrayList<>(factorial(n))));

/*
        ScopedValue<String> sw = ScopedValue.newInstance();
        IO.println(sw.isBound());
        ScopedValue.where(sw, "paso").run(() -> {
            IO.println(sw.isBound());
            IO.println(sw.get());
        });
        IO.println(sw.isBound());
*/
    }



}