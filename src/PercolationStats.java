/**
 * Created by mogrein on 03.07.14.
 */

public class PercolationStats {
    private int[] experimentResult;
    private double mean;
    private boolean isMeanInCache = false;
    private double stddev;
    private boolean isStddevInCache = false;

    public PercolationStats(int N, int T) {   // perform T independent computational experiments on an N-by-N grid
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Number of experiments and grid Size must be positive numbers.");
        }
        experimentResult = new int[T];
        isMeanInCache = false;
        isStddevInCache = false;
        Percolation experiment;
        for (int i = 0; i < experimentResult.length; ++i) {
            experiment = new Percolation(N);
            while (!experiment.percolates()) {
                experiment.open(StdRandom.uniform(1, N + 1), StdRandom.uniform(1, N + 1));
                ++experimentResult[i];
            }
        }
    }

    public double mean() {                    // sample mean of percolation threshold
        if (!isMeanInCache) {
            mean = StdStats.mean(experimentResult);
            isMeanInCache = true;
        }
        return mean;
    }

    public double stddev() {                  // sample standard deviation of percolation threshold
        if (!isStddevInCache) {
            stddev = StdStats.stddev(experimentResult);
            isStddevInCache = true;
        }
        return stddev;
    }

    public double confidenceLo() {            // returns lower bound of the 95% confidence interval
        return mean() - 1.96 * stddev()/Math.sqrt(experimentResult.length);
    }

    public double confidenceHi() {            // returns upper bound of the 95% confidence interval()
        return mean() + 1.96 * stddev()/Math.sqrt(experimentResult.length);
    }

    public static void main(String[] args) {   // test client, described below
        int N = 0, T = 0;
        if (args.length > 1) {
            try {
                N = Integer.parseInt(args[0]);
                T = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Arguments" + args[0] + "and" + args[1] + " must be integers.");
                System.exit(1);
            }
        } else {
            System.err.println("Need at least two integer arguments.");
            System.exit(1);
        }
        PercolationStats percstats = new PercolationStats(N, T);
        System.out.printf("%-24s%s%n", "mean",      "= "+percstats.mean());
        System.out.printf("%-24s%s%n", "stddev",    "= "+percstats.mean());
        System.out.printf("%-24s%s%n", "95% confidence interval",
                "= "+percstats.confidenceLo() + ", " + percstats.confidenceHi()
        );
    }
}