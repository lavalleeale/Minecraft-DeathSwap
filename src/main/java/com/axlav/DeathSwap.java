package com.axlav;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DeathSwap extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathSwapListener(this), this);
        Objects.requireNonNull(getCommand("swap")).setExecutor(new SwapCommand());
    }
}
