package de.smileodon.mailbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.eldoria.jacksonbukkit.JacksonPaper;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum DatabaseManager {
    INSTANCE;
    private Connection connection;
    private final ObjectMapper objectMapper;

    DatabaseManager() {
         objectMapper = JsonMapper.builder()
                .addModule(JacksonPaper.builder().build())
                .build();
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/MailBox/database.db");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setup() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS mailbox_player (
                uuid TEXT PRIMARY KEY,
                current_name TEXT NOT NULL,
                last_mailbox_checked INTEGER NOT NULL,
                last_mailbox_modified INTEGER NOT NULL,
                mail_box_inventory TEXT
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertMailBoxPlayer(MailBoxPlayer player) {
        String insertSQL = """
            INSERT OR REPLACE INTO mailbox_player (uuid, current_name, last_mailbox_checked, last_mailbox_modified, mail_box_inventory)
            VALUES (?, ?, ?, ?, ?);
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, player.uuid());
            pstmt.setString(2, player.currentName());
            pstmt.setLong(3, player.lastMailBoxChecked());
            pstmt.setLong(4, player.lastMailBoxModified());
            pstmt.setString(5, serializeInventory(player.mailBoxInventory()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MailBoxPlayer getMailBoxPlayer(String uuid) {
        String selectSQL = "SELECT * FROM mailbox_player WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, uuid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String currentName = rs.getString("current_name");
                long lastMailBoxChecked = rs.getLong("last_mailbox_checked");
                long lastMailBoxModified = rs.getLong("last_mailbox_modified");
                Inventory inventory = deserializeInventory(rs.getString("mail_box_inventory"));

                return new MailBoxPlayer(uuid, currentName, lastMailBoxChecked, lastMailBoxModified, inventory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MailBoxPlayer> getAllMailBoxPlayers() {
        List<MailBoxPlayer> players = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM mailbox_player";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSQL)) {

            while (rs.next()) {
                String uuid = rs.getString("uuid");
                String currentName = rs.getString("current_name");
                long lastMailBoxChecked = rs.getLong("last_mailbox_checked");
                long lastMailBoxModified = rs.getLong("last_mailbox_modified");
                Inventory inventory = deserializeInventory(rs.getString("mail_box_inventory"));

                MailBoxPlayer player = new MailBoxPlayer(uuid, currentName, lastMailBoxChecked, lastMailBoxModified, inventory);
                players.add(player);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }

    private String serializeInventory(Inventory inventory) {
        try {
            ItemStack[] items = inventory.getContents();
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Inventory deserializeInventory(String data) {
        try {
            ItemStack[] items = objectMapper.readValue(data, ItemStack[].class);
            Inventory inventory = createEmptyInventory();
            inventory.setContents(items);
            return inventory;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Inventory createEmptyInventory() {
        return Bukkit.createInventory(null, 27, "Mailbox");
    }
}