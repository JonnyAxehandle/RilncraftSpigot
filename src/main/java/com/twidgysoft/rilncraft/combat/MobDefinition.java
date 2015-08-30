/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.combat;

import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Jonny
 */
public class MobDefinition {
    private final String skillID;
    private int minReward;
    private int maxReward;
    
    public MobDefinition( ConfigurationSection definition )
    {
        skillID = definition.getString("skillID","none");
    }
    
    public void setConfig( ConfigurationSection config )
    {
        minReward = config.getInt("min", 0);
        maxReward = config.getInt("max", 0);
    }
    
    public int getMaxReward()
    {
        return maxReward;
    }
    
    public int getMinReward()
    {
        return minReward;
    }

    public String getSkillID()
    {
        return skillID;
    }
    
}
