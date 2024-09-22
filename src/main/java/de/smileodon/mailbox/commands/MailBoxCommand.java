package de.smileodon.mailbox.commands;

import de.smileodon.mailbox.data.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MailBoxCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("mailbox.open")) {
                player.openInventory(DataManager.INSTANCE.getMailBoxPlayer(player).mailBoxInventory().getInventory());
            } else {
                player.sendMessage("You don't have permission to use this command.");
            }
            return true;
        } else {
            System.out.println("Command can not be executed from the console.");
            return true;
        }
    }
}
