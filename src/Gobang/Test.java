package Gobang;
/**
 * NAME:Yunlong Wang
 * STUDENT NUMBER:3184483
 */

import javax.swing.*;
import java.util.Objects;

public class Test {
    public static void main(String[] args){
        ChessFrame cf = new ChessFrame();

        String name;
        while(true){
            name = JOptionPane.showInputDialog("Input your name,please."); //Input box message.
            if(name==null){
                int result3 = JOptionPane.showConfirmDialog(cf,"Do you want to exit input?");
                if(result3==2){
                    continue;
                } else if(result3==0){
                    System.exit(0);
                }
                else if(result3==1){
                    continue;
                }
            }
            if(name.trim().isEmpty()){
                JOptionPane.showMessageDialog(null,"Please input your name again.");
                continue;
            }else{
                JOptionPane.showMessageDialog(null,"OK，let's start!!!");
                break;
            }
        }
        JOptionPane.showMessageDialog(null,"This game has single player mode and multiplayer mode. \nThere are corresponding options to choose one of them to play. \nSee help for specific tutorials.");


    }
}
