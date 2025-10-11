package com.example.batch2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class ParallelPermutationProcessor implements ItemProcessor<String, List<String>> {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Value("${parallelProcessor.elements:1,2,3,4,5,6}")
    String string;

    @Autowired
    private PermutationItemReader reader;


    @Override
    public List<String> process(String item) throws Exception {
//        List<String> elements = reader.getInputElements();
//        List<String> elements = Arrays.asList(string.split(","));
        List<String> elements = Arrays.asList(item.split(","));
        int totalPermutations = factorial(elements.size());
        List<String> result = new ArrayList<String>();
        result = IntStream.range(0, totalPermutations)
                .parallel()
                .mapToObj(index -> generateNthPermutation(elements, index))
                .map(permutation -> String.join(",", permutation))
                .toList();
        return result;
    }

    /*
    @Override
    public List<String> process(String item) throws Exception {
        List<String> ls = Arrays.asList(elements.split(","));

        System.out.println("\nprocess\n");

        return Arrays.stream(elements.split(","))
                .parallel()
                .peek((i) -> {
                    System.out.print(i + " ");
                })
                .toList();

     / *
        return IntStream.range(0, totalPermutations)
            .parallel()
            .
            .map(permutation -> String.join(",", new List<CharSequence>(). {
            }))
            .toList();
    * /
    }

*/


    private List<String> generateNthPermutation(List<String> elements, int index) {
        List<String> mutableElements = new ArrayList<>(elements);
        List<String> result = new ArrayList<>();
        int n = elements.size();
        int currentIndex = index;

        for (int i = n; i > 0; i--) {
            int fact = factorial(i - 1);
            int pos = currentIndex / fact;
            result.add(mutableElements.remove(pos));
            currentIndex %= fact;
        }

        return result;
    }

    private int factorial(int n) {
        if (n <= 1) return 1;
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
