package uz.pdp.online.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.online.database.ApiConnection;
import uz.pdp.online.model.*;
import uz.pdp.online.telegram.enums.BotStage;
import uz.pdp.online.service.ChatService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MyBot extends TelegramLongPollingBot implements BotUtils {


    final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###,###,###.###");

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = String.valueOf(message.getChatId());
            authChat();

            BotStage stage = BotStage.findByCode(message.getText().trim());
            String text = message.getText();
            switch (stage) {
                case START -> startRespond();
                case TO_UZS -> currencyPanel(true);
                case FROM_UZS -> currencyPanel(false);
                case CONVERTOR -> cConvertor();
                case BACK -> startRespond();
                case TRANSLATOR -> translator();
                case UNDEFINED -> {
                    switch (userChat.getState()) {
                        case FROM_UZS:
                        case TO_UZS:
                            converterResult(text);
                            break;
                        case TRANSLATOR:
                            translatorResult(text);
                            break;
                    }
                }
            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            chatId = callbackQuery.getMessage().getChatId().toString();

            Currency currency = ApiConnection.getCurrencyByEngName(callbackQuery.getData());
            userChat.setCurrency(currency);

            String messagetext = "";
            switch (userChat.getState()) {
                case TO_UZS:
                    messagetext = "FROM " + currency.getCcy() + " --> ???  UZS\n\n" +
                            "Enter amount: ";
                    break;
                case FROM_UZS:
                    messagetext = "FROM ??? UZS" + " --> " + currency.getCcy() + "\n\n" +
                            "Enter amount: ";
                    break;
                case TRANSLATOR:
                    userChat.setTranslateType(callbackQuery.getData());
                    messagetext = "Translator type : " + "\t\t-->\t\t" + userChat.getTranslateType().toUpperCase() + "\n\n\n\n" +
                            "Enter text : ";
            }

            try {
                execute(BotUtils.editMessage(chatId, callbackQuery.getMessage().getMessageId(), messagetext, null));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        }
        chatService.edit(userChat);

    }

    private static ChatService chatService = new ChatService();
    private static UserChat userChat;
    private static String chatId;

    public void startRespond() {
        try {
            execute(BotUtils.sendMessage(chatId, "WELCOME TO OUR 'Currency Converter' BOT ",
                    BotUtils.createReplyKeyboardMarkup(new ArrayList<>(Arrays.asList(" 💵  CONVERTER 💵 ", " Translator 👀 ")))));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void cConvertor(){
        try {
            execute(BotUtils.sendMessage(chatId, "Select Currency Convert Type",
                    BotUtils.createReplyKeyboardMarkup(new ArrayList<>(Arrays.asList(" TO UZS 🔺 ", " FROM UZS 🔻 "," Back 🔙 ")))));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void currencyPanel(boolean isToUZS) {
        if (isToUZS)
            userChat.setState(BotStage.TO_UZS);
        else userChat.setState(BotStage.FROM_UZS);

        try {
            execute(BotUtils.sendMessage(chatId, "Select Currency 💵",
                    BotUtils.createInlineKeyboard(BotUtils.availbleCurrencies)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void converterResult(String input) {
        String value;
        Currency currency = userChat.getCurrency();
        String respond = "Incompatible value entered ❌\nEnter again: ";
        if (ChatService.isDigit(input)) {
            respond = " Currency:  1" + currency.getCcy() + " ==  " + currency.getRate() + " UZS\n\n";

            switch (userChat.getState()) {
                case TO_UZS: {
                    value = decimalFormat.format(Double.valueOf(input) * Double.valueOf(currency.getRate()));
                    respond += " --  " + input + " " + currency.getCcy() + " ==== " + value + " UZS";

                }
                break;
                case FROM_UZS: {
                    value = decimalFormat.format(Double.valueOf(input) / Double.valueOf(currency.getRate()));
                    respond += " --  " + input + " UZS ==== " + value + " " + currency.getCcy();
                }
                break;
            }
        }

        try {
            execute(BotUtils.sendMessage(chatId, respond, null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void translator() {
        userChat.setState(BotStage.TRANSLATOR);
        try {
            execute(BotUtils.sendMessage(chatId, "Select Translator Languages Format: ",
                    BotUtils.createInlineKeyboard(ApiConnection.languages)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void translatorResult(String input) {
        Translator result = ApiConnection.getTranslator(input, userChat.getTranslateType());
        StringBuilder messageText = new StringBuilder();

        if (result != null && !result.getDef().isEmpty()) {
            DefItem defItem = result.getDef().get(0);
            TrItem trItem = defItem.getTr().get(0);
            messageText.append("Translate lang:\t\t").append(userChat.getTranslateType().toUpperCase());
            messageText.append("\n\nWord:\t\t").append(defItem.getText()).append(" ( ").append(defItem.getPos()).append(" ) \npronounciation:\t\t" + (defItem.getTs()));
            messageText.append("\n\n\nTranslate:");
            messageText.append("\n result :\t\t" + trItem.getText() + " ( " + trItem.getPos() + " )\n syn : \t\t");

            for (SynItem synItem : trItem.getSyn()) {
                messageText.append(synItem.getText() + ", ");
            }

        } else messageText.append("No result found!\n ‼");
        try {
            execute(BotUtils.sendMessage(chatId, messageText.toString(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void authChat() {
        userChat = chatService.check(chatId);

        if (userChat == null) {
            userChat = new UserChat(chatId);
            chatService.add(userChat);
        }

    }


}
