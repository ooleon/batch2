package com.example.lexicograph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



class AnagramGeneratorTest {

    @Test
    void testGenerateAnagrams_ABC() {
        List<String> result = AnagramGenerator.generateAnagrams("abc");
        List<String> expected = List.of("abc", "acb", "bac", "bca", "cab", "cba");
        assertEquals(expected, result);
    }

    @Test
    void testSingleLetter() {
        List<String> result = AnagramGenerator.generateAnagrams("a");
        assertEquals(List.of("a"), result);
    }

    @Test
    void testEmptyInput_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnagramGenerator.generateAnagrams("")
        );
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void testNullInput_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnagramGenerator.generateAnagrams(null)
        );
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void testNonLetterInput_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnagramGenerator.generateAnagrams("ab1")
        );
        assertTrue(exception.getMessage().contains("letters"));
    }

    @Test
    void testDuplicateLetters_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnagramGenerator.generateAnagrams("aab")
        );
        assertTrue(exception.getMessage().contains("distinct"));
    }

    @Test
    void testUppercaseAndMixedCase() {
        List<String> result = AnagramGenerator.generateAnagrams("AbC");
        // Lexicographic order: A, b, C → ASCII: 'A'=65, 'C'=67, 'b'=98
        List<String> expected = List.of("ACb", "AbC", "CAb", "CbA", "bAC", "bCA");
        assertEquals(expected, result);
    }
}