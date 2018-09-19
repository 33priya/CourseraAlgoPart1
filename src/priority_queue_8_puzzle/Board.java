package priority_queue_8_puzzle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {
    private int[][] blocks;
    private int dimension;
    private int manhattanDistance;
    private int noOfBlockOutOfPlace;

    public Board(int[][] blocks_) {
        if (blocks_ == null) {
            throw new IllegalArgumentException("arg is null");
        }

        dimension = blocks_.length;
        this.blocks = getCopiedArray(blocks_);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int item = blocks[i][j];
                if (item == 0) continue;

                int targetX = (item - 1) / dimension;
                int targetY = (item - 1) % dimension;
                int dx = i - targetX;
                int dy = j - targetY;
                manhattanDistance += Math.abs(dx) + Math.abs(dy);

                int x = (i * dimension) + (j + 1);
                if (item != x) {
                    noOfBlockOutOfPlace++;
                }
            }
        }
    }

    private int[][] getCopiedArray(int[][] blocks) {
        int[][] aux = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            System.arraycopy(blocks[i], 0, aux[i], 0, dimension);
        }

        return aux;
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        return noOfBlockOutOfPlace;
    }

    public int manhattan() {
        return manhattanDistance;
    }

    public boolean isGoal() {
        return (manhattanDistance == 0 && noOfBlockOutOfPlace == 0);
    }

    public Board twin() {
        int [][] aux = getCopiedArray(this.blocks);
        int rowIndexBlock1 = 0;
        int colIndexBlock1 = 0;
        int rowIndexBlock2 = 0;
        int colIndexBlock2 = 0;

        int count = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int item = aux[i][j];
                if (item == 0) continue;

                if (count == 0) {
                    rowIndexBlock1 = i;
                    colIndexBlock1 = j;
                    count++;
                } else if (count == 1) {
                    rowIndexBlock2 = i;
                    colIndexBlock2 = j;
                    count++;
                } else {
                    break;
                }
            }
        }

        int temp = aux[rowIndexBlock1][colIndexBlock1];
        aux[rowIndexBlock1][colIndexBlock1] = aux[rowIndexBlock2][colIndexBlock2];
        aux[rowIndexBlock2][colIndexBlock2] = temp;

        return new Board(aux);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        if (this.dimension != that.dimension) return false;
        if (this.noOfBlockOutOfPlace != that.noOfBlockOutOfPlace) return false;
        if (this.manhattanDistance != that.manhattanDistance) return false;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        return new NeighborsIterable(this);
    }

    public String toString() {
        if (blocks == null) {
            throw new IllegalArgumentException("arg is null");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.dimension);
        stringBuilder.append("\n");

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                stringBuilder.append(blocks[i][j]);
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    private class NeighborsIterable implements Iterable<Board> {
        private List<Board> neighbors;
        private int[][] searchBoardBlocks;
        private int emptyBlockRowIndex;
        private int emptyBlockColIndex;

        NeighborsIterable(Board searchBoard) {
            if (searchBoard == null) {
                throw new IllegalArgumentException("arg is null");
            }

            searchBoardBlocks = searchBoard.blocks;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int item = searchBoardBlocks[i][j];
                    if (item == 0) {
                        emptyBlockRowIndex = i;
                        emptyBlockColIndex = j;
                        break;
                    }
                }
            }

            prepareNeighbors();
        }

        private void prepareNeighbors() {
            neighbors = new ArrayList<>();
            if (emptyBlockRowIndex > 0) { // check top row
                neighbors.add(getNeighbor(emptyBlockRowIndex - 1, emptyBlockColIndex));
            }

            if (emptyBlockRowIndex < dimension - 1) { // check bottom row
                neighbors.add(getNeighbor(emptyBlockRowIndex + 1, emptyBlockColIndex));
            }

            if (emptyBlockColIndex > 0) { // check left column
                neighbors.add(getNeighbor(emptyBlockRowIndex, emptyBlockColIndex - 1));
            }

            if (emptyBlockColIndex < dimension - 1) { // check right column
                neighbors.add(getNeighbor(emptyBlockRowIndex, emptyBlockColIndex + 1));
            }
        }

        private Board getNeighbor(int row, int col) {
            int[][] aux = getCopiedArray(searchBoardBlocks);

            int temp = aux[emptyBlockRowIndex][emptyBlockColIndex];
            aux[emptyBlockRowIndex][emptyBlockColIndex] = aux[row][col];
            aux[row][col] = temp;

            return new Board(aux);
        }

        @Override
        public Iterator<Board> iterator() {
            if (neighbors == null) {
                throw new IllegalArgumentException("arg is null");
            }
            return neighbors.iterator();
        }
    }
}
