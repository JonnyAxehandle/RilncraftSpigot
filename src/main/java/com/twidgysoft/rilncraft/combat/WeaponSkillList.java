/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.combat;

import java.util.HashMap;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Jonny
 */
public class WeaponSkillList extends HashMap<String,String> {
    
    public void loadFromConfig( ConfigurationSection configWeapons )
    {
        Set<String> keys;
        keys = configWeapons.getKeys(false);
        for( String key : keys )
        {
            String mobType = configWeapons.getString(key);
            this.put(key, mobType);
        }
    }
    
}
