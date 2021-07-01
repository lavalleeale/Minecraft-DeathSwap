package com.axlav;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Swap extends BukkitRunnable {
    private final Player player1;
    private final Player player2;
    public Swap(Player player1, Player player2) {
        this.player1=player1;
        this.player2=player2;
    }
    @Override
    public void run() {
        Location location1 = player1.getLocation();
        player1.teleport(player2);
        player2.teleport(location1);
    }
}
