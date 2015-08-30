/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.combat;

import com.twidgysoft.rilncraft.GainSkillEvent;
import com.twidgysoft.rilncraft.SkillDefinition;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;

/**
 *
 * @author Jonny
 */
class PotionListener implements Listener {
    private final CombatModule module;
    
    public PotionListener(CombatModule module)
    {
        this.module = module;
    }
    
    @EventHandler
    public void onPotionHit(PotionSplashEvent e)
    {
        ThrownPotion potion = e.getPotion();
        
        if( !(potion.getShooter() instanceof Player) )
        {
            return;
        }
        
        Player shooter = (Player) potion.getShooter();
        Collection<LivingEntity> affectedEntities = e.getAffectedEntities();
        Collection<PotionEffect> effects = potion.getEffects();
        
        for( LivingEntity target : affectedEntities )
        {
            for( PotionEffect effect : effects )
            {
                handleHit( shooter , target , effect );
            }
        }
    }

    private void handleHit(Player player, LivingEntity target, PotionEffect effect) {
        
        String skillID = module.potionTypes.get( effect.getType().getName() );
        SkillDefinition skillDef = module.skills.get("potions").get( skillID );
        
        GainSkillEvent event = new GainSkillEvent(player,skillDef,"combat.potions."+skillID,10);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}
