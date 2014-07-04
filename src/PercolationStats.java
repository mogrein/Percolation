/**
 * Created by mogrein on 03.07.14.
 */

public class PercolationStats {
    private double[] experimentResult;
    private double mean;
    private boolean isMeanInCache = false;
    private double stddev;
    private boolean isStddevInCache = false;

    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Number of experiments"
                   + " and grid Size must be positive numbers.");
        }
        experimentResult = new double[T];
        int experimentSize = N * N;
        isMeanInCache = false;
        isStddevInCache = false;
        Percolation experiment;
        for (int i = 0; i < experimentResult.length; ++i) {
            int random, x, y, counter = 0;
            experiment = new Percolation(N);
            while (!experiment.percolates()) {
                //get uniformly distributed square and open it if not already opened
                random = StdRandom.uniform(0, experimentSize);
                x = random / N + 1;
                y = random % N + 1;
                if (!experiment.isOpen(x, y)) {
                    experiment.open(x, y);
                    ++counter;
                }
            }
            experimentResult[i] = (double) counter/experimentSize;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (!isMeanInCache) {
            mean = StdStats.mean(experimentResult);
            isMeanInCache = true;
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (!isStddevInCache) {
            stddev = StdStats.stddev(experimentResult);
            isStddevInCache = true;
        }
        return stddev;
    }

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev()/Math.sqrt(experimentResult.length);
    }

    // returns upper bound of the 95% confidence interval()
    public double confidenceHi() {
        return mean() + 1.96 * stddev()/Math.sqrt(experimentResult.length);
    }

    // test client, described below
    public static void main(String[] args) {
        int N, T;
        if (args.length > 1) {
            try {
                N = Integer.parseInt(args[0]);
                T = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Arguments" + args[0]
                        + "and" + args[1] + " must be integers.");
            }
        } else {
            throw new IllegalArgumentException("Need at least two integer"
                    +" arguments.");
        }
        PercolationStats percstats = new PercolationStats(N, T);
        System.out.printf("%-24s%s%n", "mean", "= " + percstats.mean());
        System.out.printf("%-24s%s%n", "stddev",    "= "+percstats.stddev());
        System.out.printf("%-24s%s%n", "95% confidence interval",
                "= "+percstats.confidenceLo() + ", " + percstats.confidenceHi()
        );
    }
}