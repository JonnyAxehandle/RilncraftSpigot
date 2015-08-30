/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonny
 */
public class Rilncrafter {
    private final Player player;
    private final YamlConfiguration config;
    private int rilnBalance;
    private final HashMap<String,Skill> skills;
    
    public Rilncrafter(Player player,YamlConfiguration config) {
        this.skills = new HashMap<>();
        this.rilnBalance = 0;
        this.player = player;
        this.config = config;
        
        if( config.isInt("riln") )
        {
            rilnBalance = config.getInt("riln");
        }
    }
    
    public Player getPlayer()
    {
        return player;
    }

    /**
     * @return the config
     */
    public YamlConfiguration getConfig() {
        config.set("riln", getRilnBalance());
        
        for( Map.Entry<String,Skill> entry : skills.entrySet() )
        {
            String key = entry.getKey();
            Skill value = entry.getValue();
            ConfigurationSection skillConfig = config.getConfigurationSection("skills");
            ConfigurationSection sectionConfig = skillConfig.getConfigurationSection( key );
            sectionConfig.set("xp", value.getXp());
            sectionConfig.set("level", value.getLevel());
        }
        
        return config;
    }
    
    public int getRilnBalance()
    {
        return rilnBalance;
    }
    
    public void addRiln( int riln )
    {
        rilnBalance += riln;
    }

    public Skill getSkill(String skillID) {
        
        if( !skills.containsKey( skillID ) )
        {
            skills.put(skillID, loadSkill( skillID ) );
        }
        
        return skills.get( skillID );
    }

    private Skill loadSkill(String skillID) {
        if( !config.isConfigurationSection("skills") )
        {
            config.createSection("skills");
        }
        
        ConfigurationSection skillConfig = config.getConfigurationSection("skills");
        if( !skillConfig.isConfigurationSection(skillID) )
        {
            ConfigurationSection newSection = skillConfig.createSection(skillID);
            newSection.set("xp",0);
            newSection.set("level",0);
        }
        
        ConfigurationSection sectionConfig = skillConfig.getConfigurationSection(skillID);
        return new Skill( sectionConfig.getInt("xp") , sectionConfig.getInt("level") );
    }
    
}
