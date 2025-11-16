package io.github.milkdrinkers.maquillage.gui.edit;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.wordweaver.Translation;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class NameColorEditor {
    public static void edit(
        Player player,
        NameColor color,
        EditActionCallback onSuccessCb,
        EditActionCallback onUpdateCb,
        EditActionCallback onCancelCb
    ) {
        final NameColorGuiData data = new NameColorGuiData(
            color.getColor(),
            color.getLabel(),
            color.getPerm(),
            (float) color.getWeight()
        );

        edit(
            player,
            data,
            onSuccessCb,
            onUpdateCb,
            onCancelCb
        );
    }

    public static NameColorGuiData extractData(DialogResponseView response) {
        final String color = response.getText("color");
        final String label = response.getText("label");
        final String permission = response.getText("permission");
        final Float weight = response.getFloat("weight");

        return new NameColorGuiData(
            color,
            label,
            permission,
            weight
        );
    }

    public static void edit(
        Player player,
        NameColorGuiData data,
        EditActionCallback onSuccessCb,
        EditActionCallback onUpdateCb,
        EditActionCallback onCancelCb
    ) {
        if (!player.hasPermission("maquillage.command.admin.edit.color")) {
            player.sendMessage(Bukkit.permissionMessage());
            return;
        }

        final DialogAction onSuccess = DialogAction.customClick((response, audience) -> {
                if (!(audience instanceof Player p))
                    return;

                onSuccessCb.accept(response, p, extractData(response));
            },
            ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build()
        );

        final DialogAction onCancel = DialogAction.customClick(
            (response, audience) -> {
                if (!(audience instanceof Player p))
                    return;

                onCancelCb.accept(response, p, extractData(response));
            },
            ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build()
        );

        final DialogAction onPreview = DialogAction.customClick(
            (response, audience) -> {
                if (!(audience instanceof Player p))
                    return;

                onUpdateCb.accept(response, p, extractData(response));
                p.closeDialog();
                edit(p, extractData(response), onSuccessCb, onUpdateCb, onCancelCb);
            },
            ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build()
        );

        Component previewComponent;
        try {
            previewComponent = ColorParser.of(Translation.of("dialog.namecolor.editor.preview-format"))
                .with("preview", data.color())
                .with("player", player.getName())
                .papi(player)
                .mini(player)
                .build();
        } catch (Exception e) {
            previewComponent = Translation.as("dialog.namecolor.editor.preview-fallback");
        }

        final Component finalPreviewComponent = previewComponent;
        final Dialog dialog = Dialog.create(
            builder -> builder.empty()
                .base(
                    DialogBase.builder(Translation.as("dialog.namecolor.editor.title"))
                        .externalTitle(Component.empty())
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .pause(false)
                        .body(
                            List.of(
                                DialogBody.plainMessage(Translation.as("dialog.namecolor.editor.preview-title")),
                                DialogBody.plainMessage(
                                    finalPreviewComponent
                                )
                            )
                        )
                        .inputs(
                            List.of(
                                DialogInput.text("color", Translation.as("dialog.namecolor.editor.input-color"))
                                    .initial(data.color())
                                    .labelVisible(true)
                                    .maxLength(255)
                                    .build(),
                                DialogInput.text("label", Translation.as("dialog.namecolor.editor.input-label"))
                                    .initial(data.label())
                                    .labelVisible(true)
                                    .maxLength(255)
                                    .build(),
                                DialogInput.text("permission", Translation.as("dialog.namecolor.editor.input-permission"))
                                    .initial(data.permission())
                                    .labelVisible(true)
                                    .maxLength(255)
                                    .build(),
                                DialogInput.numberRange("weight", Translation.as("dialog.namecolor.editor.input-weight"), 0, 100)
                                    .step(1F)
                                    .initial(data.weight())
                                    .build()
                            )
                        )
                        .build()
                )
                .type(
                    DialogType.multiAction(
                            List.of(
                                ActionButton.builder(Translation.as("dialog.namecolor.editor.input-success"))
                                    .action(
                                        onSuccess
                                    )
                                    .tooltip(
                                        Translation.as("dialog.namecolor.editor.input-success-tooltip")
                                    )
                                    .build(),
                                ActionButton.builder(Translation.as("dialog.namecolor.editor.input-update"))
                                    .action(onPreview)
                                    .tooltip(
                                        Translation.as("dialog.namecolor.editor.input-update-tooltip")
                                    )
                                    .build(),
                                ActionButton.builder(Translation.as("dialog.namecolor.editor.input-cancel"))
                                    .action(
                                        onCancel
                                    )
                                    .tooltip(
                                        Translation.as("dialog.namecolor.editor.input-cancel-tooltip")
                                    )
                                    .build()
                            )
                        )
                        .build()
                )
        );

        player.showDialog(dialog);
    }

    @FunctionalInterface
    public interface EditActionCallback {
        @ApiStatus.OverrideOnly
        void accept(DialogResponseView response, Player player, NameColorGuiData data);
    }

    public record NameColorGuiData(
        String color,
        String label,
        String permission,
        Float weight
    ) {
    }
}
