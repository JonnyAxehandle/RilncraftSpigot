/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.combat;

import com.twidgysoft.rilncraft.GainSkillEvent;
import com.twidgysoft.rilncraft.SkillDefinition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 *
 * @author Jonny
 */
class ArcheryListener implements Listener {
    private final CombatModule module;

    public ArcheryListener(CombatModule module) {
        this.module = module;
    }
    
    @EventHandler
    public void onEntityHit( EntityDamageByEntityEvent e )
    {
        Entity target = e.getEntity();
        Entity damager = e.getDamager();
        
        if( damager.getType() != EntityType.ARROW )
        {
            return;
        }
        
        Arrow arrow = (Arrow) damager;
        ProjectileSource shooter = arrow.getShooter();
        
        if( !( shooter instanceof Player ) )
        {
            return;
        }
        
        Player player = (Player) shooter;
        int damage = (int) e.getDamage();
        
        SkillDefinition skillDef = module.skills.get("archery").get("general");
        
        GainSkillEvent event = new GainSkillEvent(player,skillDef,"combat.archery.general",damage);
        Bukkit.getServer().getPluginManager().callEvent(event);
        
        awardMobSkill( e );
    }
    
    private void awardMobSkill( EntityDamageByEntityEvent e )
    {
        EntityType entityType = e.getEntityType();
        
        if( !module.mobDictionary.hasDefinition(entityType) )
        {
            return;
        }
        
        MobDefinition definition = module.mobDictionary.getDefinition(entityType);
        
        Arrow arrow = (Arrow) e.getDamager();
        ProjectileSource shooter = arrow.getShooter();
        
        if( !( shooter instanceof Player ) )
        {
            return;
        }
        
        Player player = (Player) shooter;
        
        String skillIDRaw = definition.getSkillID();
        SkillDefinition skillDef = module.skills.get("mobs").get( skillIDRaw );
        
        GainSkillEvent event = new GainSkillEvent(player,skillDef,"combat.mobs."+skillIDRaw, (int) e.getDamage());
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
    
}
