package de.smileodon.mailbox.commands;

import de.smileodon.mailbox.data.DataManager;
import de.smileodon.mailbox.data.OutBoxInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MailCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("mailbox.send")) {
                if (args.length == 1) {
                    String targetPlayerName = args[0];
                    if(DataManager.INSTANCE.getMailBoxPlayerByName(targetPlayerName)!=null){
                        OutBoxInventory outBoxInventory =new OutBoxInventory(false, targetPlayerName);
                        player.openInventory(outBoxInventory.getInventory());

                    } else {
                        player.sendMessage("Unknown player.");
                    }
                } else {
                    player.sendMessage("Please specify a player name.");
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
