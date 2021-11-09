import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Optional;

class Main {

    public static void main(String[] args) {

    }

    static long countTwinPrimes(int n) {
        long numOfTwinPrimes = Stream.<Integer>iterate(1, x -> x < n + 1, x -> x + 1)
                .filter(x -> (isPrime(x) && isPrime(x + 2)) || isPrime(x) && isPrime(x - 2)).count();
        return numOfTwinPrimes;
    }

    static boolean isPrime(int n) {
        return n > 1 && IntStream.range(2, (int) Math.sqrt(n) + 1) // or (2,n)
                .noneMatch(x -> n % x == 0);
    }

    static String reverse(String str) {
        List<Character> result = new ArrayList<Character>();
        char[] letterArray = str.toCharArray();
        IntStream.range(0, letterArray.length).mapToObj(i -> letterArray[(letterArray.length - 1) - i])
                .forEach(letter -> result.add(letter));

        return result.stream().map(String::valueOf).collect(Collectors.joining());
    }

    static long countRepeats(int... array) {
        return IntStream.range(0, array.length - 1)
                .filter(x -> array[x] == array[x + 1] && (x >= array.length - 2 || array[x] != array[x + 2])).count();
    }

    static double normalizedMean(Stream<Integer> stream) {
        List<Integer> list = stream.collect(Collectors.toList());

        double  min = list.stream().mapToDouble(i -> i).min().orElse(0.0);
        double  max = list.stream().mapToDouble(i -> i).max().orElse(0.0);
        double total = list.stream().mapToDouble(i -> i).reduce(0, (x, y) -> x + y);
        double avg = total / list.size();
        double res = (avg - min) / (max - min);
        return Stream.of(res).filter(i -> !Double.isNaN(i)).reduce((x,y) -> x+y).orElse(0.0);
    }   


}