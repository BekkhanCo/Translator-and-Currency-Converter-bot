package uz.pdp.online.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public interface BotUtils {

    String BOT_USERNAME = "http://t.me/weather_by_bekkhan_bot";
    String BOT_TOKEN = "5091314583:AAGHNJjBQkEIg4GzXPbccpxPpv9Dx7ZwC24";
    List<String> availbleCurrencies = List.of("US Dollar", "Russian Ruble", "Yuan Renminbi");

    static ReplyKeyboardMarkup createReplyKeyboardMarkup(List<String> list) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        for (int i = 1; i <= list.size(); i++) {
            keyboardRow.add(list.get(i - 1));
            if (i % 2 == 0 || i == list.size()) {
                keyboardRows.add(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    static SendMessage sendMessage(String chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        if (replyKeyboard != null) {
            sendMessage.setReplyMarkup(replyKeyboard);
        }

        return sendMessage;
    }

    static InlineKeyboardMarkup createInlineKeyboard(List<String> list) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(listButton);
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton;

        int index = 0;
        for (int i = 1; i <= 30; i++) {
            inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(list.get(index));
            inlineKeyboardButton.setCallbackData(list.get(index));
            row.add(inlineKeyboardButton);
            index++;

            if (i % 3 == 0) {
                listButton.add(row);
                row = new ArrayList<>();
            }
            if (index == list.size())
                break;
        }

        if (row.size() != 0)
            listButton.add(row);

        return inlineKeyboardMarkup;
    }

    static EditMessageText editMessage(String chatId, int messageId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId);
        editMessageText.setText(text);
        if (inlineKeyboardMarkup != null)
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        return editMessageText;
    }




}
