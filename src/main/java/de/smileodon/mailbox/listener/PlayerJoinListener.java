package de.smileodon.mailbox.listener;

import de.smileodon.mailbox.MailBoxPlayer;
import de.smileodon.mailbox.data.DataManager;
import de.smileodon.mailbox.data.DatabaseManager;
import de.smileodon.mailbox.data.InBoxInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        // Check if the player is already in the HashMap
        MailBoxPlayer mailBoxPlayer = DataManager.INSTANCE.getMailBoxPlayer(uuid);

        if (mailBoxPlayer == null) {
            // Instantiate the new MailBoxPlayer object
            mailBoxPlayer = new MailBoxPlayer(uuid, player.getName(), System.currentTimeMillis(), System.currentTimeMillis(), new InBoxInventory());

            // Add the MailBoxPlayer to the HashMap
            DataManager.INSTANCE.setMailBoxPlayer(mailBoxPlayer);

            // Insert the MailBoxPlayer into the database
            DatabaseManager.INSTANCE.insertMailBoxPlayer(mailBoxPlayer);
        }
    }
}
