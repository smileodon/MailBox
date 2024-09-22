package de.smileodon.mailbox.listener;

import de.smileodon.mailbox.data.DataManager;
import de.smileodon.mailbox.data.InBoxInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof InBoxInventory) {
            String uuid = event.getPlayer().getUniqueId().toString();
            DataManager.INSTANCE.updateMailboxContent(uuid, inventory);
        }
    }
}

