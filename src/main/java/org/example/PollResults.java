package org.example;

import javax.swing.*;
import java.awt.*;

public class PollResults extends JPanel {

    public PollResults(CardLayout mainCardLayout, JPanel mainPanel,
                       int windowWidth, int windowHeight,
                       PollBot bot){
        this.setBackground(Color.pink);
        this.setLayout(null);
        this.setBounds(0, 0, windowWidth, windowHeight);

        JLabel results = new JLabel();
        results.setText(bot.getPollResults());
        results.setSize(500, 500);

        JButton backButton = new JButton("Back");
        backButton.setBounds(0, windowHeight - 100, windowWidth - 100, 0);
        backButton.setSize(100, 50);

        this.add(results);
        this.add(backButton);

        backButton.addActionListener(e -> mainCardLayout.show(mainPanel, "main menu"));
    }
}
