package org.example;

import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SelfCreatingPoll extends JPanel {

    public static final String ANSWER_INSTRUCTIONS = "Answers of Poll - divided by a comma (ex: soccer,football,...)";

    public SelfCreatingPoll(CardLayout mainCardLayout, JPanel mainPanel,
                            int windowWidth, int windowHeight,
                            PollBot bot){
        // Survey questions made by humans
        this.setBackground(Color.pink);
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(null);
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(windowWidth - 120, windowHeight - 100, windowWidth, windowHeight);
        sendButton.setSize(100, 50);

        JButton backButton = new JButton("Back");
        backButton.setBounds(0, windowHeight - 100, windowWidth - 100, 0);
        backButton.setSize(100, 50);

        JLabel writingInstructions = new JLabel("Delete the text in the text area to write.");

        JTextArea pollTitle = new JTextArea("Title of Poll");
        JTextArea pollAnswers = new JTextArea(ANSWER_INSTRUCTIONS);

        writingInstructions.setSize(500, 100);
        writingInstructions.setLocation(windowWidth / 3, 0);

        pollTitle.setSize(400, 50);
        pollTitle.setLocation(0, writingInstructions.getHeight());
        pollAnswers.setSize(400, 50);
        pollAnswers.setLocation(0, pollTitle.getHeight() + 100);

        this.add(sendButton);
        this.add(writingInstructions);
        this.add(pollTitle);
        this.add(pollAnswers);
        this.add(backButton);

        sendButton.addActionListener(e -> {
            if(pollAnswers.getText().split(",").length > 12){
                writingInstructions.setText("Sorry, too many answers");
            }
            else if(pollAnswers.getText().contains(", ")){
                writingInstructions.setText("You have a spacebar after a comma. We cannot accept this");
            }
            else if(pollAnswers.getText().isEmpty() || pollAnswers.getText().equals(ANSWER_INSTRUCTIONS)){
                writingInstructions.setText("You haven't added any answers to the poll");
            }
            else{
                if(bot.isPollActive()){
                    writingInstructions.setText("Sorry, but a poll is running right now");
                }
                else {
                    writingInstructions.setText("Thank you! Sending this to the bot now...");
                    bot.sendPoll(pollTitle.getText(), pollAnswers.getText().split(","));
                }
            }
        });
        backButton.addActionListener(e -> mainCardLayout.show(mainPanel, "main menu"));
    }
}
