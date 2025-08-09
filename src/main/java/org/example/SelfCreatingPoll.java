package org.example;

import javax.swing.*;
import java.awt.*;

public class SelfCreatingPoll extends JPanel {
    public SelfCreatingPoll(CardLayout mainCardLayout, JPanel mainPanel,
                            int windowWidth, int windowHeight){
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(null);

        JTextArea pollQuestion = new JTextArea("Enter question here");
        JTextArea ans1 = new JTextArea("Answer 1");
        JTextArea ans2 = new JTextArea("Answer 2");
        JTextArea ans3 = new JTextArea("Answer 3");
        JTextArea ans4 = new JTextArea("Answer 4");

        JCheckBox check1 = new JCheckBox();

        pollQuestion.setLineWrap(true);
        pollQuestion.setLocation(0, windowHeight / 18);
        pollQuestion.setSize(400, 70);
        this.add(pollQuestion);
        this.add(ans1);
        this.add(ans2);
        this.add(ans3);
        this.add(ans4);
    }
}
