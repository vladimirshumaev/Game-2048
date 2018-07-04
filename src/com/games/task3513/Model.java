package com.games.task3513;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 8;
    private Tile [][] gameTiles = new Tile [FIELD_WIDTH][FIELD_WIDTH];
     int score;
     int maxTile;
     private Stack<Tile[][]> previousStates = new Stack<>();
     private Stack<Integer> previousScores = new Stack<>();
     private boolean isSaveNeeded = true;

    public Model(){
        this.score = 0;
        this.maxTile = 0;
        resetGameTiles();
    }
    private void saveState (Tile [][] tile) {
        Tile[][] fieldToSave = new Tile[tile.length][tile[0].length];
        for (int i = 0 ; i < tile.length;i++){
            for (int j = 0 ; j < tile[0].length; j ++){
                fieldToSave[i][j] = new Tile(tile[i][j].value);
            }
        }
        previousStates.push(fieldToSave);
        int scoreToSave = score;
        previousScores.push(scoreToSave);
        isSaveNeeded = false;

    }
    public void rollback(){
       if (!previousScores.isEmpty() && !previousStates.isEmpty()){
           gameTiles = previousStates.pop();
           score = previousScores.pop();
       }
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }
    public boolean canMove() {
        if (!getEmptyTiles().isEmpty()) {
            return true;
        }
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length - 1; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j + 1].value) {
                    return true;
                }
            }
        }
        for (int j = 0; j < gameTiles.length; j++) {
            for (int i = 0; i < gameTiles.length - 1; i++) {
                if (gameTiles[i][j].value == gameTiles[i + 1][j].value) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Tile> getEmptyTiles(){
        List<Tile> list = new ArrayList<>();
        for (int i = 0 ; i < FIELD_WIDTH; i ++){
            for (int j = 0 ; j < FIELD_WIDTH; j++){

                if(gameTiles[i][j].isEmpty()) {
                    list.add(gameTiles[i][j]);
                }
            }
        }
        return list;
    }
    public void addTile (){
        List<Tile> listEmptyTiles = getEmptyTiles();

        if(listEmptyTiles.size()!= 0 ) {

            listEmptyTiles.get((int) ( listEmptyTiles.size()*Math.random())).value = (Math.random() < 0.9 ? 128 : 256);
        }
    }
    public void resetGameTiles(){
        for (int i = 0 ; i < FIELD_WIDTH; i ++){
            for (int j = 0 ; j < FIELD_WIDTH; j++){
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }
    private boolean compressTiles(Tile[] tiles){
        boolean flag = false;
        for (int i = 0 ; i < tiles.length; i++){
           if(i < tiles.length-1){
               if(tiles[i].value == 0 && tiles[i+1].value != 0 ) {
                   Tile temp = tiles[i];
                   tiles[i] = tiles[i+1];
                   tiles[i+1] = temp;
                   i = -1;
                   flag = true;
               }
           }
        }
        return flag;
    }
   private boolean mergeTiles(Tile[] tiles) {
        boolean flag = false;
        for (int i = 0 ; i < tiles.length; i++){
            if (i < tiles.length-1){
                if((tiles[i].value == tiles[i+1].value) && tiles[i].value != 0) {
                    tiles[i].value = tiles[i].value*2;
                    flag = true;
                    if(tiles[i].value > maxTile){
                        maxTile = tiles[i].value;
                    }
                    score += tiles[i].value;
                    tiles[i+1].value = 0;
                    compressTiles(tiles);
                }
            }
        }
        return flag;
   }

   public boolean hasBoardChanged(){
       int sum1 = 0;
       int sum2 = 0;
       if(!previousStates.isEmpty()){
           Tile [][] newTiles = previousStates.peek();
           for (int i = 0 ; i < FIELD_WIDTH; i++){
               for ( int j = 0 ; j < FIELD_WIDTH ; j++){
                   sum1 += gameTiles[i][j].value;
                   sum2 += newTiles[i][j].value;
               }


           }
       }
       return sum1 != sum2;
    }

    public MoveEfficiency getMoveEfficiency(Move move){
       MoveEfficiency moveEfficiency;
       move.move();
        if (hasBoardChanged()) moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        else moveEfficiency = new MoveEfficiency(-1, 0, move);
        rollback();

        return moveEfficiency;
    }
    public  void autoMove(){
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4,Collections.reverseOrder());
        priorityQueue.offer(getMoveEfficiency(() -> left()));
        priorityQueue.offer(getMoveEfficiency(() -> right()));
        priorityQueue.offer(getMoveEfficiency(() -> up()));
        priorityQueue.offer(getMoveEfficiency(() -> down()));

        priorityQueue.peek().getMove().move();
    }
   public void randomMove(){
        int randomNumber =  (int)(Math.random()*4);
        if (randomNumber == 0 ){
            left();
        }
        else if(randomNumber == 1){
            right();
        }
        else if(randomNumber == 2){
            up();
        }
        else if(randomNumber == 3){
            down();
        }

   }
   public void left (){
        boolean event = false;
        if(isSaveNeeded == true){
            saveState(gameTiles);
        }
        for (int i = 0 ; i < gameTiles.length ; i++){
            if(compressTiles(gameTiles[i])| mergeTiles(gameTiles[i])){
                event = true;
            }

        }
        if(event){
            addTile();
        }
        isSaveNeeded = true;

   }

    public void rotate() {
        for (int k = 0; k < 4; k++) {
            for (int j = k; j < 7 - k; j++) {
                Tile tmp = gameTiles[k][j];
                gameTiles[k][j] = gameTiles[j][7 - k];
                gameTiles[j][7 - k] = gameTiles[7 - k][7 - j];
                gameTiles[7 - k][7 - j] = gameTiles[7 - j][k];
                gameTiles[7 - j][k] = tmp;
            }
        }
    }
    public void down(){
        saveState(gameTiles);
        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }
    public void up(){
        saveState(gameTiles);
        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }
    public void right(){
        saveState(gameTiles);
        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

}
