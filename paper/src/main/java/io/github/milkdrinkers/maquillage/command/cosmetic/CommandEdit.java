package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.milkdrinkers.maquillage.gui.NameColorGui;
import io.github.milkdrinkers.maquillage.gui.TagGui;

public class CommandEdit {
    public static CommandAPICommand registerCommandEdit(boolean tags, boolean colors) {
        CommandAPICommand commandEdit = new CommandAPICommand("edit")
            .withPermission("maquillage.command.admin.edit")
            .withShortDescription("Edits a Maquillage color or tag.");

        if (tags)
            commandEdit.withSubcommand(registerSubcommandTag());

        if (colors)
            commandEdit.withSubcommand(registerSubcommandColor());

        return commandEdit;
    }

    private static CommandAPICommand registerSubcommandColor() {
        return new CommandAPICommand("color")
            .withPermission("maquillage.command.admin.edit.color")
            .executesPlayer((sender, args) -> {
                new NameColorGui().open(sender, true);
            });
    }

    private static CommandAPICommand registerSubcommandTag() {
        return new CommandAPICommand("tag")
            .withPermission("maquillage.command.admin.edit.tag")
            .executesPlayer((sender, args) -> {
                new TagGui().open(sender, true);
            });
    }

}
