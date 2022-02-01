package uz.pdp.online.model;

import lombok.Data;
import uz.pdp.online.telegram.enums.BotStage;

@Data

public class UserChat {

    private String chatId;
    private BotStage state;
    private Currency currency;
    private String translateType;

    public UserChat(String chatId) {
        this.chatId = chatId;
    }
}
