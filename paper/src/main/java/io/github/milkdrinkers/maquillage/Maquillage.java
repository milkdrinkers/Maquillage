package io.github.milkdrinkers.maquillage;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.api.MaquillageAPI;
import io.github.milkdrinkers.maquillage.command.CommandHandler;
import io.github.milkdrinkers.maquillage.config.ConfigHandler;
import io.github.milkdrinkers.maquillage.cooldown.CooldownHandler;
import io.github.milkdrinkers.maquillage.database.handler.DatabaseHandler;
import io.github.milkdrinkers.maquillage.hook.HookManager;
import io.github.milkdrinkers.maquillage.listener.ListenerHandler;
import io.github.milkdrinkers.maquillage.messaging.MessagingHandler;
import io.github.milkdrinkers.maquillage.messaging.MessagingUtils;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.threadutil.SchedulerHandler;
import io.github.milkdrinkers.maquillage.translation.TranslationHandler;
import io.github.milkdrinkers.maquillage.updatechecker.UpdateHandler;
import io.github.milkdrinkers.maquillage.utility.DB;
import io.github.milkdrinkers.maquillage.utility.Logger;
import io.github.milkdrinkers.maquillage.utility.Messaging;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Maquillage extends AbstractMaquillage {
    private static Maquillage instance;

    // Handlers/Managers
    private ConfigHandler configHandler;
    private TranslationHandler translationHandler;
    private DatabaseHandler databaseHandler;
    private MessagingHandler messagingHandler;
    private HookManager hookManager;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private UpdateHandler updateHandler;
    private SchedulerHandler schedulerHandler;
    private CooldownHandler cooldownHandler;
    private MaquillageAPIProvider apiHandler;

    // Handlers list (defines order of load/enable/disable)
    private List<? extends Reloadable> handlers;

    /**
     * Gets plugin instance.
     *
     * @return the plugin instance
     */
    public static Maquillage getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;

        configHandler = new ConfigHandler(this);
        translationHandler = new TranslationHandler(configHandler);
        databaseHandler = DatabaseHandler.builder()
            .withConfigHandler(configHandler)
            .withLogger(getComponentLogger())
            .withMigrate(true)
            .build();
        messagingHandler = MessagingHandler.builder()
            .withLogger(getComponentLogger())
            .withName(getName())
            .build();
        hookManager = new HookManager(this);
        commandHandler = new CommandHandler(this);
        listenerHandler = new ListenerHandler(this);
        updateHandler = new UpdateHandler(this);
        schedulerHandler = new SchedulerHandler();
        cooldownHandler = new CooldownHandler();
        apiHandler = new MaquillageAPIProvider(this);

        handlers = List.of(
            configHandler,
            translationHandler,
            databaseHandler,
            schedulerHandler,
            messagingHandler,
            hookManager,
            commandHandler,
            listenerHandler,
            updateHandler,
            cooldownHandler,
            apiHandler
        );

        DB.init(databaseHandler);
        Messaging.init(messagingHandler);
        MessagingUtils.registerCodecs();
        for (Reloadable handler : handlers)
            handler.onLoad(instance);

    }

    @Override
    public void onEnable() {
        for (Reloadable handler : handlers)
            handler.onEnable(instance);

        if (!DB.isStarted()) {
            Logger.get().warn(ColorParser.of("<yellow>Database handler failed to start. Database support has been disabled.").build());
            throw new IllegalStateException("Database handler failed to start, disabling " + getName() + ".");
        }

        if (!Messaging.isReady() && configHandler.getDatabaseConfig().messaging.enabled) {
            Logger.get().warn(ColorParser.of("<yellow>Messaging handler failed to start. Messaging support has been disabled.").build());
            throw new IllegalStateException("Messaging handler failed to start, disabling " + getName() + ".");
        }

        NameColorHolder.getInstance().loadAll();
        TagHolder.getInstance().loadAll();
    }

    @Override
    public void onDisable() {
        for (Reloadable handler : handlers.reversed()) // If reverse doesn't work implement a new List with your desired disable order
            handler.onDisable(instance);

        NameColorHolder.getInstance().cacheClear();
        TagHolder.getInstance().cacheClear();
    }

    /**
     * Use to reload the entire plugin.
     */
    public void onReload() {
        onDisable();
        onLoad();
        onEnable();
    }

    @Override
    public @NotNull ConfigHandler getConfigHandler() {
        return configHandler;
    }

    /**
     * Gets hook manager.
     *
     * @return the hook manager
     */
    public @NotNull HookManager getHookManager() {
        return hookManager;
    }

    /**
     * Gets update handler.
     *
     * @return the update handler
     */
    public @NotNull UpdateHandler getUpdateHandler() {
        return updateHandler;
    }

    /**
     * Gets the api handler.
     *
     * @return the api handler
     */
    public @NotNull MaquillageAPI getApiHandler() {
        return apiHandler;
    }
}
