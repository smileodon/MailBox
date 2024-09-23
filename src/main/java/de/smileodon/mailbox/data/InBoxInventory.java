package de.smileodon.mailbox.data;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InBoxInventory implements InventoryHolder {
        private Inventory inventory;

        public InBoxInventory() {
            // Create an Inventory with 27 slots, `this` here is our InventoryHolder.
            this.inventory = Bukkit.createInventory(this, 27, "MailBox");
            populateInventory();
        }

        public void setInventory(Inventory inventory){
            this.inventory = inventory;
    }

        @Override
        public Inventory getInventory() {
            return this.inventory;
        }

        private void populateInventory(){
            ItemStack blackGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta blackGlassPaneMeta = blackGlassPane.getItemMeta();
            blackGlassPaneMeta.setDisplayName(" ");
            blackGlassPane.setItemMeta(blackGlassPaneMeta);

            ItemStack greenGlassPane = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta greenMeta = greenGlassPane.getItemMeta();
            greenMeta.setDisplayName("Claim Mail");
            greenGlassPane.setItemMeta(greenMeta);

            int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
            for (int slot : borderSlots) {
                inventory.setItem(slot, blackGlassPane);
            }

            inventory.setItem(22, greenGlassPane);
        }
}
