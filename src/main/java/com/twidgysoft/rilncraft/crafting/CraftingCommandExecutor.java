/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.crafting;

import com.twidgysoft.rilncraft.Rilncrafter;
import com.twidgysoft.rilncraft.SkillSection;
import com.twidgysoft.rilncraft.SkillSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonny
 */
class CraftingCommandExecutor implements CommandExecutor {
    private final CraftingModule module;

    public CraftingCommandExecutor(CraftingModule module) {
        this.module = module;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if( !( cs instanceof Player ) )
        {
            return true;
        }
        
        Player player = (Player) cs;
        
        if( args.length != 1 )
        {
            return false;
        }
        
        SkillSection section = module.skills.get( args[0] );
        if( section == null )
        {
            return false;
        }
        
        Rilncrafter rilncrafter = module.getRilncraft().getRilncrafter(player);
        String prefix = String.format("crafting.%s.",args[0]);
        section.forEach(new SkillSender(rilncrafter,prefix));
        
        return true;
    }
    
}
