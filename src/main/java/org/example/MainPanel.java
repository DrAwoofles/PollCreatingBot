package org.example;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel{
    public MainPanel(CardLayout mainCardLayout, JPanel mainPanel,
                     int windowWidth, int windowHeight){
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        JButton selfPollCreateButton = new JButton("Create poll yourself");
        JButton gptPollCreateButton = new JButton("Ask ChatGPT to create a poll");
        this.add(selfPollCreateButton);
        this.add(gptPollCreateButton);
        selfPollCreateButton.setLocation(windowWidth / 2 - 200, windowHeight / 2);
        gptPollCreateButton.setLocation(windowWidth / 2 + 10, windowHeight / 2);
        selfPollCreateButton.setSize(150, 30);
        gptPollCreateButton.setSize(200, 30);
        selfPollCreateButton.addActionListener(e -> mainCardLayout.show(mainPanel,
                "create by yourself"));
        gptPollCreateButton.addActionListener(e -> mainCardLayout.show(mainPanel,
                "create via AI"));
        this.add(selfPollCreateButton);
        this.add(gptPollCreateButton);
    }
}
