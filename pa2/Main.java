import java.util.List;
import java.util.stream.Stream;

class Main {
    private static void main(String[] args) {
        Stream.iterate(10, x -> x * 10).limit(6).map(x -> simulate(2030, x)).forEach(x -> System.out.println(x));
    }
    static double simulate(int seed, int o){
        Circle c = new Circle(new Point(0.0, 0.0), 1.0);
        long n = Stream.iterate(Rand.of(seed), x -> x.next().next())
            .limit(o)
            .map(x -> x.flatMap(y -> Rand.of(y).map(z -> List.of(g(y),g(z))).next()).get())
            .filter(x -> c.contains(new Point(x.get(0), x.get(1))))
            .count();
        return 4.0 * n / o;
    }

    private static double g(int v) {
        double l = -1.0;
        double h = 1.0;

        return (h - l) * v / (Integer.MAX_VALUE - 1) + l;
    }
}
