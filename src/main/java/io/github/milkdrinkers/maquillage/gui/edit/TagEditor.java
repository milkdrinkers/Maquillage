package io.github.milkdrinkers.maquillage.gui.edit;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
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
public final class TagEditor {
    // TODO Input validation on permission (don't allow weird as fucking symbols)
    public static void create(
        Player player,
        EditActionCallback onSuccessCb,
        EditActionCallback onUpdateCb,
        EditActionCallback onCancelCb
    ) {
        if (!player.hasPermission("maquillage.command.admin.create.tag")) {
            player.sendMessage(Bukkit.permissionMessage());
            return;
        }

        final TagGuiData data = new TagGuiData(
            "",
            "",
            "",
            0.0f
        );

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
            previewComponent = ColorParser.of(Translation.of("dialog.tag.editor.preview-format"))
                .with("preview", Component.empty())
                .with("player", player.getName())
                .papi(player)
                .mini(player)
                .build();
        } catch (Exception e) {
            previewComponent = Translation.as("dialog.tag.editor.preview-fallback");
        }

        final Component finalPreviewComponent = previewComponent;
        final Dialog dialog = Dialog.create(
            builder -> builder.empty()
                .base(
                    DialogBase.builder(Translation.as("dialog.tag.creator.title"))
                        .externalTitle(Component.empty())
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .pause(false)
                        .body(
                            List.of(
                                DialogBody.plainMessage(Translation.as("dialog.tag.editor.preview-title")),
                                DialogBody.plainMessage(
                                    finalPreviewComponent
                                )
                            )
                        )
                        .inputs(dialogInputs(data))
                        .build()
                )
                .type(
                    DialogType.multiAction(
                            editorActions(
                                data,
                                onSuccess,
                                onPreview,
                                onCancel
                            )
                        )
                        .build()
                )
        );

        player.showDialog(dialog);
    }

    public static void edit(
        Player player,
        Tag tag,
        EditActionCallback onSuccessCb,
        EditActionCallback onUpdateCb,
        EditActionCallback onCancelCb
    ) {
        final TagGuiData data = new TagGuiData(
            tag.getTag(),
            tag.getLabel(),
            tag.getPerm(),
            (float) tag.getWeight()
        );

        edit(
            player,
            data,
            onSuccessCb,
            onUpdateCb,
            onCancelCb
        );
    }

    public static TagGuiData extractData(DialogResponseView response) {
        final String tag = response.getText("tag");
        final String label = response.getText("label");
        final String permission = response.getText("permission");
        final Float weight = response.getFloat("weight");

        return new TagGuiData(
            tag,
            label,
            permission,
            weight
        );
    }

    public static void edit(
        Player player,
        TagGuiData data,
        EditActionCallback onSuccessCb,
        EditActionCallback onUpdateCb,
        EditActionCallback onCancelCb
    ) {
        if (!player.hasPermission("maquillage.command.admin.edit.tag")) {
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
            previewComponent = ColorParser.of(Translation.of("dialog.tag.editor.preview-format"))
                .with("preview", data.tag())
                .with("player", player.getName())
                .papi(player)
                .mini(player)
                .build();
        } catch (Exception e) {
            previewComponent = Translation.as("dialog.tag.editor.preview-fallback");
        }

        final Component finalPreviewComponent = previewComponent;
        final Dialog dialog = Dialog.create(
            builder -> builder.empty()
                .base(
                    DialogBase.builder(Translation.as("dialog.tag.editor.title"))
                        .externalTitle(Component.empty())
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .pause(false)
                        .body(
                            List.of(
                                DialogBody.plainMessage(Translation.as("dialog.tag.editor.preview-title")),
                                DialogBody.plainMessage(
                                    finalPreviewComponent
                                )
                            )
                        )
                        .inputs(
                            dialogInputs(data)
                        )
                        .build()
                )
                .type(
                    DialogType.multiAction(
                            editorActions(
                                data,
                                onSuccess,
                                onPreview,
                                onCancel
                            )
                        )
                        .build()
                )
        );

        player.showDialog(dialog);
    }

    private static List<ActionButton> editorActions(
        TagGuiData data,
        DialogAction onSuccess,
        DialogAction onPreview,
        DialogAction onCancel
    ) {
        return List.of(
            ActionButton.builder(Translation.as("dialog.tag.editor.input-success"))
                .action(
                    onSuccess
                )
                .tooltip(
                    Translation.as("dialog.tag.editor.input-success-tooltip")
                )
                .build(),
            ActionButton.builder(Translation.as("dialog.tag.editor.input-update"))
                .action(onPreview)
                .tooltip(
                    Translation.as("dialog.tag.editor.input-update-tooltip")
                )
                .build(),
            ActionButton.builder(Translation.as("dialog.tag.editor.input-cancel"))
                .action(
                    onCancel
                )
                .tooltip(
                    Translation.as("dialog.tag.editor.input-cancel-tooltip")
                )
                .build()
        );
    }

    private static List<DialogInput> dialogInputs(TagGuiData data) {
        return List.of(
            DialogInput.text("tag", Translation.as("dialog.tag.editor.input-tag"))
                .initial(data.tag())
                .labelVisible(true)
                .maxLength(255)
                .build(),
            DialogInput.text("label", Translation.as("dialog.tag.editor.input-label"))
                .initial(data.label())
                .labelVisible(true)
                .maxLength(255)
                .build(),
            DialogInput.text("permission", Translation.as("dialog.tag.editor.input-permission"))
                .initial(data.permission())
                .labelVisible(true)
                .maxLength(255)
                .build(),
            DialogInput.numberRange("weight", Translation.as("dialog.tag.editor.input-weight"), 0, 100)
                .step(1F)
                .initial(data.weight())
                .build()
        );
    }

    @FunctionalInterface
    public interface EditActionCallback {
        @ApiStatus.OverrideOnly
        void accept(DialogResponseView response, Player player, TagGuiData data);
    }

    public record TagGuiData(
        String tag,
        String label,
        String permission,
        Float weight
    ) {
    }
}
