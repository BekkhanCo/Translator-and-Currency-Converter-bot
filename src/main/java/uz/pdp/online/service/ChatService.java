package uz.pdp.online.service;

import uz.pdp.online.model.UserChat;

import java.util.ArrayList;
import java.util.List;

public class ChatService {



    private List<UserChat> userChatList = new ArrayList<>();

    public void add(UserChat userChat) {
        userChatList.add(userChat);
    }

    public UserChat check(String chatId) {
        for (UserChat userChat : userChatList) {
            if (userChat.getChatId().equals(chatId))
                return userChat;
        }
        return null;
    }

    public void edit(UserChat newUserChat) {
        int index = 0;
        for (UserChat userChat : userChatList) {
            if (userChat.getChatId().equals(newUserChat.getChatId())) {
                userChatList.set(index, newUserChat);
            }
            index++;
        }
    }

    public List<UserChat> getList(){
        return userChatList;
    }

    public static boolean isDigit(String num){
        for (char c : num.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }


}
