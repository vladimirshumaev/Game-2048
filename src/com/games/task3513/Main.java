package com.games.task3513;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        JFrame game = new JFrame();


        game.setTitle("GAME 262144");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(665, 720);
        game.setResizable(false);

        game.add(controller.getView());

        game.setLocationRelativeTo(null);
        game.setVisible(true);

        File folder = new File("C:\\Users\\Михаил\\Desktop\\Prosharp");
        for (File file : folder.listFiles())
        {
            System.out.println(file.getName());
        }



    }

}
