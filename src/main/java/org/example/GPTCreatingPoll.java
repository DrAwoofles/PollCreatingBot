package org.example;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class GPTCreatingPoll extends JPanel {
    public GPTCreatingPoll(CardLayout mainCardLayout, JPanel mainPanel,
                           int windowWidth, int windowHeight,
                           PollBot bot) {
        // Survey questions made by ChatGPT
        this.setBackground(Color.pink);
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(null);
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(windowWidth - 120, windowHeight - 100, windowWidth, windowHeight);
        sendButton.setSize(100, 50);

        JButton backButton = new JButton("Back");
        backButton.setBounds(0, windowHeight - 100, windowWidth - 100, 0);
        backButton.setSize(100, 50);

        JLabel writingInstructions = new JLabel("Write in your ID." +
                " Then, in the next text area a topic \n" +
                "for a poll.");
        writingInstructions.setSize(800, 50);
        writingInstructions.setLocation(0, 0);

        JTextArea id = new JTextArea();
        id.setSize(400, 50);
        id.setLocation(0, writingInstructions.getHeight() + 10);

        JTextArea topicPrompt = new JTextArea();
        topicPrompt.setSize(400, 50);
        topicPrompt.setLocation(0, id.getHeight() + 100);

        this.add(sendButton);
        this.add(backButton);
        this.add(writingInstructions);
        this.add(id);
        this.add(topicPrompt);

        sendButton.addActionListener(e -> {
            if(!bot.isPollActive()){
                writingInstructions.setText("Good! Sending now...");
                try {
                    HttpResponse<String> response = Unirest.get("https://app.seker.live/fm1/send-message")
                            .queryString("id", id.getText())
                            .queryString("text",
                                    "write me up to 12 answers (you may randomise the number) to a "
                                            + topicPrompt.getText() +
                                            "poll, dividing each answer by a comma, in String, no spaces," +
                                            " with that being your only response")
                            .asString();
                    JSONObject responseChecker = new JSONObject(response.getBody());
                    if (!responseChecker.getBoolean("success")) {
                        switch (responseChecker.getInt("errorCode")) {
                            case 3000 -> writingInstructions.setText("You didn't write your ID. Try again");
                            case 3001 ->
                                    writingInstructions.setText("Your ID doesn't exist in the database. We're sorry");
                            case 3002 ->
                                    writingInstructions.setText("Your amount of chats to send is 0. You cant send anymore");
                            case 3003 -> writingInstructions.setText("You didn't write your text. Try again");
                            default ->
                                    writingInstructions.setText("We're sorry, a general error has occurred. Try again later");
                        }
                    } else {
                        bot.sendPoll(topicPrompt.getText(),
                                responseChecker.get("extra").toString().split(","));
                    }
                } catch (UnirestException exception) {
                    throw new RuntimeException(exception);
                }
            }
            else{
                writingInstructions.setText("A poll is running right now");
            }
        });
        backButton.addActionListener(e ->mainCardLayout.show(mainPanel,"main menu"));

}
}
