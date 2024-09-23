package de.smileodon.mailbox;

import de.smileodon.mailbox.commands.MailAllCommand;
import de.smileodon.mailbox.commands.MailBoxCommand;
import de.smileodon.mailbox.commands.MailCommand;
import de.smileodon.mailbox.data.DataManager;
import de.smileodon.mailbox.data.DatabaseManager;
import de.smileodon.mailbox.listener.InventoryClickListener;
import de.smileodon.mailbox.listener.InventoryCloseListener;
import de.smileodon.mailbox.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MailBoxPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        createPluginFolder();
        DatabaseManager databaseManager = DatabaseManager.INSTANCE;
        DataManager dataManager = DataManager.INSTANCE;
        dataManager.setMultipleMailBoxPlayers(databaseManager.getAllMailBoxPlayers());
        registerCommandsAndListener();
    }

    private void registerCommandsAndListener(){
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        this.getCommand("sendmail").setExecutor(new MailCommand());
        this.getCommand("sendmailall").setExecutor(new MailAllCommand());
        this.getCommand("mailbox").setExecutor(new MailBoxCommand());

    }

    private void createPluginFolder(){
        // Check and create plugin folder
        File pluginFolder = getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();  // Create the folder if it doesn't exist
            getLogger().info("Plugin folder created.");
        }
    }

}
