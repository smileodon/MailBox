package de.smileodon.mailbox;

import de.smileodon.mailbox.data.DatabaseManager;
import de.smileodon.mailbox.listener.InventoryClickListener;
import de.smileodon.mailbox.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MailBoxPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Check and create plugin folder
        File pluginFolder = getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();  // Create the folder if it doesn't exist
            getLogger().info("Plugin folder created.");
        }
        DatabaseManager instance = DatabaseManager.INSTANCE;
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);


    }


}
