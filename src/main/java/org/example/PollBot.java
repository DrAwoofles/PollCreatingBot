package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMemberCount;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.polls.StopPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.io.File;

import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class PollBot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = "8354974668:AAHL0LlV_TAB16x2ukelFTN5hlb3Yexhkn4";
    private static final String BOT_NAME = "etherberbot";
    private static final Long GROUP_CHAT_ID = -1002986034429L;
    private static final File POLL_STATUS_FILE = new File("poll_endTime_file.txt");
    private Set<Long> voters;
    private int pollMessageId;
    private String pollResults;

    public String getPollResults(){
        return pollResults;
    }

    public boolean isPollActive(){
        if(!POLL_STATUS_FILE.exists()){
            System.out.println("the file doesnt exist");return true;}
        try{
            Scanner scanner = new Scanner(POLL_STATUS_FILE);
            //System.out.println("reading file...");
            if(scanner.hasNextLine()){
                //System.out.println("found writing. reading...");
               if(System.currentTimeMillis() / 1000L < Long.parseLong(scanner.nextLine())){
                   //System.out.println("current time is smaller than 5 mins");
                   return true;
               }
            }
            scanner.close();
        } catch (FileNotFoundException | NumberFormatException e) {
            //System.out.println("There's been an error");
            return true;
        }
        return false;
    }

    private void setPollActivityBy(long unixTimestamp){
        try{
            FileWriter activityDecider = new FileWriter(POLL_STATUS_FILE);
            activityDecider.write(String.valueOf(unixTimestamp));
            activityDecider.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendPoll(String questionTitle, String[] pollOptions){
        try {
            GetChatMemberCount chatMemberCount = new GetChatMemberCount(GROUP_CHAT_ID.toString());
            if (execute(chatMemberCount) >= 1) {

                SendPoll poll = new SendPoll();
                poll.setChatId(GROUP_CHAT_ID);
                poll.setQuestion(questionTitle);
                poll.setOptions(Arrays.asList(pollOptions));
                poll.setIsAnonymous(false);
                int closeDate = (int)(System.currentTimeMillis() / 1000L +
                        TimeUnit.MINUTES.toSeconds(5));
                poll.setCloseDate(closeDate);
                setPollActivityBy(closeDate);
                Message pollMessage = execute(poll);
                this.pollMessageId = pollMessage.getMessageId();

            } else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(GROUP_CHAT_ID);
                sendMessage.setText("Sorry, there's too little users for a poll to start!");
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            setPollActivityBy(0);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.getMessage().getChat().getType().equals("private")){
            if(update.getMessage().getText().equals("/start") ||
                    update.getMessage().getText().equals("hi")){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId());
                CreateChatInviteLink createChatInviteLink = new CreateChatInviteLink();
                createChatInviteLink.setChatId(GROUP_CHAT_ID);
                try {
                    ChatInviteLink inviteLink = execute(createChatInviteLink);
                    sendMessage.setText("Chat invite: " + inviteLink.getInviteLink());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (update.getMessage().getNewChatMembers() != null) {
            for(User user : update.getMessage().getNewChatMembers()){
                try {
                    execute(new SendMessage(GROUP_CHAT_ID.toString(), user.getUserName() + " has joined the group!"));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (update.hasPollAnswer()) {
            //System.out.println("We got an answer");
            PollAnswer pollAnswer = update.getPollAnswer();
            Long answeredId = pollAnswer.getUser().getId();
            this.voters.add(answeredId);
            try {
                if(this.voters.size() >= execute(new GetChatMemberCount(GROUP_CHAT_ID.toString())) - 1){
                    //System.out.println("Stopping poll...");
                    StopPoll stopPoll = new StopPoll(GROUP_CHAT_ID.toString(), this.pollMessageId);
                    execute(stopPoll);
                    this.voters.clear();
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            //System.out.println("Poll hasn't been stopped");
        } else if (update.hasPoll()) {
            Poll poll = update.getPoll();
            //System.out.println("got Poll update");
            if(poll.getIsClosed()){
                //System.out.println("The results are here");
                setPollActivityBy(0);
                this.pollResults = "";
                poll.getOptions().forEach(option -> {
                    this.pollResults += option.getText() + ": " + ((double)option.getVoterCount() / this.voters.size()) * 100 + "%\n";
                });
            }
        }
    }

    public PollBot(){
        super(BOT_TOKEN);
        try{
            if(!POLL_STATUS_FILE.exists()) POLL_STATUS_FILE.createNewFile();
            if(!isPollActive()) setPollActivityBy(0);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize the file for starting a poll",e);
        }
        this.voters = new HashSet<>();
        this.pollResults = "";
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }
}
