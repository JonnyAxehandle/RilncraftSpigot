/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.combat;

import java.util.HashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

/**
 *
 * @author Jonny
 */
public class MobDictionary {
    
    private final HashMap<EntityType,MobDefinition> mobs;
    
    public MobDictionary( ConfigurationSection dictionary )
    {
        mobs = new HashMap<>();
        for( String mobID : dictionary.getKeys(false) )
        {
            EntityType type = EntityType.fromName(mobID);
            ConfigurationSection mobDef = dictionary.getConfigurationSection(mobID);
            mobs.put(type,new MobDefinition(mobDef));
        }
    }
    
    public void setConfig( ConfigurationSection config )
    {
        for( String mobID : config.getKeys(false) )
        {
            
        }
    }
    
    public boolean hasDefinition( EntityType type )
    {
        return mobs.containsKey(type);
    }
    
    public MobDefinition getDefinition( EntityType type )
    {
        return mobs.get(type);
    }
    
}
