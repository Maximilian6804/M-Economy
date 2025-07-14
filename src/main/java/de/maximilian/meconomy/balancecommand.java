package de.maximilian.meconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class balancecommand implements CommandExecutor {

    private final MEconomy plugin;

    public balancecommand(MEconomy plugin) {
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

        int balance = plugin.getBalance(playerId);
        player.sendMessage("Dein Kontostand beträgt: " + balance + " Stackycoins.");

        return true;
    }
}
