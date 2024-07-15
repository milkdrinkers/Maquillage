package io.github.Alathra.Maquillage;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.db.DatabaseHandler;
import io.github.Alathra.Maquillage.command.CommandHandler;
import io.github.Alathra.Maquillage.config.ConfigHandler;
import io.github.Alathra.Maquillage.hooks.EssentialsHook;
import io.github.Alathra.Maquillage.listener.ListenerHandler;
import io.github.Alathra.Maquillage.hooks.VaultHook;
import io.github.Alathra.Maquillage.module.namecolor.NameColorHolder;
import io.github.Alathra.Maquillage.module.tag.TagHolder;
import io.github.Alathra.Maquillage.placeholders.PlaceholderHandler;
import io.github.Alathra.Maquillage.utility.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Maquillage extends JavaPlugin {
    private static Maquillage instance;
    private ConfigHandler configHandler;
    private DatabaseHandler databaseHandler;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private static VaultHook vaultHook;
    private static EssentialsHook essentialsHook;
    private PlaceholderHandler placeholderHandler;

    public static Maquillage getInstance() {
        return instance;
    }

    public void onLoad() {
        instance = this;
        configHandler = new ConfigHandler(instance);
        databaseHandler = new DatabaseHandler(instance);
        commandHandler = new CommandHandler(instance);
        listenerHandler = new ListenerHandler(instance);
        vaultHook = new VaultHook(instance);
        essentialsHook = new EssentialsHook();
        placeholderHandler = new PlaceholderHandler(instance);

        configHandler.onLoad();
        databaseHandler.onLoad();
        commandHandler.onLoad();
        listenerHandler.onLoad();
        vaultHook.onLoad();
        essentialsHook.onLoad();
        placeholderHandler.onLoad();
    }

    public void onEnable() {
        configHandler.onEnable();
        databaseHandler.onEnable();
        commandHandler.onEnable();
        listenerHandler.onEnable();

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Logger.get().warn(ColorParser.of("Vault is required for this plugin.").build());
            Bukkit.getPluginManager().disablePlugin(this);
        }
        vaultHook.onEnable();
        essentialsHook.onEnable();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Logger.get().warn(ColorParser.of("PlaceholderAPI is required for this plugin.").build());
            Bukkit.getPluginManager().disablePlugin(this);
        }
        placeholderHandler.onEnable();

        NameColorHolder.getInstance().loadAll();
        TagHolder.getInstance().loadAll();
    }

    public void onDisable() {
        configHandler.onDisable();
        databaseHandler.onDisable();
        commandHandler.onDisable();
        listenerHandler.onDisable();
        vaultHook.onDisable();
        essentialsHook.onDisable();
        placeholderHandler.onDisable();

        NameColorHolder.getInstance().cacheClear();
        TagHolder.getInstance().cacheClear();
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

    public static EssentialsHook getEssentialsHook() {
        return essentialsHook;
    }
}
