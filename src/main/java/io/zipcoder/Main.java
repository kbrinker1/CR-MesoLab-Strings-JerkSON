package io.zipcoder;

import org.apache.commons.io.IOUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {


    public String readRawDataToString() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception {
        String output = (new Main()).readRawDataToString();

        System.out.println(output);
        // TODO: parse the data in output into items, and display to console.


        ItemParser itemParser = new ItemParser();
        HashMap<String, ArrayList<Item>> testMap = itemParser.getCompleteList();
        for (String key : testMap.keySet()) {
            for (int i = 0; i < testMap.get(key).size(); i++) {
                System.out.println(key + " " + testMap.get(key).get(i).getPrice());
            }
        }
        }
    }

