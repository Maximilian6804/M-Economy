package de.maximilian.meconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Command-Klasse für den /addcoins Befehl.
 * Fügt dem Spieler, der den Befehl ausführt, eine bestimmte Anzahl an Stackycoins hinzu.
 */
public class addcoinscommand implements CommandExecutor {

    // Referenz auf das Hauptplugin (MEconomy), um auf die API zuzugreifen
    private final MEconomy plugin;

    // Konstruktor zur Übergabe der Plugin-Instanz
    public addcoinscommand(MEconomy plugin) {
        this.plugin = plugin;
    }

    /**
     * Wird ausgeführt, wenn ein Spieler /addcoins <Betrag> eingibt.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Nur Spieler dürfen diesen Befehl ausführen
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        // Überprüfen, ob genau ein Argument (der Betrag) übergeben wurde
        if (args.length != 1) {
            player.sendMessage("Bitte gib einen Betrag an. Beispiel: /addcoins 100");
            return true;
        }

        int amount;
        try {
            // Betrag in eine Zahl umwandeln
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Das ist keine gültige Zahl.");
            return true;
        }

        // Betrag muss positiv sein
        if (amount <= 0) {
            player.sendMessage("Bitte gib eine positive Zahl an.");
            return true;
        }

        // Aktuelles Guthaben ermitteln und neuen Kontostand berechnen
        int currentBalance = plugin.getBalance(playerId);
        int newBalance = currentBalance + amount;

        // Guthaben aktualisieren und speichern
        plugin.setBalance(playerId, newBalance);
        plugin.saveBalancesFile(); // Änderungen speichern

        // Spieler benachrichtigen
        player.sendMessage("Dir wurden " + amount + " Stackycoins gutgeschrieben. Neuer Kontostand: " + newBalance + " Stackycoins.");

        return true;
    }
}
