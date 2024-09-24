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
            this.inventory = Bukkit.createInventory(this, 27, "Send Mail");
            populateInventory();
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
        greenMeta.setDisplayName("Confirm");
        greenGlassPane.setItemMeta(greenMeta);

        ItemStack redGlassPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta redMeta = greenGlassPane.getItemMeta();
        redMeta.setDisplayName("Cancel");
        redGlassPane.setItemMeta(redMeta);

        int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        for (int slot : borderSlots) {
            inventory.setItem(slot, blackGlassPane);
        }

        inventory.setItem(26, greenGlassPane);
        inventory.setItem(18, redGlassPane);

    }

    public boolean isSendToAll() {
        return sendToAll;
    }

    public String getTargetName() {
        return target;
    }
}
