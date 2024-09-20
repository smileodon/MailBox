package de.smileodon.mailbox.listener;

import de.smileodon.mailbox.MailBoxPlayer;
import de.smileodon.mailbox.data.DataManager;
import de.smileodon.mailbox.data.DatabaseManager;
import net.minecraft.world.entity.player.Inventory;
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
            // Create a new MailBoxPlayer object
            Inventory inventory =  Bukkit.createInventory(null, 27, "Mailbox");

            // Create a stone item with the name "MailBox"
            ItemStack stone = new ItemStack(Material.STONE, 1);
            ItemMeta meta = stone.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("MailBox");
                stone.setItemMeta(meta);
            }
            inventory.addItem(stone); // Add the stone item to the inventory

            // Instantiate the new MailBoxPlayer object
            mailBoxPlayer = new MailBoxPlayer(uuid, player.getName(), System.currentTimeMillis(), System.currentTimeMillis(), inventory);

            // Add the MailBoxPlayer to the HashMap
            DataManager.INSTANCE.setMailBoxPlayer(mailBoxPlayer);

            // Insert the MailBoxPlayer into the database
            DatabaseManager.INSTANCE.insertMailBoxPlayer(mailBoxPlayer);
        }
    }
}
