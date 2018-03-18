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

    public String printFinalList() throws Exception {
        completeList = getCompleteList();
        StringBuilder listBuilder = new StringBuilder();

        for(Map.Entry<String,ArrayList<Item>>groceryItems:completeList.entrySet()){
            listBuilder.append("\nname: ");
            listBuilder.append(String.format("%8s",captitalizeFirstLetter(groceryItems.getKey())));
            listBuilder.append("\t\t\t\tseen: "+getOccurencesOfItems(groceryItems.getValue())+" times\n");
            listBuilder.append("==============="+"\t\t\t\t===============\n");
            String priceReport = generatePriceReport(groceryItems);
            listBuilder.append(priceReport);
            listBuilder.append("---------------"+"\t\t\t\t---------------\n");
        }

        listBuilder.append("\nErrors\t\t\t\t\t\tseen: "+exceptionCounter+" times\n");

        return listBuilder.toString();
    }

    public int getOccurencesOfItems(ArrayList list) {
        return list.size();
    }

    public int getOccurencesOfPrices(ArrayList<Item> list, Double price) {
        int priceCounter = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPrice().equals(price) ) {
                priceCounter++;
            }
        }
        return priceCounter;
    }

    public String generatePriceReport(Map.Entry<String, ArrayList<Item>> input) {
        String priceReport = "";
        ArrayList<Double> nonDupPrices = getUniquePrices(input);
        for(int i=0;i<nonDupPrices.size();i++){
            priceReport+="Price";
            priceReport+=(String.format("%10s",nonDupPrices.get(i)));
            priceReport+=("\t\t\t\tseen: "+ getOccurencesOfPrices(input.getValue(),nonDupPrices.get(i))+
                    " times\n");
        }
        return priceReport;

    }

    public ArrayList<Double> getUniquePrices(Map.Entry<String, ArrayList<Item>> input) {
        ArrayList<Double> uniquePrices = new ArrayList<>();
        for (int i=0;i<input.getValue().size();i++) {
            if (!uniquePrices.contains(input.getValue().get(i).getPrice())) {
                uniquePrices.add(input.getValue().get(i).getPrice());
            }
        }
        return uniquePrices;
    }

    public String captitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    }


//    public int counts() throws Exception {
//        String input = getCompleteList().toString();
//        String[] inputs = input.split("([\\W\\s+])");
//        Map<String, Integer> listCounter = new HashMap<String, Integer>();
//        for (String searchWord : inputs) {
//            if (listCounter.containsKey(searchWord)) {
//                listCounter.put(searchWord, listCounter.get(searchWord) + 1);
//            } else {
//                listCounter.put(searchWord, 1);
//            }
//        }
//        return listCounter.get();
//    }
//    public String printList() throws Exception{
//        ItemParser itemParser = new ItemParser();
//        StringBuilder finalList = new StringBuilder();
//        HashMap<String, ArrayList<Item>> testMap = itemParser.getCompleteList();
//        for (String key : testMap.keySet()) {
//            for (int i = 0; i < testMap.get(key).size(); i++) {
//                finalList.append(("name:      "+ key + " seen:"+testMap.get(key).size()+"\n" + testMap.get(key).get(i).getPrice()));
//            }
//        }
//        return finalList.toString();
//    }

