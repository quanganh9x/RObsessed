package com.quanganh9x.rss;

import org.jdom2.Element;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLMainController extends XMLHandler {
    @Override
    public void setPath() {
        this.path = "/Users/quanganh9x/Desktop/quanganh9x/maindata.xml";
    }

    public List<Element> sitesName() {
        return trace(Arrays.asList("data", "sites"));
    }

    public int lookup(String type, String what) { // 0,1 as types
        List<Element> sitesName = sitesName();
        for (int i = 0; i < sitesName.size(); i++) {
            if (getChildName(sitesName.get(i), type).equals(what)) return i;
        }
        return -1;
    }

    public String lookupAndGetURL(String name) {
        int i = lookup("name", name);
        if (i != -1) {
            List<Element> sitesName = sitesName();
            return getChildName(sitesName.get(i), "url");
        }
        return null;
    }

    public String getBetween(String text, String pattern1, String pattern2) throws IllegalStateException {
        String regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        return matcher.group(1);
    }

    public static int getPosTo(String str, String substr) {
        int substrlen = substr.length();
        int strlen = str.length();
        int j = 0;
        if (substrlen >= 1) {
            for (int i = 0; i < strlen; i++) {              // iterate through main string
                if (str.charAt(i) == substr.charAt(j)) {    // check substring
                    j++;                                    // iterate
                    if (j == substrlen) {                   // when to stop
                        return i;
                    }
                }
                else {
                    j = 0;
                }
            }
        }
        return -1;
    }
    /*public int convertToInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }*/
}