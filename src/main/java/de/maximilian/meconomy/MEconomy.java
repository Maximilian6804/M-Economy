package de.maximilian.meconomy;

import de.maximilian.mapi.EconomyAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/*
 * Hauptklasse des M-Economy Plugins.
 * Diese Klasse stellt die EconomyAPI bereit und verwaltet die Spielerkonten.
 */
public final class MEconomy extends JavaPlugin implements EconomyAPI {

    // Datei, in der alle Spieler-Guthaben gespeichert werden
    private File balancesFile;
    private FileConfiguration balancesConfig;

    /*
     * Wird vor onEnable() aufgerufen.
     * Registriert EconomyAPI als Bukkit-Service, damit andere Plugins darauf zugreifen können.
     */
    @Override
    public void onLoad() {
        // Registriere diese Klasse als EconomyAPI-Service
        getServer().getServicesManager().register(EconomyAPI.class, this, this, ServicePriority.Normal);
    }

    /*
     * Initialisierung beim Start des Plugins
     */
    @Override
    public void onEnable() {
        // Erstelle oder lade balances.yml
        createBalancesFile();

        // Registriere Befehle
        this.getCommand("balance").setExecutor(new balancecommand(this));
        this.getCommand("addcoins").setExecutor(new addcoinscommand(this));
        this.getCommand("removecoins").setExecutor(new removecoinscommand(this));
        this.getCommand("donate").setExecutor(new donatecommand(this));

        // Überprüfe, ob EconomyAPI korrekt registriert wurde
        ServicesManager sm = getServer().getServicesManager();
        RegisteredServiceProvider<EconomyAPI> rsp = sm.getRegistration(EconomyAPI.class);

        if (rsp != null && rsp.getProvider() != null) {
            getLogger().info("EconomyAPI ist registriert von " + rsp.getProvider().getClass().getName());
        } else {
            getLogger().warning("EconomyAPI Registrierung fehlgeschlagen!");
        }

        getLogger().info("MEconomy wurde aktiviert und EconomyAPI registriert.");
    }

    /*
     * Wird beim Stoppen des Plugins aufgerufen.
     * Speichert alle Kontostände.
     */
    @Override
    public void onDisable() {
        saveBalancesFile();
        getLogger().info("MEconomy wurde deaktiviert.");
    }

    /*
     * Erstellt die Datei balances.yml, wenn sie noch nicht existiert.
     * Lädt sie anschließend in die Config.
     */
    private void createBalancesFile() {
        balancesFile = new File(getDataFolder(), "balances.yml");

        if (!balancesFile.exists()) {
            balancesFile.getParentFile().mkdirs(); // Ordner erstellen, falls nötig
            try {
                balancesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Lade die Konfiguration aus der Datei
        balancesConfig = YamlConfiguration.loadConfiguration(balancesFile);
    }

    /*
     * Speichert die balances.yml Datei
     */
    public void saveBalancesFile() {
        try {
            balancesConfig.save(balancesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------- EconomyAPI Methoden -------------

    /*
     * Gibt das Guthaben eines Spielers zurück
     */
    @Override
    public int getBalance(UUID playerId) {
        return balancesConfig.getInt(playerId.toString(), 0);
    }

    /*
     * Fügt dem Konto eines Spielers Coins hinzu
     */
    @Override
    public void addCoins(UUID playerId, int amount) {
        int current = getBalance(playerId);
        setBalance(playerId, current + amount);
        saveBalancesFile();
    }

    /*
     * Zieht Coins vom Konto eines Spielers ab.
     * Der Kontostand kann dabei nicht negativ werden.
     */
    @Override
    public void removeCoins(UUID playerId, int amount) {
        int current = getBalance(playerId);
        int newBalance = Math.max(0, current - amount); // verhindert negative Werte
        setBalance(playerId, newBalance);
        saveBalancesFile();
    }

    /*
     * Setzt das Guthaben eines Spielers direkt (wird intern verwendet)
     */
    public void setBalance(UUID playerId, int amount) {
        balancesConfig.set(playerId.toString(), amount);
    }
}
