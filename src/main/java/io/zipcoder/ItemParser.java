package io.zipcoder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {
    public HashMap<String, ArrayList<Item>> completeList = new HashMap<String, ArrayList<Item>>();
    int exceptionCounter = 0;


    public ArrayList<String> parseRawDataIntoStringArray(String rawData) {
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern, rawData);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException {
        if (nameFinder(rawItem) == null || priceFinder(rawItem) == null) {
            throw new ItemParseException();
        }

        String name = nameFinder(rawItem);
        Double price = Double.parseDouble(priceFinder(rawItem));
        String type = "food";
        String expiration = expFinder(rawItem);

        return new Item(name, price, type, expiration);
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem) {
        String stringPattern = "[;|^]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern, rawItem);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString) {
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }

    public String nameFinder(String rawNames) {
        StringBuilder found = new StringBuilder();

        String itemSearch = rawNames;
        Pattern foodNames = Pattern.compile("\\w+(?=\\Wprice)", Pattern.CASE_INSENSITIVE);
        Matcher m = foodNames.matcher(itemSearch);
        int lastMatchPos = 0;
        while (m.find()) {
            if (!m.group().equals("")) {
                found.append(m.group().replaceAll("\\d", "o"));
                lastMatchPos = m.end();
            }
            return found.toString().toLowerCase();
        }
        return null;
    }

    public String priceFinder(String rawPrices) {
        StringBuilder found = new StringBuilder();

        String itemSearch = rawPrices;
        Pattern foodNames = Pattern.compile("\\d+\\.\\d\\d", Pattern.CASE_INSENSITIVE);
        Matcher m = foodNames.matcher(itemSearch);
        int lastMatchPos = 0;
        while (m.find()) {
            if (!m.group().equals("")) {
                found.append(m.group() + "");
                lastMatchPos = m.end();
            }
            return found.toString();
        }
        return null;
    }

    public String expFinder(String rawExp) {
        StringBuilder found = new StringBuilder();

        String itemSearch = rawExp;
        Pattern foodNames = Pattern.compile("\\d{1,2}\\/\\d\\d\\/\\d{4}", Pattern.CASE_INSENSITIVE);
        Matcher m = foodNames.matcher(itemSearch);
        int lastMatchPos = 0;
        while (m.find()) {
            found.append(m.group() + "");
            lastMatchPos = m.end();
        }
        return found.toString();
    }


    public HashMap<String, ArrayList<Item>> getCompleteList() throws Exception {
        Main mainObject = new Main();
        ArrayList<String> rawinputdata = parseRawDataIntoStringArray(mainObject.readRawDataToString());
        for (String item : rawinputdata) {
            try {
                Item myItem = parseStringIntoItem(item);
                if (!completeList.containsKey(myItem.getName())) {
                    ArrayList<Item> myItemList = new ArrayList<Item>();
                    myItemList.add(myItem);
                    completeList.put(myItem.getName(), myItemList);
                } else {
                    completeList.get(myItem.getName()).add(myItem);
                }

            } catch (ItemParseException except) {
                exceptionCounter++;
            }
        }
        return completeList;
    }
}
