package io.github.milkdrinkers.maquillage;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.command.CommandHandler;
import io.github.milkdrinkers.maquillage.config.ConfigHandler;
import io.github.milkdrinkers.maquillage.database.handler.DatabaseHandlerBuilder;
import io.github.milkdrinkers.maquillage.database.sync.SyncHandler;
import io.github.milkdrinkers.maquillage.hook.BStatsHook;
import io.github.milkdrinkers.maquillage.hook.EssentialsHook;
import io.github.milkdrinkers.maquillage.hook.PAPIHook;
import io.github.milkdrinkers.maquillage.hook.VaultHook;
import io.github.milkdrinkers.maquillage.listener.ListenerHandler;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.translation.TranslationManager;
import io.github.milkdrinkers.maquillage.updatechecker.UpdateChecker;
import io.github.milkdrinkers.maquillage.utility.DB;
import io.github.milkdrinkers.maquillage.utility.Logger;
import io.github.milkdrinkers.threadutil.PlatformBukkit;
import io.github.milkdrinkers.threadutil.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Maquillage extends JavaPlugin {
    private static Maquillage instance;
    private ConfigHandler configHandler;
    private TranslationManager translationManager;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private UpdateChecker updateChecker;
    private static SyncHandler syncHandler;
    private @SuppressWarnings("unused") MaquillageAPIProvider apiProvider;

    // Hooks
    private static BStatsHook bStatsHook;
    private static VaultHook vaultHook;
    private static EssentialsHook essentialsHook;
    private static PAPIHook papiHook;

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
        apiProvider = new MaquillageAPIProvider(this);
        Scheduler.init(new PlatformBukkit(this));
        Scheduler.setErrorHandler(e -> this.getSLF4JLogger().error("[Scheduler]: {}", e.getMessage()));
        configHandler = new ConfigHandler(instance);
        translationManager = new TranslationManager(instance);
        DB.init(
            new DatabaseHandlerBuilder()
                .withConfigHandler(configHandler)
                .withLogger(getComponentLogger())
                .build()
        );
        commandHandler = new CommandHandler(instance);
        listenerHandler = new ListenerHandler(instance);
        updateChecker = new UpdateChecker();
        bStatsHook = new BStatsHook(instance);
        vaultHook = new VaultHook(instance);
        essentialsHook = new EssentialsHook();
        papiHook = new PAPIHook(instance);
        syncHandler = new SyncHandler();

        configHandler.onLoad();
        translationManager.onLoad();
        DB.getHandler().onLoad();
        commandHandler.onLoad();
        listenerHandler.onLoad();
        updateChecker.onLoad();
        bStatsHook.onLoad();
        vaultHook.onLoad();
        essentialsHook.onLoad();
        papiHook.onLoad();
        syncHandler.onLoad();
    }

    public void onEnable() {
        configHandler.onEnable();
        translationManager.onEnable();
        DB.getHandler().onEnable();
        commandHandler.onEnable();
        listenerHandler.onEnable();
        updateChecker.onEnable();
        bStatsHook.onEnable();
        vaultHook.onEnable();
        essentialsHook.onEnable();
        papiHook.onEnable();

        if (!DB.isReady()) {
            Logger.get().warn(ColorParser.of("<yellow>Database handler failed to start. Database support has been disabled.").build());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (!vaultHook.isVaultLoaded()) {
            Logger.get().warn(ColorParser.of("<yellow>Vault is required by this plugin.").build());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (essentialsHook.isHookLoaded()) {
            Logger.get().info(ColorParser.of("<green>EssentialsX has been found on this server. EssentialsX support enabled.").build());
        } else {
            Logger.get().warn(ColorParser.of("<yellow>EssentialsX is not installed on this server. EssentialsX support has been disabled.").build());
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Logger.get().warn(ColorParser.of("<yellow>Maquillage features will be limited as PlaceholderAPI is not installed on this server.").build());
        }

        NameColorHolder.getInstance().loadAll();
        TagHolder.getInstance().loadAll();
        syncHandler.onEnable();
    }

    public void onDisable() {
        Scheduler.shutdown();
        configHandler.onDisable();
        translationManager.onDisable();
        DB.getHandler().onDisable();
        commandHandler.onDisable();
        listenerHandler.onDisable();
        updateChecker.onDisable();
        bStatsHook.onDisable();
        vaultHook.onDisable();
        essentialsHook.onDisable();
        papiHook.onDisable();
        apiProvider = null;

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
     * Gets config handler.
     *
     * @return the translation handler
     */
    @NotNull
    public TranslationManager getTranslationManager() {
        return translationManager;
    }

    /**
     * Gets update checker.
     *
     * @return the update checker
     */
    @NotNull
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    /**
     * Gets bStats hook.
     *
     * @return the bStats hook
     */
    @NotNull
    public static BStatsHook getBStatsHook() {
        return bStatsHook;
    }

    /**
     * Gets vault hook.
     *
     * @return the vault hook
     */
    @NotNull
    public static VaultHook getVaultHook() {
        return vaultHook;
    }

    public static EssentialsHook getEssentialsHook() {
        return essentialsHook;
    }

    public static SyncHandler getSyncHandler() {
        return syncHandler;
    }
}
