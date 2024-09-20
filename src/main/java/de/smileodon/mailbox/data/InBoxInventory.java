package de.smileodon.mailbox.data;


import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InBoxInventory implements InventoryHolder {
        private final Inventory inventory;

        public InBoxInventory() {
            // Create an Inventory with 27 slots, `this` here is our InventoryHolder.
            this.inventory = Bukkit.createInventory(this, 27, "MailBox");
        }

        @Override
        public Inventory getInventory() {
            return this.inventory;
        }
}
