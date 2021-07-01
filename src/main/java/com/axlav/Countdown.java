package com.axlav;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    int timeLeft;
    private final World world;
    private final DeathSwap plugin;
    public Countdown(World world, DeathSwap plugin) {
        this.world = world;
        this.plugin = plugin;
    }
    @Override
    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                timeLeft = 10;
                world.getPlayers().forEach(player -> player.sendMessage("Swapping in " + timeLeft + " seconds!"));
                timeLeft--;
                if (timeLeft==0) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin,0L,20L);
    }
}
