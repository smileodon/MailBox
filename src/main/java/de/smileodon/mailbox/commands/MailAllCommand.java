package de.smileodon.mailbox.commands;

import de.smileodon.mailbox.data.OutBoxInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MailAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("mailbox.send.all")) {
                if (args.length == 0) {
                    OutBoxInventory outBoxInventory =new OutBoxInventory(true);
                    player.openInventory(outBoxInventory.getInventory());
                } else {
                    player.sendMessage("Please do not specify any arguments.");
                }
                return true;
            } else {
                player.sendMessage("You don't have permission to use this command.");
                return true;
            }
        } else {
            System.out.println("Command can not be executed from the console.");
            return true;
        }
    }
}
