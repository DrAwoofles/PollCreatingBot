package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 500;
    public static void main(String[] args) throws TelegramApiException {
        PollBot pollBot = new PollBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(pollBot);

        JFrame mainWindow = new JFrame("Polling Bot GUI");
        mainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        mainPanel.add(new MainPanel(cardLayout, mainPanel, WINDOW_WIDTH, WINDOW_HEIGHT, pollBot),
                "main menu");
        mainPanel.add(new SelfCreatingPoll(cardLayout, mainPanel, WINDOW_WIDTH, WINDOW_HEIGHT, pollBot),
                "create by yourself");
        mainPanel.add(new GPTCreatingPoll(cardLayout, mainPanel, WINDOW_WIDTH, WINDOW_HEIGHT, pollBot),
                "create via AI");
        mainPanel.add(new PollResults(cardLayout, mainPanel, WINDOW_WIDTH, WINDOW_HEIGHT, pollBot),
                "results");
        mainWindow.add(mainPanel);
        cardLayout.show(mainPanel, "main menu");

        mainWindow.setVisible(true);
    }
}