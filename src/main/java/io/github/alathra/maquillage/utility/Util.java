package io.github.alathra.maquillage.utility;

import java.util.HashMap;

public class Util {

    public static String createKey(String label, HashMap<String, Integer> map) {
        String keyNoNum = label.replaceAll("[^a-zA-Z ]", "").toLowerCase().replace(" ", "_");
        String key = keyNoNum;

        int i = 1;
        while (map.containsKey(key)) {
            key = keyNoNum + "_" + i;
            i++;
        }
        return key;
    }

}
