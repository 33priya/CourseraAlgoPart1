package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] sites;
    private int n;
    private int virtualTop;
    private WeightedQuickUnionUF wquf;
    private int openSiteCount = 0;

    public Percolation(int n){
        if(n <= 0){
            throw new IllegalArgumentException("n can not be zero");
        }
        this.n = n;
        virtualTop = n * n;
        sites = new boolean[n][n];
        wquf = new WeightedQuickUnionUF(virtualTop + 2);
    }

    private void validateRequest(int row, int col){
        if(row <= 0 || row > n || col <= 0 || col > n){
            throw new IllegalArgumentException("row or column can not be zero");
        }
    }

    public void open(int row, int col){
        validateRequest(row, col);

        if(!isOpen(row, col)){
            openSite(row - 1, col - 1);
        }
    }

    public boolean isOpen(int row, int col){
        validateRequest(row, col);
        return sites[row - 1][col - 1];
    }

    private void openSite(int row, int col) {
        openSiteCount++;
        sites[row][col] = true;
        connectWithNeighbours(row, col);
        if(row == 0){
            wquf.union(col, virtualTop);
        }
        if(row == (n  - 1)){
            wquf.union(((row * n) + col), (virtualTop + 1));
        }
    }

    private void connectWithNeighbours(int row, int col) {
        int p = (n * row) + col;
        int q;

        //Connect Top
        if(row != 0){
            if(isSiteOpen(row - 1, col)){
                q = (n * (row - 1)) + col;
                wquf.union(p, q);
            }
        }

        //Connect Bottom
        if(row != n - 1){
            if(isSiteOpen(row + 1, col)){
                q = (n * (row + 1)) + col;
                wquf.union(p, q);
            }
        }

        //Connect Left
        if(col != 0){
            if(isSiteOpen(row, col - 1)){
                q = (n * row) + col - 1;
                wquf.union(p, q);
            }
        }

        //Connect Right
        if(col != n - 1){
            if(isSiteOpen(row, col + 1)){
                q = (n * row) + col + 1;
                wquf.union(p, q);
            }
        }
    }

    private boolean isSiteOpen(int row, int col){
        return isOpen(row + 1, col + 1);
    }

    public boolean isFull(int row, int col){
        validateRequest(row, col);
        row = row - 1;
        col = col - 1;
        int p = (n * row) + col;
        return wquf.connected(p, virtualTop);
    }

    public int numberOfOpenSites(){
        return openSiteCount;
    }

    public boolean percolates(){
        return wquf.connected(virtualTop, virtualTop + 1);
    }
}

