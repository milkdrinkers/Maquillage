package io.github.milkdrinkers.maquillage.gui;

import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
import io.github.milkdrinkers.wordweaver.Translation;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

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

    public enum SortOrder {
        IGNORED,
        ASCENDING,
        DESCENDING
    }

    /**
     * Creates a comparator for gui entries that sorts according to provided values.
     * @return a comparator
     * @param <T> the type the comparator is sorting
     */
    public static <T> Comparator<T> sorter(
        Function<T, Integer> weightExtractor,
        Function<T, String> labelExtractor,
        SortOrder weightOrder,
        SortOrder labelOrder
    ) {
        Comparator<T> comparator = null;

        // weight sorting
        if (weightOrder != SortOrder.IGNORED) {
            comparator = Comparator.comparingInt(weightExtractor::apply);
            if (weightOrder == SortOrder.DESCENDING) {
                comparator = comparator.reversed();
            }
        }

        // label sorting
        if (labelOrder != SortOrder.IGNORED) {
            Comparator<T> labelComparator = Comparator.comparing(item -> labelExtractor.apply(item));
            if (labelOrder == SortOrder.DESCENDING) {
                labelComparator = labelComparator.reversed();
            }

            comparator = (comparator == null) ? labelComparator : comparator.thenComparing(labelComparator);
        }

        // return comparator that maintains order
        return comparator != null ? comparator : (a, b) -> 0;
    }

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
