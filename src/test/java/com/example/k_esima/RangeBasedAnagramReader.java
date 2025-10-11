package com.example.k_esima;

import org.springframework.batch.item.ItemReader;

import java.math.BigInteger;

public class RangeBasedAnagramReader implements ItemReader<String> {

    private final String input;
    private BigInteger currentK;
    private final BigInteger endK;

    public RangeBasedAnagramReader(String input, BigInteger startK, BigInteger endK) {
        this.input = input;
        this.currentK = startK;
        this.endK = endK;
    }

    @Override
    public String read() {
        if (currentK.compareTo(endK) > 0) return null;
        String perm = KthPermutationGenerator.getKthPermutation(input, currentK);
        currentK = currentK.add(BigInteger.ONE);
        return perm;
    }
}
