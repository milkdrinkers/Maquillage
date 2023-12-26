package io.github.Alathra.Maquillage.hooks;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.Reloadable;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook implements Reloadable {
    private final Maquillage instance;
    private RegisteredServiceProvider<Permission> rsp;

    public VaultHook(Maquillage instance) {
        this.instance = instance;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        if (!instance.getServer().getPluginManager().isPluginEnabled("Vault"))
            return;

        setVault(instance.getServer().getServicesManager().getRegistration(Permission.class));
    }

    @Override
    public void onDisable() {
        setVault(null);
    }

    public boolean isVaultLoaded() {
        return rsp != null;
    }

    /**
     * Gets vault. Should only be used following {@link #isVaultLoaded()}.
     *
     * @return vault instance
     */
    public Permission getVault() {
        return rsp.getProvider();
    }

    private void setVault(RegisteredServiceProvider<Permission> rsp) {
        this.rsp = rsp;
    }
}
