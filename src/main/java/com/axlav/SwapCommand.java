package com.axlav;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class SwapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("This command may only be run by a player");
            return false;
        }
        if (args.length != 1) {
            return false;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(args[0] + "does not exist");
            return false;
        }
        Inventory invite = Bukkit.createInventory(null, 9, "Invite from " + sender.getName());
        ItemStack accept = new ItemStack(Material.GREEN_WOOL, 1);
        ItemMeta acceptMeta = accept.getItemMeta();
        Objects.requireNonNull(acceptMeta).setDisplayName("Accept invite from " + sender.getName());
        accept.setItemMeta(acceptMeta);
        ItemStack deny = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta denyMeta = deny.getItemMeta();
        Objects.requireNonNull(denyMeta).setDisplayName("Deny invite from " + sender.getName());
        deny.setItemMeta(denyMeta);
        invite.setItem(0, accept);
        invite.setItem(8, deny);
        targetPlayer.openInventory(invite);
        return true;
    }
}
