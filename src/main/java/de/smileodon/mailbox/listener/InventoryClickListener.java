package de.smileodon.mailbox.listener;

import de.smileodon.mailbox.data.DataManager;
import de.smileodon.mailbox.data.InBoxInventory;
import de.smileodon.mailbox.data.OutBoxInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryClickListener implements Listener {
    private final Component mailSent = MiniMessage.miniMessage().deserialize("<blue>Mail sent!</blue>");
    private final Component notEnoughSpaceMessage = MiniMessage.miniMessage().deserialize("<red>Not enough space for all items in your inventory. Some remain behind.</red>");
    private final Component allItemsTransferredMessage = MiniMessage.miniMessage().deserialize("<green>All items successfully transferred to your inventory!</green>");

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory.getType() == InventoryType.CHEST) {
            InventoryHolder holder = inventory.getHolder();
            Player player = (Player) event.getWhoClicked();


            // Check if the holder is OutBoxInventory
            if (holder instanceof OutBoxInventory) {
                int clickedSlot = event.getSlot();
                if (clickedSlot < 10 || clickedSlot > 16) {
                    event.setCancelled(true);
                    if (clickedSlot == 26) {
                        int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
                        for (int slot : borderSlots) {
                            inventory.setItem(slot, null);
                        }
                        DataManager.INSTANCE.sendMail((OutBoxInventory) holder);
                        player.closeInventory();
                        player.sendMessage(mailSent);
                    } else if (clickedSlot == 18) {
                        player.closeInventory();
                    }
                }
            } else if (holder instanceof InBoxInventory) {
                int clickedSlot = event.getSlot();
                if (clickedSlot < 27) {
                    event.setCancelled(true);
                    if (clickedSlot == 22) {
                        Inventory playerInventory = player.getInventory();

                        for (int i = 10; i <= 16; i++) {
                            ItemStack item = inventory.getItem(i);

                            if (item != null) {
                                HashMap<Integer, ItemStack> leftover = playerInventory.addItem(item);
                                if (!leftover.isEmpty()) {
                                    inventory.setItem(i, leftover.get(0));
                                    player.sendMessage(notEnoughSpaceMessage);
                                } else {
                                    inventory.clear(i);
                                }
                            }
                        }
                        player.sendMessage(allItemsTransferredMessage);
                        player.closeInventory();
                    } else if (clickedSlot == 18) {
                        player.closeInventory();
                    }
                }
            }
        }
    }
}

