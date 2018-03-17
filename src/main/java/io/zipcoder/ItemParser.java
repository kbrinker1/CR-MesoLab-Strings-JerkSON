package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {


    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException{

        String name = nameFinder(rawItem);
        Double price = Double.parseDouble(priceFinder(rawItem));
        String type = "food";
        String expiration = expFinder(rawItem);
        return new Item(name, price, type, expiration);


    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;|^]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }

    public String nameFinder (String rawNames) throws Exception {
        StringBuilder found = new StringBuilder();

        String itemSearch = rawNames;
        Pattern foodNames = Pattern.compile("\\w+(?=\\Wprice)", Pattern.CASE_INSENSITIVE);
        Matcher m = foodNames.matcher(itemSearch);
        int lastMatchPos = 0;
           while (m.find())
            {
                found.append(m.group()+"\n");
                lastMatchPos = m.end();
            }
        return found.toString();
    }

    public String priceFinder (String rawPrices) throws Exception {
        StringBuilder found = new StringBuilder();

        String itemSearch = rawPrices;
        Pattern foodNames = Pattern.compile("\\d+\\.\\d\\d", Pattern.CASE_INSENSITIVE);
        Matcher m = foodNames.matcher(itemSearch);
        int lastMatchPos = 0;
        while (m.find())
        {
            found.append(m.group()+"\n");
            lastMatchPos = m.end();
        }
        return found.toString();
    }

    public String expFinder (String rawExp) throws Exception {
        StringBuilder found = new StringBuilder();

        String itemSearch = rawExp;
        Pattern foodNames = Pattern.compile("\\d{1,2}\\/\\d\\d\\/\\d{4}", Pattern.CASE_INSENSITIVE);
        Matcher m = foodNames.matcher(itemSearch);
        int lastMatchPos = 0;
        while (m.find())
        {
            found.append(m.group()+"\n");
            lastMatchPos = m.end();
        }
        return found.toString();
    }


}
