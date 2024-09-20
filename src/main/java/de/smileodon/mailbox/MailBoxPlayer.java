package de.smileodon.mailbox;

import org.bukkit.inventory.Inventory;

public record MailBoxPlayer(
        String uuid,
        String currentName,
        long lastMailBoxChecked,
        long lastMailBoxModified,
        Inventory mailBoxInventory) {
}
