package Gobang;

//Yutian Wang: Endgame Loader Module
//This class is responsible for reading level data from .txt files
//and converting it into a 2D array that the game can use.

    import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

    public class EndgameLoader {

        // Reads a text file and returns a 15x15 array.
        //Expected file format: 15 lines, each with 15 integers separated by spaces.
        //0 = Empty, 1 = Black, 2 = White
        //@param filePath The path to the endgame .txt file
        //@return A 15x15 int array, or null if loading fails
        public static int[][] loadEndgame(String filePath) {
            int[][] loadedBoard = new int[15][15];

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                int row = 0;

                // Read line by line until we reach 15 rows
                while ((line = br.readLine()) != null && row < 15) {
                    // Trim spaces and split by any whitespace
                    String[] values = line.trim().split("\\s+");

                    for (int col = 0; col < 15 && col < values.length; col++) {
                        try {
                            loadedBoard[row][col] = Integer.parseInt(values[col]);
                        } catch (NumberFormatException e) {
                            // If data is not a number, default to 0 (Empty)
                            loadedBoard[row][col] = 0;
                        }
                    }
                    row++;
                }
                System.out.println("Endgame loaded successfully from: " + filePath);

            } catch (IOException e) {
                System.err.println("Error: Could not read the file. Please check the path: " + filePath);
                return null;
            }

            return loadedBoard;
        }
    }









