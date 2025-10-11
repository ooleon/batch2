package com.example.septillones;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Iterator que genera permutaciones de forma iterativa (sin recursión ni almacenamiento completo).
 * Usa el algoritmo "next permutation" para avanzar una por una.
 * Ideal para ItemReader en Spring Batch.
 */
public class AnagramIterator implements Iterator<String> {

    private final String prefix;
    private List<Character> currentPermutation;
    private boolean first = true;

    public AnagramIterator(String prefix, String remaining) {
        this.prefix = prefix;
        this.currentPermutation = remaining.chars()
                .mapToObj(c -> (char) c)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean hasNext() {
        return first || nextPermutation(currentPermutation);
    }

    @Override
    public String next() {
        if (first) {
            first = false;
        }
        return prefix + currentPermutation.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    // Algoritmo "next permutation" iterativo (mismo que antes)
    private boolean nextPermutation(List<Character> arr) {
        int n = arr.size();
        int i = n - 2;
        while (i >= 0 && arr.get(i) >= arr.get(i + 1)) i--;
        if (i < 0) return false;
        int j = n - 1;
        while (arr.get(j) <= arr.get(i)) j--;
        Collections.swap(arr, i, j);
        reverseSuffix(arr, i + 1);
        return true;
    }

    private void reverseSuffix(List<Character> arr, int start) {
        int end = arr.size() - 1;
        while (start < end) {
            Collections.swap(arr, start, end);
            start++;
            end--;
        }
    }
}