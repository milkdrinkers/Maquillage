package io.github.milkdrinkers.maquillage.utility;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.HashMap;
import java.util.Map;

public class Util {
    private static final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

    public static String createKey(String label, Map<String, Integer> map) {
        Component labelComponent = ColorParser.of(label).legacy().build();
        String keyNoNum = plainTextComponentSerializer.serialize(labelComponent);

        if (keyNoNum.isEmpty()) {
            keyNoNum = "nameless";
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
