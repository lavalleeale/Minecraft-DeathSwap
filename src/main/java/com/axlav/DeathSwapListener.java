package com.axlav;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.io.File;
import java.util.Objects;

public class DeathSwapListener implements Listener {
    private final DeathSwap plugin;

    public DeathSwapListener(DeathSwap plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if ((event.getInventory() instanceof AnvilInventory) && event.getSlotType() != InventoryType.SlotType.RESULT) {
            if (Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName()
                    .contains("Invite from")) {
                event.setCancelled(true);
            }
        }
        if (event.getView().getTitle().contains("Invite from") && event.getCurrentItem() != null) {
            event.setCancelled(true);
            Player receiver = (Player) event.getWhoClicked();
            if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName().contains("Accept")) {
                Player sender = Bukkit.getPlayer(event.getView().getTitle().replaceAll("Invite from ", ""));
                World world = createWorld(Objects.requireNonNull(sender), receiver);
                int timerId = new Countdown(world, plugin).runTaskTimer(plugin, 1200L, 1200L).getTaskId();
                int swapId = new Swap(receiver,sender).runTaskTimer(plugin, 1400L, 1200L).getTaskId();
                world.setMetadata("timerId", new FixedMetadataValue(plugin, timerId));
                world.setMetadata("swapId", new FixedMetadataValue(plugin, swapId));
            } else {
                receiver.closeInventory();
            }
        }
    }

    private World createWorld(Player player1, Player player2) {
        WorldCreator wc = new WorldCreator(player1.getName() + "." + player2.getName());
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        World world = wc.createWorld();

        player1.teleport(Objects.requireNonNull(world).getSpawnLocation());
        player2.teleport(world.getSpawnLocation());
        player1.setBedSpawnLocation(Objects.requireNonNull(world).getSpawnLocation());
        player2.setBedSpawnLocation(Objects.requireNonNull(world).getSpawnLocation());
        return world;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player loser = event.getEntity();
        World world = loser.getWorld();
        Player winner;
        if (world.getPlayers().get(0) == loser) {
            winner = world.getPlayers().get(1);
        } else {
            winner = world.getPlayers().get(0);
        }
        loser.sendMessage(winner.getDisplayName() + " Wins!");
        winner.sendMessage(winner.getDisplayName() + " Wins!");
        loser.setBedSpawnLocation(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
        winner.setBedSpawnLocation(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
        winner.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
        for(MetadataValue meta : world.getMetadata("timerId")) {
            if(meta.getOwningPlugin() == plugin) {
                Bukkit.getScheduler().cancelTask(meta.asInt());
            }
        }
        for(MetadataValue meta : world.getMetadata("swapId")) {
            if(meta.getOwningPlugin() == plugin) {
                Bukkit.getScheduler().cancelTask(meta.asInt());
            }
        }
        Bukkit.unloadWorld(world, false);
        world.setAutoSave(false);
        deleteWorld(world.getWorldFolder());
    }
    private void deleteWorld(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();
            for (File file : Objects.requireNonNull(files)) {
                if (file.isDirectory()) {
                    deleteWorld(file);
                } else {
                    file.delete();
                }
            }
        }
        path.delete();
    }

}
