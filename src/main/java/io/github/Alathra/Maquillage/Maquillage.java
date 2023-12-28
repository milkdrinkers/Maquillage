package io.github.Alathra.Maquillage;

import io.github.Alathra.Maquillage.db.DatabaseHandler;
import io.github.Alathra.Maquillage.command.CommandHandler;
import io.github.Alathra.Maquillage.config.ConfigHandler;
import io.github.Alathra.Maquillage.listener.ListenerHandler;
import io.github.Alathra.Maquillage.hooks.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Maquillage extends JavaPlugin {
    private static Maquillage instance;
    private ConfigHandler configHandler;
    private DatabaseHandler databaseHandler;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private static VaultHook vaultHook;

    public static Maquillage getInstance() {
        return instance;
    }

    public void onLoad() {
        instance = this;
        configHandler = new ConfigHandler(instance);
        databaseHandler = new DatabaseHandler(instance);
        commandHandler = new CommandHandler(instance);
        listenerHandler = new ListenerHandler(instance);

        configHandler.onLoad();
        databaseHandler.onLoad();
        commandHandler.onLoad();
        listenerHandler.onLoad();
        vaultHook.onLoad();
    }

    public void onEnable() {
        configHandler.onEnable();
        databaseHandler.onEnable();
        commandHandler.onEnable();
        listenerHandler.onEnable();
        vaultHook.onEnable();
    }

    public void onDisable() {
        configHandler.onDisable();
        databaseHandler.onDisable();
        commandHandler.onDisable();
        listenerHandler.onDisable();
        vaultHook.onDisable();
    }

    @NotNull
    public DatabaseHandler getDataHandler() {
        return databaseHandler;
    }

    @NotNull
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public static VaultHook getVaultHook() {
        return vaultHook;
    }
}
