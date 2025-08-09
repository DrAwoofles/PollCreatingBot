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
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new PollBot());

        JFrame mainWindow = new JFrame("Polling Bot GUI");
        mainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        mainPanel.add(new MainPanel(cardLayout, mainPanel, WINDOW_WIDTH, WINDOW_HEIGHT),
                "main menu");
        mainPanel.add(new SelfCreatingPoll(cardLayout, mainPanel, WINDOW_WIDTH, WINDOW_HEIGHT),
                "create by yourself");
        mainPanel.add(new GPTCreatingPoll(cardLayout, mainPanel, WINDOW_WIDTH, WINDOW_HEIGHT),
                "create via AI");
        mainWindow.add(mainPanel);
        cardLayout.show(mainPanel, "create by yourself");

        mainWindow.setVisible(true);
    }
}