package de.smileodon.mailbox.listener;

import de.smileodon.mailbox.MailBoxPlayer;
import de.smileodon.mailbox.data.DataManager;
import de.smileodon.mailbox.data.DatabaseManager;
import de.smileodon.mailbox.data.InBoxInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        // Check if the player is already in the HashMap
        MailBoxPlayer mailBoxPlayer = DataManager.INSTANCE.getMailBoxPlayerByUUID(uuid);

        if (mailBoxPlayer == null) {
            // Instantiate the new MailBoxPlayer object
            mailBoxPlayer = new MailBoxPlayer(uuid, player.getName(), System.currentTimeMillis(), System.currentTimeMillis(), new InBoxInventory());

            // Add the MailBoxPlayer to the HashMap
            DataManager.INSTANCE.setMailBoxPlayer(mailBoxPlayer);

            // Insert the MailBoxPlayer into the database
            DatabaseManager.INSTANCE.insertMailBoxPlayer(mailBoxPlayer);
        } else {
            if(mailBoxPlayer.lastMailBoxModified() >= mailBoxPlayer.lastMailBoxChecked()){
                final Component component = MiniMessage.miniMessage().deserialize("<blue>You have unopened mail! Type <color:#f7ccff><color:#ff2172>/mailbox</color></color> to open your mailbox.</blue>");
                player.sendMessage(component);
            }
            if(!mailBoxPlayer.currentName().equalsIgnoreCase(player.getName())){
                DataManager.INSTANCE.updatePlayerName(player.getUniqueId().toString(), player.getName());
            }
        }
    }
}
