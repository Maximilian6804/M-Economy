package de.maximilian.meconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/*
 * Command-Klasse für den /removecoins-Befehl.
 * Entfernt Stackycoins vom eigenen Konto.
 */
public class removecoinscommand implements CommandExecutor {

    // Referenz auf das Hauptplugin, um Zugriff auf Methoden wie getBalance zu haben
    private final MEconomy plugin;

    // Konstruktor zum Übergeben der Plugin-Instanz
    public removecoinscommand(MEconomy plugin) {
        this.plugin = plugin;
    }

    /*
     * Wird aufgerufen, wenn ein Spieler /removecoins <Betrag> ausführt.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Nur Spieler dürfen diesen Befehl ausführen (Konsole blockieren)
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        // Prüfe, ob genau ein Argument (der Betrag) angegeben wurde
        if (args.length != 1) {
            player.sendMessage("Bitte gib einen Betrag an. Beispiel: /removecoins 100");
            return true;
        }

        int amount;
        try {
            // Versuche, das Argument in eine Ganzzahl zu parsen
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Das ist keine gültige Zahl.");
            return true;
        }

        // Stelle sicher, dass der Betrag positiv ist
        if (amount <= 0) {
            player.sendMessage("Bitte gib eine positive Zahl an.");
            return true;
        }

        // Aktuelles Guthaben abfragen
        int currentBalance = plugin.getBalance(playerId);

        // Prüfe, ob der Spieler genug Stackycoins hat
        if (currentBalance < amount) {
            player.sendMessage("Du hast nicht genug Stackycoins, um diesen Betrag zu entfernen.");
            return true;
        }

        // Guthaben aktualisieren und speichern
        int newBalance = currentBalance - amount;
        plugin.setBalance(playerId, newBalance);
        plugin.saveBalancesFile();

        // Rückmeldung an den Spieler
        player.sendMessage(amount + " Stackycoins wurden von deinem Konto entfernt. Neuer Kontostand: " + newBalance + " Stackycoins.");

        return true;
    }
}
