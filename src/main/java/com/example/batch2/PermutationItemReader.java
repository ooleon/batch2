package com.example.batch2;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PermutationItemReader implements ItemReader<String> {

    private final List<String> inputElements;
    private int currentIndex = 0;

    public PermutationItemReader(@Value("${permutation.elements:A,B,C,D}") String elements) {
        this.inputElements = Arrays.asList(elements.split(","));
    }

    @Override
    public String read() {
        if (currentIndex < inputElements.size()) {
            return inputElements.get(currentIndex++);
        }
        return null;
    }

    public List<String> getInputElements() {
        return inputElements;
    }
}