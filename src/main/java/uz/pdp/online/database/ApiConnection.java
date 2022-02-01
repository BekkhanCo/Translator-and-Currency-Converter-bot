package uz.pdp.online.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import uz.pdp.online.model.Currency;
import uz.pdp.online.model.Translator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public interface ApiConnection {

    List<Currency> currencyList = getDataFromJson();
    List<String> languages = List.of("ru-ru", "ru-en", "ru-tr", "en-ru", "en-en", "en-tr", "tr-ru", "tr-en");

    static Currency getCurrencyByEngName(String name) {
        for (Currency currency : currencyList) {
            if (currency.getCcyNmEN().equals(name))
                return currency;
        }
        return null;
    }


    static List<Currency> getDataFromJson() {
        List<Currency> list = new ArrayList<>();
        try {
            URL url = new URL("https://cbu.uz/uz/arkhiv-kursov-valyut/json/");
            URLConnection urlConnection = url.openConnection();
            ObjectMapper objectMapper = new ObjectMapper();
            list = objectMapper.readValue(urlConnection.getInputStream(), new TypeReference<ArrayList<Currency>>() {
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    static Translator getTranslator(String text, String lang) {
        try {
            URL url = new URL("https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20220103T115131Z.e001ac42db0bbe00.6a0a7da14bed700366fdc2ab32fa2d090e95f76f&lang=" + lang + "&text=" + text + "");
            URLConnection urlConnection = url.openConnection();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(urlConnection.getInputStream(),Translator.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void start() {
        System.out.println(currencyList.getClass() + " ulandi");
    }

}
