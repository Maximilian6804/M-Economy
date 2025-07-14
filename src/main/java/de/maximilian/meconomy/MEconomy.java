package de.maximilian.meconomy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public final class MEconomy extends JavaPlugin {

    private File balancesFile;
    private FileConfiguration balancesConfig;

    @Override
    public void onEnable() {
        createBalancesFile();
        // Command-Executors registrieren
        this.getCommand("balance").setExecutor(new balancecommand(this));
        this.getCommand("addcoins").setExecutor(new addcoinscommand(this));

        getLogger().info("MEconomy wurde aktiviert.");
    }

    @Override
    public void onDisable() {
        saveBalancesFile();
        getLogger().info("MEconomy wurde deaktiviert.");
    }

    private void createBalancesFile() {
        balancesFile = new File(getDataFolder(), "balances.yml");
        if (!balancesFile.exists()) {
            balancesFile.getParentFile().mkdirs();
            try {
                balancesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        balancesConfig = YamlConfiguration.loadConfiguration(balancesFile);
    }

    public void saveBalancesFile() {
        try {
            balancesConfig.save(balancesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Für die Commands: Kontostand als int auslesen
    public int getBalance(UUID playerId) {
        return balancesConfig.getInt(playerId.toString(), 0);
    }

    // Für die Commands: Kontostand setzen
    public void setBalance(UUID playerId, int amount) {
        balancesConfig.set(playerId.toString(), amount);
    }
}
