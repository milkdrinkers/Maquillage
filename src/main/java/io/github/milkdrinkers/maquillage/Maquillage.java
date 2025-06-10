package io.github.milkdrinkers.maquillage;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.command.CommandHandler;
import io.github.milkdrinkers.maquillage.config.ConfigHandler;
import io.github.milkdrinkers.maquillage.database.handler.DatabaseHandler;
import io.github.milkdrinkers.maquillage.database.handler.DatabaseHandlerBuilder;
import io.github.milkdrinkers.maquillage.database.sync.SyncHandler;
import io.github.milkdrinkers.maquillage.hook.HookManager;
import io.github.milkdrinkers.maquillage.listener.ListenerHandler;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.threadutil.SchedulerHandler;
import io.github.milkdrinkers.maquillage.translation.TranslationHandler;
import io.github.milkdrinkers.maquillage.updatechecker.UpdateHandler;
import io.github.milkdrinkers.maquillage.utility.DB;
import io.github.milkdrinkers.maquillage.utility.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Maquillage extends JavaPlugin {
    private static Maquillage instance;

    // Handlers/Managers
    private ConfigHandler configHandler;
    private TranslationHandler translationHandler;
    private DatabaseHandler databaseHandler;
    private HookManager hookManager;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private UpdateHandler updateHandler;
    private SchedulerHandler schedulerHandler;
    private SyncHandler syncHandler;

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

    public void onLoad() {
        instance = this;
        configHandler = new ConfigHandler(instance);
        translationHandler = new TranslationHandler(instance, configHandler);
        databaseHandler = new DatabaseHandlerBuilder()
            .withConfigHandler(configHandler)
            .withLogger(getComponentLogger())
            .build();
        hookManager = new HookManager(this);
        commandHandler = new CommandHandler(instance);
        listenerHandler = new ListenerHandler(instance);
        updateHandler = new UpdateHandler(this);
        schedulerHandler = new SchedulerHandler();
        syncHandler = new SyncHandler();

        handlers = List.of(
            configHandler,
            translationHandler,
            databaseHandler,
            hookManager,
            commandHandler,
            listenerHandler,
            updateHandler,
            schedulerHandler,
            syncHandler
        );

        DB.init(databaseHandler);
        for (Reloadable handler : handlers)
            handler.onLoad(instance);
    }

    public void onEnable() {
        for (Reloadable handler : handlers)
            handler.onEnable(instance);

        if (!DB.isReady()) {
            Logger.get().warn(ColorParser.of("<yellow>Database handler failed to start. Database support has been disabled.").build());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        NameColorHolder.getInstance().loadAll();
        TagHolder.getInstance().loadAll();
    }

    public void onDisable() {
        for (Reloadable handler : handlers.reversed()) // If reverse doesn't work implement a new List with your desired disable order
            handler.onDisable(instance);

        NameColorHolder.getInstance().cacheClear();
        TagHolder.getInstance().cacheClear();
    }

    public void onReload() {
        onDisable();
        onLoad();
        onEnable();
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    @NotNull
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    /**
     * Gets hook manager.
     *
     * @return the hook manager
     */
    @NotNull
    public HookManager getHookManager() {
        return hookManager;
    }

    /**
     * Gets update handler.
     *
     * @return the update handler
     */
    @NotNull
    public UpdateHandler getUpdateHandler() {
        return updateHandler;
    }

    public SyncHandler getSyncHandler() {
        return syncHandler;
    }
}
