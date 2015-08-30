/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft;

import java.util.HashMap;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Jonny
 */
public class SkillDictionary extends HashMap<String,SkillSection> {
    
    public void loadFromConfig( ConfigurationSection configSkills )
    {
        Set<String> keys;
        Set<String> subkeys;
        ConfigurationSection subsection;
        
        keys = configSkills.getKeys(false);
        for( String key : keys )
        {
            this.put( key , new SkillSection() );
            
            subsection = configSkills.getConfigurationSection( key );
            subkeys = subsection.getKeys(false);
            for( String subkey : subkeys )
            {
                String title = subsection.getConfigurationSection(subkey).getString("title");
                this.get( key ).put( subkey , new SkillDefinition( title ));
            }
        }
    }
    
}
