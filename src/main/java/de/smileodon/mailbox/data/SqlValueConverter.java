package de.smileodon.mailbox.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.chojo.sadu.mapper.reader.ValueReader;
import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.queries.api.call.adapter.Adapter;
import de.chojo.sadu.queries.converter.ValueConverter;
import org.bukkit.inventory.ItemStack;

import java.sql.Types;

public class SqlValueConverter {

    public static ValueConverter<InBoxInventory, String> create(ObjectMapper mapper) {
        return ValueConverter.create(
                Adapter.create(InBoxInventory.class, (stmt, index, value) -> {
                    ItemStack[] items = value.getInventory().getContents();
                    try {
                        stmt.setString(index, mapper.writeValueAsString(items));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }, Types.VARCHAR),
                ValueReader.create(data -> {
                    try {
                        ItemStack[] items = mapper.readValue(data, ItemStack[].class);
                        InBoxInventory inBoxInventory = new InBoxInventory();
                        inBoxInventory.getInventory().setContents(items);
                        return inBoxInventory;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }, Row::getString, Row::getString));
    }
}
