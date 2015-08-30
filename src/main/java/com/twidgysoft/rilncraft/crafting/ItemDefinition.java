/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.crafting;

import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Jonny
 */
public class ItemDefinition {
    private final ConfigurationSection config;
    
    public ItemDefinition( ConfigurationSection config )
    {
        this.config = config;
    }
    
    public String getCraftSkillID()
    {
        return config.getString("itemskill");
    }
    
    public int getCraftSkillXP()
    {
        return config.getInt("itemxp");
    }
    
    public String getMaterialSkillID()
    {
        return config.getString("materialskill");
    }
    
    public int getMaterialSkillXP()
    {
        return config.getInt("materialxp");
    }
    
}
