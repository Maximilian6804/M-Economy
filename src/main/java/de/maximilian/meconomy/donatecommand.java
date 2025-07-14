package de.maximilian.meconomy;

import de.maximilian.mapi.EconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Command-Klasse für den /donate-Befehl.
 * Ermöglicht Spielern, Stackycoins an andere Spieler zu senden.
 */
public class donatecommand implements CommandExecutor {

    // Referenz auf das MEconomy-Plugin, um auf Economy-Methoden zuzugreifen
    private final MEconomy plugin;

    // Konstruktor – speichert Plugin-Referenz
    public donatecommand(MEconomy plugin) {
        this.plugin = plugin;
    }

    /*
     * Wird aufgerufen, wenn ein Spieler /donate ausführt.
     * Syntax: /donate <Spieler> <Betrag>
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Stelle sicher, dass nur Spieler den Befehl benutzen können (kein Server oder Konsole)
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl benutzen.");
            return true;
        }

        // Überprüfe, ob genau 2 Argumente angegeben wurden
        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Benutzung: /donate <Spieler> <Betrag>");
            return true;
        }

        // Versuche, den Zielspieler anhand des Namens zu finden
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Spieler nicht gefunden.");
            return true;
        }

        // Spieler darf sich selbst kein Geld senden
        if (target == player) {
            player.sendMessage(ChatColor.RED + "Du kannst dir selbst kein Geld senden.");
            return true;
        }

        int amount;
        try {
            // Versuche, den Betrag in eine Zahl umzuwandeln
            amount = Integer.parseInt(args[1]);
            if (amount <= 0) throw new NumberFormatException(); // Kein negativer oder 0-Betrag
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Bitte gib eine gültige Zahl größer als 0 ein.");
            return true;
        }

        // Zugriff auf die EconomyAPI
        EconomyAPI economy = plugin;

        // Prüfe, ob der Sender genug Geld hat
        int senderBalance = economy.getBalance(player.getUniqueId());
        if (senderBalance < amount) {
            player.sendMessage(ChatColor.RED + "Du hast nicht genug Stackycoins.");
            return true;
        }

        // Transaktion durchführen: abziehen und beim Ziel hinzufügen
        economy.removeCoins(player.getUniqueId(), amount);
        economy.addCoins(target.getUniqueId(), amount);

        // Feedback an beide Spieler
        player.sendMessage(ChatColor.GREEN + "Du hast " + amount + " Stackycoins an " + target.getName() + " gesendet.");
        target.sendMessage(ChatColor.GOLD + "Du hast " + amount + " Stackycoins von " + player.getName() + " erhalten.");

        return true;
    }
}
