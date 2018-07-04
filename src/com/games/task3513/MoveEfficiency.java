package com.games.task3513;

public class MoveEfficiency implements Comparable<MoveEfficiency>{
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;

    }

    public Move getMove() {
        return move;
    }


    @Override
    public int compareTo(MoveEfficiency that) {
        if(this == that) {
            return 0;
        }

        if(this.numberOfEmptyTiles > that.numberOfEmptyTiles){return 1;}
        else if (this.numberOfEmptyTiles < that.numberOfEmptyTiles){return - 1;}
        else if (this.numberOfEmptyTiles == that.numberOfEmptyTiles) {
            if(this.score > that.score){return 1;}
            else if(this.score < that.score){return -1;}
            else if(this.score == that.score) {return 0;}
        }
        return 0;
    }
}
