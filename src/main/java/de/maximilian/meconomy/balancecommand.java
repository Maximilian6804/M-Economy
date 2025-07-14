package de.maximilian.meconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/*
 * Command-Klasse für den /balance Befehl.
 * Zeigt dem Spieler seinen aktuellen Kontostand an.
 */
public class balancecommand implements CommandExecutor {

    // Referenz auf das Hauptplugin, um auf die API-Methoden zuzugreifen
    private final MEconomy plugin;

    // Konstruktor zum Setzen der Plugin-Instanz
    public balancecommand(MEconomy plugin) {
        this.plugin = plugin;
    }

    /*
     * Wird ausgeführt, wenn ein Spieler /balance aufruft.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Sicherstellen, dass nur Spieler diesen Befehl ausführen können
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        // Aktuellen Kontostand vom Spieler abrufen
        int balance = plugin.getBalance(playerId);

        // Kontostand dem Spieler anzeigen
        player.sendMessage("Dein Kontostand beträgt: " + balance + " Stackycoins.");

        return true;
    }
}
