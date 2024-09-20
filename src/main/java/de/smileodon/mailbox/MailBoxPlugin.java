package de.smileodon.mailbox;

import de.smileodon.mailbox.data.DatabaseManager;
import de.smileodon.mailbox.listener.InventoryClickListener;
import de.smileodon.mailbox.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MailBoxPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        DatabaseManager.INSTANCE.connect();
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);


    }


}
