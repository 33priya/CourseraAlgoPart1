package priority_queue_8_puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private MinPQ<SearchNode> minPQ = new MinPQ<>();
    private Board initialBoard;
    private boolean isSolvable;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("arg is null");
        }

        initialBoard = initial;
        SearchNode searchNode = new SearchNode(initial, 0, null);
        minPQ.insert(searchNode);

        MinPQ<SearchNode> minPQTwin = new MinPQ<>();
        Board initialBoardTwin = initial.twin();
        SearchNode searchNodeTwin = new SearchNode(initialBoardTwin, 0, null);
        minPQTwin.insert(searchNodeTwin);

        while (!minPQ.min().isGoal() && !minPQTwin.min().isGoal()) {
            SearchNode minSearchNode = minPQ.delMin();
            Board searchBoard = minSearchNode.searchBoard;
            SearchNode predecessorSearchNode = minSearchNode.predecessorSearchNode;
            for (Board neighbourBoard : searchBoard.neighbors()) {
                if ((minSearchNode.moves == 0) || (!neighbourBoard.equals(predecessorSearchNode.searchBoard))) {
                    minPQ.insert(new SearchNode(neighbourBoard, minSearchNode.moves + 1, minSearchNode));
                }
            }

            SearchNode minSearchNodeTwin = minPQTwin.delMin();
            Board searchBoardTwin = minSearchNodeTwin.searchBoard;
            SearchNode predecessorSearchNodeTwin = minSearchNodeTwin.predecessorSearchNode;
            for (Board neighbourBoardTwin : searchBoardTwin.neighbors()) {
                if ((minSearchNodeTwin.moves == 0) || (!neighbourBoardTwin.equals(predecessorSearchNodeTwin.searchBoard))) {
                    minPQTwin.insert(new SearchNode(neighbourBoardTwin, minSearchNodeTwin.moves + 1, minSearchNodeTwin));
                }
            }
        }

        if (minPQ.min().isGoal()) {
            isSolvable = true;
        }
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        return this.isSolvable() ?  minPQ.min().moves : -1;
    }

    public Iterable<Board> solution() {
        if (!this.isSolvable()) {
            return null;
        }

        Stack<Board> stackBoard  = new Stack<>();
        SearchNode searchNode = minPQ.min();
        while (searchNode.predecessorSearchNode != null) {
            stackBoard.push(searchNode.searchBoard);
            searchNode = searchNode.predecessorSearchNode;
        }

        stackBoard.push(initialBoard);

        return stackBoard;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private Board searchBoard;
        private SearchNode predecessorSearchNode;
        private int moves;
        private int effectiveMoves;

        SearchNode (Board searchBoard, int moves, SearchNode predecessorSearchNode) {
            this.searchBoard = searchBoard;
            this.moves = moves;
            this.predecessorSearchNode = predecessorSearchNode;
            this.effectiveMoves = moves + searchBoard.manhattan();
        }

        public boolean isGoal() {
            return searchBoard.isGoal();
        }

        @Override
        public int compareTo(SearchNode that) {
            if (that == null) {
                throw new IllegalArgumentException("arg is null");
            }

            return Integer.compare(this.effectiveMoves, that.effectiveMoves);
        }
    }
}
