package com.example.ngl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;


class AnagramGeneratorIterativeTest {
    @Test
    void testGenerateAnagrams_ABC() {
        List<String> result = AnagramGeneratorIterative.generateAnagrams("abc");
        List<String> expected = List.of("abc", "acb", "bac", "bca", "cab", "cba");
        assertEquals(expected, result); // ¡Sigue en orden lexicográfico!
    }

    //
    @Test
    void test01() {
    }
}