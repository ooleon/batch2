package com.example.batch2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;


import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;
import java.lang.foreign.*;
import java.lang.constant.*;
import java.lang.management.*;
import java.lang.runtime.*;
import java.lang.classfile.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ParallelPermutationProcessorTest {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    ParallelPermutationProcessor parallelPermutationProcessor;

    @Test
    void process05Chart() throws Exception {
        var sw = new StopWatch();
        var stringToApplay = "a,b,c,d,e";
        String title = String.format("process %d Charts to permutation, ", stringToApplay.split(",").length);

        sw.start(title + stringToApplay);
//        IO.println(stringToApplay);
        logger.info(stringToApplay);

//        PermutationItemReader reader = new PermutationItemReader(stringToApplay);

        List<String> result = parallelPermutationProcessor.process(stringToApplay);


        sw.stop();
        logger.info("result: " + result.size());
        logger.info(result);
        HashSet<String> hs = new HashSet<>();
        result.forEach((s) -> hs.add(s));
        logger.info("HashSet: " + hs.size());
        logger.info(hs.toString());

        logger.info(sw.prettyPrint());


    }


    @Test
    void process10Chart() throws Exception {
        var sw = new StopWatch();

        var stringToApplay = "a,b,c,d,e,f,g,h,i,j";
        String title = String.format("process %d Charts to permutation, ", stringToApplay.split(",").length);
        logger.info("\tfreeMemory  \t " + Runtime.getRuntime().freeMemory());
        logger.info("\ttotalMemory  \t " + Runtime.getRuntime().totalMemory());
        logger.info("\tmaxMemory  \t " + Runtime.getRuntime().maxMemory());
        sw.start(title + stringToApplay);
        PermutationItemReader reader = new PermutationItemReader(stringToApplay);

        List<String> result = parallelPermutationProcessor.process(stringToApplay);

        sw.stop();
        logger.info(sw.prettyPrint());
        logger.info("result: " + result.size());

    }

    /**
     * No use 11 or more without configure the parameters of heap memory space, it casue an exception.
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     *
     */
    //    @Test
    void process11Chart() throws Exception {
        var sw = new StopWatch();
        sw.start("process 11 Charts to permutation");

        var stringToApplay = "a,b,c,d,e,f,g,h,i,j,k";
        PermutationItemReader reader = new PermutationItemReader(stringToApplay);

        List<String> result = parallelPermutationProcessor.process(stringToApplay);


        sw.stop();
        logger.info("result: " + result.size());
        logger.info(result);
        logger.info(sw.prettyPrint());
    }

    @Test
    void testUpdate25(){
        PBEKeySpec pbeKeySpec = new PBEKeySpec(new char[]{'1','2'},"salt".getBytes(),65536, 256);
        SecretKeyFactory secretKeyFactory = null;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        SecretKey secretKey = null;
        try {
            secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        logger.info("Pass: " + pbeKeySpec);
        logger.info("Pass: " + secretKey.getEncoded());

    }


}