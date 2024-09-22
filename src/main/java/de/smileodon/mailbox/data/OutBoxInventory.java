package de.smileodon.mailbox.data;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OutBoxInventory implements InventoryHolder {
        private Inventory inventory;
        private String target;
        private boolean sendToAll;

        public OutBoxInventory(boolean sendToAll, String target){
            this.sendToAll = sendToAll;
            this.target = target;
            init();
        }
        public OutBoxInventory(boolean sendToAll) {
            this.sendToAll = sendToAll;
            init();
        }

        private void init(){
            // Create an Inventory with 9 slots, `this` here is our InventoryHolder.
            this.inventory = Bukkit.createInventory(this, 9);
            ItemStack greenWool = new ItemStack(Material.GREEN_WOOL);
            ItemMeta woolMeta = greenWool.getItemMeta();

            if (woolMeta != null) {
                woolMeta.setDisplayName("Send");
                greenWool.setItemMeta(woolMeta);
            }
            this.inventory.setItem(8, greenWool);
        }

        @Override
        public Inventory getInventory() {
            return this.inventory;
        }

    public boolean isSendToAll() {
        return sendToAll;
    }

    public String getTargetName() {
        return target;
    }
}
