package partOne;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String[] words = {"road", "newspaper", "pen", "cat", "dog", "door", "ocean",
                           "cat", "journey", "bag", "snow", "big", "cat", "spider", "dog", "man"};
        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        for (String word : words) {
            if (!wordCount.containsKey(word)) {
                wordCount.put(word, 1);
            } else {
                wordCount.put(word, wordCount.get(word)+1);
            }
        }

        wordCount.forEach((k, v) -> System.out.println("Word - " +k+ ", count - " +v));
    }
}
