package io.github.alathra.maquillage;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.maquillage.command.CommandHandler;
import io.github.alathra.maquillage.config.ConfigHandler;
import io.github.alathra.maquillage.database.handler.DatabaseHandler;
import io.github.alathra.maquillage.database.sync.SyncHandler;
import io.github.alathra.maquillage.hook.BStatsHook;
import io.github.alathra.maquillage.hook.EssentialsHook;
import io.github.alathra.maquillage.hook.PAPIHook;
import io.github.alathra.maquillage.hook.VaultHook;
import io.github.alathra.maquillage.listener.ListenerHandler;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;
import io.github.alathra.maquillage.translation.TranslationManager;
import io.github.alathra.maquillage.updatechecker.UpdateChecker;
import io.github.alathra.maquillage.utility.ImportUtil;
import io.github.alathra.maquillage.utility.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Maquillage extends JavaPlugin {
    private static Maquillage instance;
    private ConfigHandler configHandler;
    private ImportUtil importUtil;
    private TranslationManager translationManager;
    private DatabaseHandler databaseHandler;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private UpdateChecker updateChecker;
    private static SyncHandler syncHandler;

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
        configHandler = new ConfigHandler(instance);
        importUtil = new ImportUtil();
        translationManager = new TranslationManager(instance);
        databaseHandler = new DatabaseHandler(configHandler, getComponentLogger());
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
        databaseHandler.onLoad();
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
        databaseHandler.onEnable();
        commandHandler.onEnable();
        listenerHandler.onEnable();
        updateChecker.onEnable();
        bStatsHook.onEnable();
        vaultHook.onEnable();
        essentialsHook.onEnable();
        papiHook.onEnable();

        if (!databaseHandler.isRunning()) {
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
        configHandler.onDisable();
        translationManager.onDisable();
        databaseHandler.onDisable();
        commandHandler.onDisable();
        listenerHandler.onDisable();
        updateChecker.onDisable();
        bStatsHook.onDisable();
        vaultHook.onDisable();
        essentialsHook.onDisable();
        papiHook.onDisable();

        NameColorHolder.getInstance().cacheClear();
        TagHolder.getInstance().cacheClear();
    }

    public void onReload() {
        onDisable();
        onLoad();
        onEnable();
    }

    /**
     * Gets data handler.
     *
     * @return the data handler
     */
    @NotNull
    public DatabaseHandler getDataHandler() {
        return databaseHandler;
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

    public ImportUtil getImportUtil() {
        return importUtil;
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
