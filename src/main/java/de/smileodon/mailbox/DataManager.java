package de.smileodon.mailbox;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public enum DataManager {
    INSTANCE;

    private final HashMap<String, MailBoxPlayer> mailBoxPlayerMap = new HashMap<>();

    public MailBoxPlayer getMailBoxPlayer(String uuid){
        return mailBoxPlayerMap.get(uuid);
    }

    public MailBoxPlayer getMailBoxPlayer(Player player){
        return getMailBoxPlayer(player.getUniqueId().toString());
    }

    public void setMailBoxPlayer(MailBoxPlayer mailBoxPlayer){
        mailBoxPlayerMap.put(mailBoxPlayer.uuid(), mailBoxPlayer);
    }

    public void setMultipleMailBoxPlayers(List<MailBoxPlayer> players){
        players.forEach(this::setMailBoxPlayer);
    }
}
