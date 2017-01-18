package de.faust;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
/*
Background:
        Text file a.txt, put every single word in a line (Use "\n" to separate them),
        then calculate and analyze the top 100 frequency words,
        order them in sequence of high frequency from high to low (pls. refer to the statistical results),
        if same frequency, then order them in sequence of Unicode from small to big.
        e.g. the contents of a.txt are:
        a1
        a2
        a2
        a2
        a2
        a2
        a2
        a3
        The statistical results are：
        a2:6
        a1:1
        a3:1
*/
public class Main {
    private static Path file = Paths.get("a.txt");
    private static Random random = new Random();
    private static String s;

    public static void main(String[] args) {

        //First value is quantoty of lines in file.
        //Second value is maxLengthWord
        //Third and fourth is for range in utf table.
        fileGenerator(1000000, 15, 32, 126); //exclude first 31 symbols

        //Result
        selectFrequency(100);
     }

    private static void selectFrequency(int numberOfSelect){
        try (Stream<String> stream = Files.lines(file)) {
            Map<String, Long> map = stream
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            map.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                            .thenComparing(Map.Entry.comparingByKey()))
                    .limit(numberOfSelect)
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    private static void fileGenerator(int quantityLines, int maxWordLength, int minValue, int maxValue){
        //Charset charset = Charset.forName("US-ASCII");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file)) {

            for (int i = 0; i < quantityLines; i++) {
                int currentWordLength = random.nextInt(maxWordLength)+1;
                s =  stringGenerator(currentWordLength, minValue, maxValue);
                bufferedWriter.write(s, 0, s.length());
                bufferedWriter.newLine();
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    //length = number of symbols in the word
    //min, max = numbercode in UTF table
    private static String stringGenerator(int length, int min, int max){
        IntStream stream = random.ints(min, max);
        return stream.limit(length)
                .collect(StringBuilder::new, (builder, n) -> builder.appendCodePoint(n), (x, y) -> x.append(y))
                .toString();
    }
}