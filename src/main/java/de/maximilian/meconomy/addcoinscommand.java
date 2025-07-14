package de.maximilian.meconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class addcoinscommand implements CommandExecutor {

    private final MEconomy plugin;

    public addcoinscommand(MEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        if (args.length != 1) {
            player.sendMessage("Bitte gib einen Betrag an. Beispiel: /addcoins 100");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Das ist keine gültige Zahl.");
            return true;
        }

        if (amount <= 0) {
            player.sendMessage("Bitte gib eine positive Zahl an.");
            return true;
        }

        int currentBalance = plugin.getBalance(playerId);
        int newBalance = currentBalance + amount;
        plugin.setBalance(playerId, newBalance);
        plugin.saveBalancesFile(); // speichern nicht vergessen

        player.sendMessage("Dir wurden " + amount + " Stackycoins gutgeschrieben. Neuer Kontostand: " + newBalance + " Stackycoins.");

        return true;
    }
}
