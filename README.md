# MailBox
**Paper 1.20.4** |  **JAVA 17 +** 
### Feature Overview

#### 1. **Send mail**
- **/mailall**: Allows admins to send items to all players, both online and offline. It uses a GUI interface where multiple items can be added for distribution to all players with ease.
- **/mail [playername]**: Enables admins to send items to a specific player, even if they are offline. The player will receive the items in their personal mailbox, accessible through a user-friendly GUI system.

#### 2. **Player Mailboxes**
Each player has a personal mailbox to store items sent by admins. Players can access their mailbox at any timeusing the /mailbox command. The system supports both online and offline players, ensuring they receive their items as soon as they log in.

#### 3. **Notification System**
Whenever players receive new mail, they will be notified via chat. For offline players, a notification will be displayed when they log in, so they can retrieve their mail from the mailbox.

#### 4. **Mailbox Storage**
Mailboxes are stored securely in a local SQLite database, ensuring efficient and reliable storage of player items.

---

### Command Permissions and Usage

| Command                | Permission           | Description                                      |
|------------------------|----------------------|--------------------------------------------------|
| `/mail [player]`        | `mailbox.send`        | Send mail to a specific player.                  |
| `/mailall`              | `mailbox.send.all`    | Send mail to all players (online and offline).   |
| `/mailbox`              | `mailbox.open`        | Open your personal mailbox to retrieve items.    |


## Technical information
### Database
This plugin uses [sadu](https://github.com/rainbowdashlabs/sadu/) for database communication.

### Build
Built with [paperweight-userdev](https://docs.papermc.io/paper/dev/userdev) for Paper 1.20.x. (sourceCompatibility Java 17)
