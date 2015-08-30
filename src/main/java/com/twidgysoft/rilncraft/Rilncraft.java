package com.twidgysoft.rilncraft;

import com.twidgysoft.rilncraft.worlds.RilncraftWorldGenerator;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Jonny
 */
public final class Rilncraft extends JavaPlugin implements Listener {
    
    HashMap<Player,Rilncrafter> playerList;
    File playerDir;
    private FileConfiguration config;
    private ModuleLoader modLoader;
    
    @Override
    public void onEnable()
    {
        // Load the configs
        File configFile = new File(getDataFolder(), "config.yml");
        if( !configFile.canRead() )
        {
            saveDefaultConfig();
        }
        else
        {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        config = getConfig();
        
        playerList = new HashMap<>();
        
        playerDir = new File(getDataFolder(), "players/");
        if( !playerDir.isDirectory() )
        {
            if( playerDir.mkdir() )
            {
                getLogger().log(Level.CONFIG, "RilnCraft: Player directory created");
            }
            else
            {
                getLogger().log(Level.SEVERE, "RilnCraft: Could not create the player directory!");
            }
        }
        loadOnlinePlayers();
        
        this.modLoader = new ModuleLoader();
        loadModules();
        
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new GainSkillListener(this), this);
        getCommand("riln").setExecutor(new RilnCommandExecutor(this));
        
    }
    
    @Override
    public void onDisable()
    {
        for( Rilncrafter rilncrafter : playerList.values() )
        {
            savePlayerData( rilncrafter );
        }
        playerList.clear();
        
        unloadModules();
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Rilncrafter rilncrafter = loadRilncrafter( player );
        playerList.put(player, rilncrafter);
        savePlayerData( rilncrafter );
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        Rilncrafter rilncrafter = playerList.get( player );
        if( rilncrafter != null )
        {
            savePlayerData( rilncrafter );
            playerList.remove( player );
        }
    }
    
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String GenId)
    {
        return new RilncraftWorldGenerator();
    }
    
    public Rilncrafter getRilncrafter(Player player)
    {
        return playerList.get( player );
    }

    private void loadOnlinePlayers() {
        for( Player player : Bukkit.getServer().getOnlinePlayers() )
        {
            playerList.put(player, loadRilncrafter( player ));
        }
    }

    private void loadModules() {
        modLoader.setModulesEnabled( getConfig().getStringList("modules_enabled") );
        modLoader.load(this);
        modLoader.enable();
    }

    private void unloadModules() {
        modLoader.disable();
    }

    private Rilncrafter loadRilncrafter(Player player) {
        File customConfigFile = new File(getDataFolder(), "players/"+player.getUniqueId().toString()+".yml" );
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        return new Rilncrafter( player , playerConfig );
    }

    private void savePlayerData(Rilncrafter rilncrafter) {
        
        Player player = rilncrafter.getPlayer();
        File customConfigFile = new File(getDataFolder(), "players/"+player.getUniqueId().toString()+".yml" );
        
        try {
            rilncrafter.getConfig().save( customConfigFile );
        } catch (IOException ex) {
            Logger.getLogger(Rilncraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
