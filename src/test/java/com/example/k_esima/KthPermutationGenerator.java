package com.example.k_esima;


import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generador de la k-ésima permutación lexicográfica SIN generar las anteriores.
 * Usa sistema factoriádico (factorial number system).
 * Ideal para procesamiento paralelo / batch / particionamiento por rango de índices.
 */
public class KthPermutationGenerator {

    /**
     * Retorna la k-ésima permutación (0-indexed) del string dado, en orden lexicográfico.
     * @param input cadena con caracteres distintos (ej: "abc", "abcdefghijklmnopqrstuvwxyz")
     * @param k índice de la permutación deseada (0 <= k < n!)
     * @return la permutación en la posición k
     * @throws IllegalArgumentException si k está fuera de rango o input inválido
     */
    public static String getKthPermutation(String input, BigInteger k) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input must not be null or empty");
        }

        List<Character> chars = input.chars()
                .mapToObj(c -> (char) c)
                .sorted() // Orden lexicográfico base
                .collect(Collectors.toCollection(ArrayList::new));

        int n = chars.size();

        // Precomputar factoriales como BigInteger
        BigInteger[] factorials = new BigInteger[n];
        factorials[0] = BigInteger.ONE;
        for (int i = 1; i < n; i++) {
            factorials[i] = factorials[i - 1].multiply(BigInteger.valueOf(i));
        }

        // Validar que k esté en rango [0, n! - 1]
        BigInteger totalPerms = factorials[n - 1].multiply(BigInteger.valueOf(n));
        if (k.compareTo(BigInteger.ZERO) < 0 || k.compareTo(totalPerms) >= 0) {
            throw new IllegalArgumentException("k must be in range [0, " + totalPerms + ")");
        }

        StringBuilder result = new StringBuilder();
        List<Character> available = new ArrayList<>(chars);

        // Para cada posición en la permutación
        for (int i = 0; i < n; i++) {
            BigInteger factorialBase = factorials[n - 1 - i]; // (n-1-i)!
            BigInteger indexBI = k.divide(factorialBase);
            int index = indexBI.intValueExact(); // índice del carácter a elegir

            result.append(available.remove(index)); // toma y elimina ese carácter
            k = k.mod(factorialBase); // actualiza k para el siguiente nivel
        }

        return result.toString();
    }
}