package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PollBot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = "8354974668:AAHL0LlV_TAB16x2ukelFTN5hlb3Yexhkn4";
    private static final String BOT_NAME = "etherberbot";

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Message received: " + update.getMessage().getText());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public PollBot(){
        super(BOT_TOKEN);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }
}
