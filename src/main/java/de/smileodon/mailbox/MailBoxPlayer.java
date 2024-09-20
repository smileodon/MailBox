package de.smileodon.mailbox;

import de.smileodon.mailbox.data.InBoxInventory;
import org.bukkit.inventory.Inventory;

public record MailBoxPlayer(
        String uuid,
        String currentName,
        long lastMailBoxChecked,
        long lastMailBoxModified,
        InBoxInventory mailBoxInventory) {
}
