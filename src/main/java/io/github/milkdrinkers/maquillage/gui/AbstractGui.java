package io.github.milkdrinkers.maquillage.gui;

import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.wordweaver.Translation;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.List;

@SuppressWarnings("unused")
public abstract class AbstractGui {
    abstract void open(Player p);

    abstract void reload(PaginatedGui gui, Player p);

    void onClose(PaginatedGui gui, InventoryCloseEvent e) {};

    void populate(PaginatedGui gui, Player p) {
        populateDecoration(gui, p);
        populateContent(gui, p);
        populateButtons(gui, p);
    };

    abstract void populateDecoration(PaginatedGui gui, Player p);
    abstract void populateButtons(PaginatedGui gui, Player p);
    abstract void populateContent(PaginatedGui gui, Player p);

    public static Component translate(String string) {
        return ColorParser.of(Translation.of(string))
            .papi()
            .mini()
            .legacy()
            .build();
    }

    public static Component translate(String string, Player p) {
        return ColorParser.of(Translation.of(string))
            .papi(p)
            .mini()
            .legacy()
            .build();
    }

    public static List<Component> translateList(String string) {
        return Translation.ofList(string).stream()
            .map(s -> ColorParser.of(s)
                .papi()
                .mini()
                .legacy()
                .build()
            )
            .toList();
    }

    public static List<Component> translateList(String string, Player p) {
        return Translation.ofList(string).stream()
            .map(s -> ColorParser.of(s)
                .papi(p)
                .mini(p)
                .legacy()
                .build()
            )
            .toList();
    }
}
