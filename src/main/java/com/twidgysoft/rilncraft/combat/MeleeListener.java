/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.combat;

import com.twidgysoft.rilncraft.GainSkillEvent;
import com.twidgysoft.rilncraft.SkillDefinition;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Jonny
 */
class MeleeListener implements Listener {
    private final CombatModule module;
    
    public MeleeListener(CombatModule module)
    {
        this.module = module;
    }
    
    @EventHandler
    public void onPlayerAttacksMob(EntityDamageByEntityEvent e)
    {
        EntityDamageEvent.DamageCause cause = e.getCause();
        if( cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK )
        {
            return;
        }
        
        if( !( e.getDamager() instanceof Player ) )
        {
            return;
        }
        
        Player damager = (Player) e.getDamager();
        
        if( !( e.getEntity() instanceof LivingEntity ) )
        {
            return;
        }
        
        awardMobSkill(e);
        awardMeleeSkill(e);
    }
    
    private void awardMobSkill(EntityDamageByEntityEvent e)
    {
        EntityType entityType = e.getEntityType();
        
        if( !module.mobDictionary.hasDefinition(entityType) )
        {
            return;
        }
        
        MobDefinition definition = module.mobDictionary.getDefinition(entityType);
        Player player = (Player) e.getDamager();
        
        String skillIDRaw = definition.getSkillID();
        SkillDefinition skillDef = module.skills.get("mobs").get( skillIDRaw );
        
        GainSkillEvent event = new GainSkillEvent(player,skillDef,"combat.mobs."+skillIDRaw, (int) e.getDamage());
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    private void awardMeleeSkill(EntityDamageByEntityEvent e) {
        
        Player player = (Player) e.getDamager();
        ItemStack itemInHand = player.getItemInHand();
        String skillIDRaw = module.weaponTypes.get( itemInHand.getType().toString() );
        SkillDefinition skillDef = module.skills.get("weapons").get( skillIDRaw );
        
        if( skillDef == null )
        {
            skillDef = module.skills.get("weapons").get("improv");
            skillIDRaw = "improv";
        }
        
        GainSkillEvent event = new GainSkillEvent(player,skillDef,"combat.weapons."+skillIDRaw, (int) e.getDamage());
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
    
}
