package Gobang;

/**
 * NAME:Yunlong Wang
 * STUDENT NUMBER:3184483
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessFrame extends JFrame {
    private BoardPanel boardPanel;
    private boolean canPlay = false; //Control whether falling is allowed.

    public ChessFrame() {
        this.setTitle("Five Chess Game"); //set form title
        this.setSize(650, 600); //set form size
        this.setResizable(false); //The parameter passed in false means that window maximization fails.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Closin the window code will also end.
        this.setLocationRelativeTo(null); //Center form display.

        boardPanel = new BoardPanel();
        add(boardPanel);
        this.setVisible(true); //Set whether the form is displayed


    }
}