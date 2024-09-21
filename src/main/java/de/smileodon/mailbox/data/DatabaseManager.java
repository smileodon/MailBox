package de.smileodon.mailbox.data;

import com.fasterxml.jackson.databind.json.JsonMapper;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.queries.converter.ValueConverter;
import de.chojo.sadu.sqlite.databases.SqLite;
import de.chojo.sadu.sqlite.mapper.SqLiteMapper;
import de.chojo.sadu.updater.SqlUpdater;
import de.eldoria.jacksonbukkit.JacksonPaper;
import de.smileodon.mailbox.MailBoxPlayer;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static de.chojo.sadu.queries.api.call.Call.call;
import static de.chojo.sadu.queries.api.query.Query.query;

public enum DatabaseManager {
    INSTANCE;
    private final ValueConverter<InBoxInventory, String> IN_BOX_INVENTORY;


    DatabaseManager() {
        var objectMapper = JsonMapper.builder()
                                     .addModule(JacksonPaper.builder().build())
                                     .build();
        IN_BOX_INVENTORY = SqlValueConverter.create(objectMapper);
        connect();
    }

    public void connect() {

        DataSource dataSource = DataSourceCreator.create(SqLite.get())
                                                 .configure(config -> config.path("plugins/MailBox/database.db"))
                                                 .create()
                                                 .build();
        RowMapperRegistry registry = new RowMapperRegistry().register(MailBoxPlayer.create(IN_BOX_INVENTORY))
                                                            .register(SqLiteMapper.getDefaultMapper());

        QueryConfiguration.setDefault(QueryConfiguration.builder(dataSource)
                .setExceptionHandler(Throwable::printStackTrace)
                .setRowMapperRegistry(registry)
                .build());

        try {
            SqlUpdater.builder(dataSource, SqLite.get())
                    .withClassLoader(getClass().getClassLoader())
                    .execute();

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertMailBoxPlayer(MailBoxPlayer player) {
        String insertSQL = """
                    INSERT OR REPLACE INTO mailbox_player (uuid, current_name, last_mailbox_checked, last_mailbox_modified, mail_box_inventory)
                    VALUES (?, ?, ?, ?, ?);
                """;
        query(insertSQL)
                .single(call()
                        .bind(player.uuid())
                        .bind(player.currentName())
                        .bind(player.lastMailBoxChecked())
                        .bind(player.lastMailBoxModified())
                        .bind(player.mailBoxInventory(), IN_BOX_INVENTORY))
                .insert();
    }

    public MailBoxPlayer getMailBoxPlayer(String uuid) {
        return query("SELECT * FROM mailbox_player WHERE uuid = ?")
                .single(call().bind(uuid))
                .mapAs(MailBoxPlayer.class)
                .first()
                .orElse(null);
    }

    public List<MailBoxPlayer> getAllMailBoxPlayers() {
        return query("SELECT * FROM mailbox_player")
                .single()
                .mapAs(MailBoxPlayer.class)
                .all();
    }
}
