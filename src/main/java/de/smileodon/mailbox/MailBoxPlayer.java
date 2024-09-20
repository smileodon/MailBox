package de.smileodon.mailbox;

import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.queries.converter.ValueConverter;
import de.smileodon.mailbox.data.InBoxInventory;

public record MailBoxPlayer(
        String uuid,
        String currentName,
        long lastMailBoxChecked,
        long lastMailBoxModified,
        InBoxInventory mailBoxInventory) {

    public static RowMapper<MailBoxPlayer> create(ValueConverter<InBoxInventory, String> converter) {
        return RowMapper.forClass(MailBoxPlayer.class)
                        .mapper(row -> new MailBoxPlayer(
                                row.getString("uuid"),
                                row.getString("current_name"),
                                row.getLong("last_mailbox_checked"), row.getLong("last_mailbox_modified"),
                                row.get("mail_box_inventory", converter)))
                        .addColumns("uuid", "current_name", "last_mailbox_checked", "last_mailbox_modified", "mail_box_inventory")
                        .build();
    }
}
