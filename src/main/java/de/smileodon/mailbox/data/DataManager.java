package de.smileodon.mailbox.data;

import de.smileodon.mailbox.MailBoxPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public enum DataManager {
    INSTANCE;

    private final HashMap<String, MailBoxPlayer> mailBoxPlayerMap = new HashMap<>();

    private final Component mailReceivedMessage = MiniMessage.miniMessage().deserialize("<blue>You received mail! Type <color:#f7ccff><color:#ff2172>/mailbox</color></color> to open your mailbox.</blue>");


    public MailBoxPlayer getMailBoxPlayerByUUID(String uuid){
        return mailBoxPlayerMap.get(uuid);
    }

    public MailBoxPlayer getMailBoxPlayer(Player player){
        return getMailBoxPlayerByUUID(player.getUniqueId().toString());
    }

    public MailBoxPlayer getMailBoxPlayerByName(String name){
        String uuid = getUUIDByName(name);
        if(uuid == null){
            return null;
        }
        return getMailBoxPlayerByUUID(uuid);
    }


    public String getUUIDByName(String name) {
        for (MailBoxPlayer mailBoxPlayer : mailBoxPlayerMap.values()) {
            if (mailBoxPlayer.currentName().equalsIgnoreCase(name)) {
                return mailBoxPlayer.uuid();
            }
        }
        return null;
    }

    public void setMailBoxPlayer(MailBoxPlayer mailBoxPlayer){
        mailBoxPlayerMap.put(mailBoxPlayer.uuid(), mailBoxPlayer);
    }

    public void setMultipleMailBoxPlayers(List<MailBoxPlayer> players){
        players.forEach(this::setMailBoxPlayer);
    }

    public void sendMail(OutBoxInventory outBoxInventory){
        if(outBoxInventory.isSendToAll()){
            mailBoxPlayerMap.keySet().forEach(uuid -> addItemsToInbox(outBoxInventory.getInventory(), uuid));
            mailBoxPlayerMap.keySet().stream()
                    .map(UUID::fromString) // Convert each String to UUID
                    .map(Bukkit::getPlayer) // Get the Player object from the UUID
                    .filter(player -> player != null && player.isOnline()) // Filter only online players
                    .forEach(player -> player.sendMessage(mailReceivedMessage)); // Send the message to each online player
        } else {
            String targetUUID = getUUIDByName(outBoxInventory.getTargetName());
            if(targetUUID != null){
                addItemsToInbox(outBoxInventory.getInventory(), targetUUID);
                UUID uuid = UUID.fromString(targetUUID); // Convert String to UUID
                Player player = Bukkit.getPlayer(uuid); // Get the Player object

                // Check if the player is online and send the message
                if (player != null && player.isOnline()) {
                    player.sendMessage(mailReceivedMessage);
                }
            }

        }
    }

    private void addItemsToInbox(Inventory inventory, String mailBoxPlayerUUID){
        MailBoxPlayer mailBoxPlayer = getMailBoxPlayerByUUID(mailBoxPlayerUUID);
        Inventory inboxInventory = mailBoxPlayer.mailBoxInventory().getInventory();

        // Loop through each slot in the source inventory
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                // Add the item to the inbox and check for leftovers
                HashMap<Integer, ItemStack> leftovers = inboxInventory.addItem(item);

                // Leftovers are ignored
            }
        }
        // Update modification time and save updated MailBoxPlayer to database
        setLastMailBoxModified(mailBoxPlayerUUID);    }

    public void updatePlayerName(String uuid, String newName){
        MailBoxPlayer player = mailBoxPlayerMap.get(uuid);
        MailBoxPlayer updatedPlayer = new MailBoxPlayer(
                player.uuid(),
                newName, // Updated name
                player.lastMailBoxChecked(),
                player.lastMailBoxModified(),
                player.mailBoxInventory()
        );
        mailBoxPlayerMap.put(uuid, updatedPlayer);
        DatabaseManager.INSTANCE.insertMailBoxPlayer(updatedPlayer);
    }

    public void setLastMailBoxChecked(String uuid){
        MailBoxPlayer player = mailBoxPlayerMap.get(uuid);
        MailBoxPlayer updatedPlayer = new MailBoxPlayer(
                player.uuid(),
                player.currentName(),
                System.currentTimeMillis(),
                player.lastMailBoxModified(),
                player.mailBoxInventory()
        );
        mailBoxPlayerMap.put(uuid, updatedPlayer);
        DatabaseManager.INSTANCE.insertMailBoxPlayer(updatedPlayer);
    }

    public void setLastMailBoxModified(String uuid){
        MailBoxPlayer player = mailBoxPlayerMap.get(uuid);
        MailBoxPlayer updatedPlayer = new MailBoxPlayer(
                player.uuid(),
                player.currentName(),
                player.lastMailBoxChecked(),
                System.currentTimeMillis(),
                player.mailBoxInventory()
        );
        mailBoxPlayerMap.put(uuid, updatedPlayer);
        DatabaseManager.INSTANCE.insertMailBoxPlayer(updatedPlayer);
    }

    public void updateMailboxContent(String uuid, Inventory inventory){
        mailBoxPlayerMap.get(uuid).mailBoxInventory().setInventory(inventory);
        setLastMailBoxChecked(uuid);
    }
}
