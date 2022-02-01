package uz.pdp.online.telegram.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public enum BotStage {

    START("/start"),
    TO_UZS("TO UZS 🔺"),
    FROM_UZS("FROM UZS 🔻"),
    BACK("Back 🔙"),
    CONVERTOR("💵  CONVERTER 💵"),
    TRANSLATOR("Translator 👀"),
    UNDEFINED("default");


    String code;

    public static BotStage findByCode(String code) {
        for (BotStage value : values()) {
            if (value.getCode().equals(code))
                return value;
        }
        return UNDEFINED;
    }

}
