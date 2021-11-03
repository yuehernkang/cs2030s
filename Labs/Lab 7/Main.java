import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

class Main {

    public static void main(String[] args) {

    }

    static long countTwinPrimes(int n) {
        long numOfTwinPrimes = Stream.<Integer>iterate(1, x -> x < n+1, x -> x + 1)
            .filter(x -> (isPrime(x) && isPrime(x+2)) || isPrime(x) && isPrime(x-2) )
            .count();
        return numOfTwinPrimes;
    }

    static boolean isPrime(int n) {
        return n > 1 && IntStream.range(2, (int) Math.sqrt(n) + 1) // or (2,n)
            .noneMatch(x -> n % x == 0); 
    }

    static String reverse(String str) {
        List<Character> result = new ArrayList<Character>();
        char[] letterArray = str.toCharArray();
        IntStream.range(0,letterArray.length)
            .mapToObj(i -> letterArray[(letterArray.length - 1) - i])
            .forEach(letter -> result.add(letter));

        String reversedString = result.stream()
            .map(String::valueOf)
            .collect(Collectors.joining());
        
        return reversedString;
    }




}