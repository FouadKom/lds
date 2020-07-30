/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fouad Komeiha
 */
public class Utility {
    
    private static char quote = '"';
    private static char separator = '|';
    
    
    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, separator, quote);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, quote);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {
        

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null || cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = quote;
        }

        if (separators == ' ') {
            separators = separator;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static boolean checkPath(String path) {
        File file = new File(path);

            if (!file.isDirectory()) {
                file = file.getParentFile();
                if (!file.exists()) {
                    File dir = new File(file.getPath());
                    return dir.mkdirs();
                }
            }

        return true;
    }
    
    public static boolean checkFile(String path) {
        File file = new File(path);
        if(!file.exists()){
            return false;
        }
        return true;
    }
    
    public static double normalizeValue(double value , double min , double max){
        return (value - min)/(max - min);
    }
    
    
   
    
}
