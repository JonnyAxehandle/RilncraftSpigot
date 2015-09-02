/**
 * 
 */
package com.twidgysoft.rilncraft.combat;

import com.twidgysoft.rilncraft.Rilncraft;
import com.twidgysoft.rilncraft.Module;
import com.twidgysoft.rilncraft.SkillDictionary;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;

/**
 * Handles events/commands related to combat.
 * @author Jonny
 */
public class CombatModule extends Module {
    final SkillDictionary skills;
    final WeaponSkillList weaponTypes;
    final PotionSkillList potionTypes;
    
    final MobDictionary mobDictionary;
    
    /**
     * Constructs the Combat Module.
     * @param rilncraft Current plugin instance
     * @param definition
     */
    public CombatModule(Rilncraft rilncraft,Configuration definition) {
        super(rilncraft,definition);
        
        skills = new SkillDictionary();
        weaponTypes = new WeaponSkillList();
        potionTypes = new PotionSkillList();
        mobDictionary = new MobDictionary( definition.getConfigurationSection("mobs") );
        
        skills.loadFromConfig(definition.getConfigurationSection("skills"));
        weaponTypes.loadFromConfig(definition.getConfigurationSection("weapons"));
        potionTypes.loadFromConfig(definition.getConfigurationSection("potions"));
        
        PluginManager pluginManager = rilncraft.getServer().getPluginManager();
        pluginManager.registerEvents(new MeleeListener(this), rilncraft);
        pluginManager.registerEvents(new PotionListener(this), rilncraft);
        pluginManager.registerEvents(new DeathListener(this), rilncraft);
        pluginManager.registerEvents(new ArcheryListener(this), rilncraft);
        
        rilncraft.getCommand("combat").setExecutor(new CombatCommandExecutor(this));
    }
    
    @Override
    public void onEnable() {
        
    }

    @Override
    public void onDisable() {
        
    }
    
}
