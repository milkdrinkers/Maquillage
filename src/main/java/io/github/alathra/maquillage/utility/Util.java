package io.github.alathra.maquillage.utility;

import java.util.HashMap;

public class Util {

    public static String createKey(String label, HashMap<String, Integer> map) {
        String keyNoNum = label;

        while (keyNoNum.contains("<")) {
            int start = keyNoNum.indexOf("<");
            int end = keyNoNum.indexOf(">");

            String s = keyNoNum;
            keyNoNum = s.substring(0, start) + s.substring(end);
        }

        keyNoNum = keyNoNum.replaceAll("[^a-zA-Z ]", "").toLowerCase().replace(" ", "_");
        String key = keyNoNum;

        int i = 1;
        while (map.containsKey(key)) {
            key = keyNoNum + "_" + i;
            i++;
        }
        return key;
    }

}
