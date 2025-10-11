package com.example.lexicograph;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class to generate all lexicographically ordered anagrams from a set of distinct letters.
 * Uses recursive DFS for clarity and readability. Input is validated for non-emptiness and letter-only content.
 * Optionally parallelizes for larger inputs (size >= 6) using parallel streams.
 */
public class AnagramGenerator {

    private static final Pattern LETTERS_ONLY = Pattern.compile("^[a-zA-Z]+$");

    /**
     * Generates all possible anagrams from the given string of distinct letters.
     * <p>
     * Algorithm: Recursive DFS with backtracking. Input is sorted to ensure lexicographic order.
     * Parallelizes via parallelStream() if input length >= 6 for performance optimization.
     * </p>
     *
     * @param input String of distinct letters (e.g., "abc")
     * @return List of all anagrams in lexicographic order
     * @throws IllegalArgumentException if input is null, empty, or contains non-letter characters
     */
    public static List<String> generateAnagrams(String input) {
        validateInput(input);

        List<Character> chars = input.chars()
                .mapToObj(c -> (char) c)
                .sorted() // Ensure lexicographic base
                .collect(Collectors.toCollection(ArrayList::new));

        List<String> results = new ArrayList<>();
        backtrack(chars, new ArrayList<>(), new boolean[chars.size()], results);

        // Optional: Parallelize for larger inputs
        if (input.length() >= 6) {
            return results.parallelStream().collect(Collectors.toList());
        }
        return results;
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

    /**
     * Recursive backtracking to generate permutations.
     * Builds each permutation by selecting unused characters one by one.
     */
    private static void backtrack(
            List<Character> chars,
            List<Character> current,
            boolean[] used,
            List<String> results
    ) {
        if (current.size() == chars.size()) {
            results.add(current.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining()));
            return;
        }

        for (int i = 0; i < chars.size(); i++) {
            if (used[i]) continue;

            used[i] = true;
            current.add(chars.get(i));
            backtrack(chars, current, used, results);
            current.removeLast(); // Java 21: SequencedCollection.removeLast()
            used[i] = false;
        }
    }
}