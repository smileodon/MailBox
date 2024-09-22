package de.smileodon.mailbox.listener;

import de.smileodon.mailbox.data.DataManager;
import de.smileodon.mailbox.data.InBoxInventory;
import de.smileodon.mailbox.data.OutBoxInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClickListener implements Listener {
    private final Component mailSent = MiniMessage.miniMessage().deserialize("<blue>Mail sent!</blue>");

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        Player player = (Player) event.getWhoClicked();

        // Check if the holder is InBoxInventory
        if (holder instanceof InBoxInventory) {
            // Prevent placing items but allow taking items out (so the mailbox oes not become a backpack)
            if (event.getAction().name().contains("PLACE")) {
                event.setCancelled(true);  // Cancel placing items
            }
        }

        // Check if the holder is OutBoxInventory
        else if (holder instanceof OutBoxInventory) {
            // Prevent players from moving or removing the item at position 8
            if (event.getSlot() == 8) {
                if (event.getClick() == ClickType.LEFT) {
                    // Send mail
                    inventory.setItem(8, null);
                    DataManager.INSTANCE.sendMail((OutBoxInventory) holder);
                    player.closeInventory();
                    player.sendMessage(mailSent);
                }

                // Allow taking all items with a shortcut (e.g., shift-double-click)
                if (!(event.isShiftClick() && event.getAction().name().contains("MOVE_TO_OTHER_INVENTORY"))) {
                    event.setCancelled(true);  // Prevent moving or removing the item otherwise
                }
            }
        }
    }
}

