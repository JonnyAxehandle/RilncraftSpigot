/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.crafting;

import com.twidgysoft.rilncraft.Module;
import com.twidgysoft.rilncraft.Rilncraft;
import com.twidgysoft.rilncraft.SkillDictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author Jonny
 */
public class CraftingModule extends Module {

    private final HashMap<String,ItemDefinition> items;
    final SkillDictionary skills;
    private final List<String> blacklist;
    
    public CraftingModule(Rilncraft rilncraft, Configuration definition) {
        super(rilncraft, definition);
        items = new HashMap<>();
        
        skills = new SkillDictionary();
        skills.loadFromConfig(definition.getConfigurationSection("skills"));
        
        blacklist = definition.getStringList("blacklist");
        
        rilncraft.getCommand("crafting").setExecutor(new CraftingCommandExecutor(this));
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = rilncraft.getServer().getPluginManager();
        pluginManager.registerEvents(new SingleCraftListener(this), rilncraft);
        pluginManager.registerEvents(new MultiCraftListener(this), rilncraft);
        
        items.clear();
        ConfigurationSection itemDef = definition.getConfigurationSection("items");
        Set<String> keys = itemDef.getKeys(false);
        for( String key : keys )
        {
            items.put(key, new ItemDefinition( itemDef.getConfigurationSection(key) ));
        }
    }

    @Override
    public void onDisable() {
        
    }
    
    boolean isBlacklisted( Material item )
    {
        if( blacklist == null )
        {
            return false;
        }
        
        return blacklist.contains( item.toString() );
    }

    boolean isBlacklisted(ItemStack[] matrix) {
        for( ItemStack item : matrix )
        {
            if( item == null )
            {
                continue;
            }
            
            if( isBlacklisted( item.getType() ) )
            {
                return true;
            }
        }
        
        return false;
    }
    
    ItemDefinition getItemDefinition( Material material )
    {
        return items.get(material.toString());
    }
    
}
