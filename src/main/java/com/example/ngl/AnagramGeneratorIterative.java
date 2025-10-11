package com.example.ngl;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Generador de anagramas usando algoritmo iterativo "Next Permutation".
 * Produce resultados en orden lexicográfico SIN recursión.
 * Totalmente compatible con Java 21, legible, validado y paralelizable.
 */
public class AnagramGeneratorIterative {

    private static final Pattern LETTERS_ONLY = Pattern.compile("^[a-zA-Z]+$");

    /**
     * Genera todas las permutaciones (anagramas) en orden lexicográfico.
     * Usa el algoritmo iterativo "next permutation", NO recursión.
     *
     * @param input cadena de letras distintas
     * @return lista de anagramas en orden lexicográfico
     * @throws IllegalArgumentException si la entrada es inválida
     */
    public static List<String> generateAnagrams(String input) {
        validateInput(input);


        // Convertir a lista de caracteres y ordenar (base lexicográfica)
        List<Character> chars = input.chars()
                .mapToObj(c -> (char) c)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));

        List<String> results = new ArrayList<>();
        do {
            results.add(chars.stream().map(String::valueOf).collect(Collectors.joining()));
        } while (nextPermutation(chars));

        return results;
    }

    /**
     * Genera la siguiente permutación lexicográfica en su lugar (in-place).
     * Retorna true si existe una siguiente permutación, false si ya es la última.
     */
    private static boolean nextPermutation(List<Character> arr) {
        int n = arr.size();
        int i = n - 2;

        // Paso 1: Encuentra el mayor i tal que arr[i] < arr[i+1]
        while (i >= 0 && arr.get(i) >= arr.get(i + 1)) {
            i--;
        }

        if (i < 0) return false; // Ya es la última permutación

        // Paso 2: Encuentra el mayor j > i tal que arr[j] > arr[i]
        int j = n - 1;
        while (arr.get(j) <= arr.get(i)) {
            j--;
        }

        // Paso 3: Intercambiar
        Collections.swap(arr, i, j);

        // Paso 4: Invertir sufijo desde i+1
        reverseSuffix(arr, i + 1);

        return true;
    }

    private static void reverseSuffix(List<Character> arr, int start) {
        int end = arr.size() - 1;
        while (start < end) {
            Collections.swap(arr, start, end);
            start++;
            end--;
        }
    }

    private static void validateInput(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input must not be null or empty");
        }
        if (!LETTERS_ONLY.matcher(input).matches()) {
            throw new IllegalArgumentException("Input must contain only letters (a-z, A-Z)");
        }
        if (input.chars().distinct().count() != input.length()) {
            throw new IllegalArgumentException("Input must contain distinct letters");
        }
    }
}