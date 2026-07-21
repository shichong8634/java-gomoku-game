package Gobang;
//Jingzu Ge
//3184476

import java.util.Stack;
import java.util.Random;

public class Gomokulogic {

    // Define the size of the chessboard
    private int size = 15;
    private int[][] board;
    public int gameMode = 0; // 0: Classic, 1: Merging Mode
    private int currentTurn = 1; // 1: Black, 2: White

    // Add a stack to record the history of playing chess for repentance. Array save: [row, col, color, type] (type 0=normal drop, 1=fusion drop)
    private Stack<int[]> moveHistory;

    // Constructor
    public Gomokulogic() {
        board = new int[size][size];
        moveHistory = new Stack<>();
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + "");
            }
            System.out.println();
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    // Method to put a piece on the board
    public boolean placePiece(int r, int c, int color) {
        if (r >= 0 && r < 15 && c >= 0 && c < 15) {

            if (board[r][c] == 0) {
                board[r][c] = color;
                moveHistory.push(new int[]{r, c, color, 0});

                if (gameMode == 1) {
                    applyMerge(r, c, color);
                }

                switchTurn();
                return true;

            } else if (gameMode == 1 && board[r][c] == color) {
                moveHistory.push(new int[]{r, c, color, 1});

                if (gameMode == 1) {
                    applyMerge(r, c, color);
                }

                switchTurn();
                return true;

            } else {
                System.out.println("Error: Spot already taken.");
            }
        } else {
            System.out.println("Error: Out of bounds.");
        }

        return false;
    }

    //merge method
    private void applyMerge(int row, int col, int color) {
        int[][] board = this.board;

        int[][] dirs = {{0,1},{1,0},{1,1},{1,-1}};

        for (int[] d : dirs) {
            int count = 1;

            count += countDir(row, col, d[0], d[1], color);
            count += countDir(row, col, -d[0], -d[1], color);

            if (count >= 3) {
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = row + dr;
                        int nc = col + dc;

                        if (nr >= 0 && nr < 15 && nc >= 0 && nc < 15) {
                            if (board[nr][nc] != 0 && board[nr][nc] != color) {
                                board[nr][nc] = color;
                            }
                        }
                    }
                }
            }
        }
    }

    // Switch current round
    private void switchTurn() {
        currentTurn = (currentTurn == 1) ? 2 : 1;
    }

    // Repentance method
    public boolean undo() {
        if (moveHistory.isEmpty()) {
            return false; // You can't repent if you haven't played chess
        }
        int[] lastMove = moveHistory.pop();
        int r = lastMove[0];
        int c = lastMove[1];
        int type = lastMove[3];

        if (type == 0) {
            board[r][c] = 0;
        } else if (type == 1) {
            board[r][c] -= 10;
        }
        switchTurn(); // Turn backward
        return true;
    }

    // To assist in judging whether a piece belongs to a certain party (including the fused pieces)
    private boolean isPlayer(int val, int color) {
        return val == color || val == (color + 10);
    }

    // Check if the game is over
    public boolean checkWin(int r, int c, int color) {
        int count;
        // Horizontal
        count = 1;
        for (int i = c + 1; i < 15; i++) {
            if (isPlayer(board[r][i], color)) count++;
            else break;
        }
        for (int i = c - 1; i >= 0; i--) {
            if (isPlayer(board[r][i], color)) count++;
            else break;
        }
        if (count >= 5) return true;

        // Vertical
        count = 1;
        for (int i = r + 1; i < 15; i++) {
            if (isPlayer(board[i][c], color)) count++;
            else break;
        }
        for (int i = r - 1; i >= 0; i--) {
            if (isPlayer(board[i][c], color)) count++;
            else break;
        }
        if (count >= 5) return true;
        // Diagonal \
        count = 1;
        for (int i = 1; r + i < 15 && c + i < 15; i++) {
            if (isPlayer(board[r + i][c + i], color)) count++;
            else break;
        }
        for (int i = 1; r - i >= 0 && c - i >= 0; i--) {
            if (isPlayer(board[r - i][c - i], color)) count++;
            else break;
        }
        if (count >= 5) return true;

        // Diagonal /
        count = 1;
        for (int i = 1; r + i < 15 && c - i >= 0; i++) {
            if (isPlayer(board[r + i][c - i], color)) count++;
            else break;
        }
        for (int i = 1; r - i >= 0 && c + i < 15; i++) {
            if (isPlayer(board[r - i][c + i], color)) count++;
            else break;
        }
        if (count >= 5) return true;

        return false;
    }

    // Easy AI: find a random empty spot to move
    public int[] easyAIMove(int aiColor) {
        Random rand = new Random();
        int r, c;
        if (isFull()) return null;
        while (true) {
            r = rand.nextInt(15);
            c = rand.nextInt(15);
            if (board[r][c] == 0) {
                placePiece(r, c, aiColor);
                return new int[]{r, c};
            }
        }
    }

    // Medium AI:Make AI mode more flexible and more difficult to fight
    public int[] mediumAIMove(int aiColor) {
        if (isFull()) return null;

        int opponent = (aiColor == 1) ? 2 : 1;
        int maxScore = -1;
        int bestR = -1;
        int bestC = -1;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] == 0) {
                    int attack = evaluateScore(i, j, aiColor);
                    int defense = evaluateScore(i, j, opponent);
                    int score = attack + defense;

                    if (score > maxScore) {
                        maxScore = score;
                        bestR = i;
                        bestC = j;
                    }
                }
            }
        }

        if (bestR != -1) {
            placePiece(bestR, bestC, aiColor);
            return new int[]{bestR, bestC};
        }

        return easyAIMove(aiColor);
    }

    // Scoring function
    private int evaluateScore(int r, int c, int color) {
        int total = 0;
        int[][] dirs = {{0,1},{1,0},{1,1},{1,-1}};

        for (int[] d : dirs) {
            int count = 1;
            count += countDir(r,c,d[0],d[1],color);
            count += countDir(r,c,-d[0],-d[1],color);

            if (count >= 5) total += 100000;
            else if (count == 4) total += 10000;
            else if (count == 3) total += 1000;
            else if (count == 2) total += 100;
        }
        return total;
    }

    private int countDir(int r,int c,int dr,int dc,int color){
        int cnt = 0;
        for(int i=1;i<=4;i++){
            int nr = r + dr*i;
            int nc = c + dc*i;

            if(nr<0||nr>=15||nc<0||nc>=15) break;

            if(isPlayer(board[nr][nc],color)) cnt++;
            else break;
        }
        return cnt;
    }

    // Get what is at (r, c)
    public int getPiece(int r, int c) {
        if (r >= 0 && r < 15 && c >= 0 && c < 15) {
            return board[r][c];
        }
        return -1;
    }

    // Check if the board is full
    public boolean isFull() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = 0;
            }
        }
        currentTurn = 1;
        moveHistory.clear();

    }

    public void setPiece(int row, int col, int value) {
        if (row >= 0 && row < 15 && col >= 0 && col < 15) {
            board[row][col] = value;
        }
    }

    //New: Set current player
    public void setCurrentTurn(int turn) {
        currentTurn = turn;
    }

    //New: Loading endgame chessboard
    public boolean loadEndgame(int[][] endgameBoard) {
        if (endgameBoard == null) {
            return false;
        }
        //Reset game
        reset();
        //Load the endgame chessboard
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (endgameBoard[i][j] != 0) {
                    board[i][j] = endgameBoard[i][j];
                }
            }
        }

        currentTurn = 1;
        return true;
    }

}






