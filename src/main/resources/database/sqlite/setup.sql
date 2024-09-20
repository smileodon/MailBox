            CREATE TABLE IF NOT EXISTS mailbox_player (
                uuid TEXT PRIMARY KEY,
                current_name TEXT NOT NULL,
                last_mailbox_checked INTEGER NOT NULL,
                last_mailbox_modified INTEGER NOT NULL,
                mail_box_inventory TEXT
            );
