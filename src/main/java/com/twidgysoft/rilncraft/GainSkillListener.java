/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author Jonny
 */
class GainSkillListener implements Listener {
    private final Rilncraft plugin;

    public GainSkillListener(Rilncraft plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onGainSkillEvent( GainSkillEvent e )
    {
        int amount = e.getAmount();
        SkillDefinition definition = e.getDefinition();
        Player player = e.getPlayer();
        Rilncrafter rilncrafter = plugin.getRilncrafter(player);
        String skillID = e.getSkillID();
        String title = definition.title;
        
        Skill skill = rilncrafter.getSkill(skillID);
        int currentLevel = skill.getLevel();
        int nextLevelXP = definition.getXPForLevel( currentLevel + 1 );
        
        skill.addXP(amount);
        if( skill.getXp() > nextLevelXP )
        {
            skill.addLevel();
            int newLevel = skill.getLevel();
            String message = String.format("§a%s§r is now level §e%d§r!",title,newLevel);
            player.sendMessage( message );
        }
        
    }
    
}
