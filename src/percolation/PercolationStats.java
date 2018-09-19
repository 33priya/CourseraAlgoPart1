package percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] result;
    private int trials;
    private double mean;
    private double stdDev;

    public PercolationStats(int n, int trials){
        if(n <= 0 || trials <= 0){
            throw new IllegalArgumentException("Either n or trials can not be zero");
        }
        result = new double[trials];
        this.trials = trials;
        int size = n * n;
        while(trials > 0){
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()){
                int p = StdRandom.uniform(1, size);
                int row = (p % n != 0) ? ((p / n ) + 1) : (p / n );
                int col = (p % n) + 1;
                percolation.open(row, col);
            }

            result[--trials] = percolation.numberOfOpenSites()/((double)size);
        }

        mean = StdStats.mean(result);
        stdDev = StdStats.stddev(result);
    }

    public double mean(){
        return mean;
    }

    public double stddev(){
        return stdDev;
    }

    public double confidenceLo(){
        return mean - ((1.96 * stdDev)/Math.sqrt(trials));
    }

    public double confidenceHi(){
      return mean + ((1.96 * stdDev)/Math.sqrt(trials));
    }

    public static void main(String[] args){
        if (!StdIn.isEmpty()){
            int n = StdIn.readInt();
            StdOut.println(n);
            int trials = StdIn.readInt();
            StdOut.println(trials);
            PercolationStats percolationStats = new PercolationStats(n, trials);
            StdOut.println("mean\t\t\t\t\t= " + percolationStats.mean);
            StdOut.println("stddev\t\t\t\t\t= " +percolationStats.stdDev);
            StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
        }
    }
}
