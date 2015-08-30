/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonny
 */
class RilnCommandExecutor implements CommandExecutor {
    private final Rilncraft rilncraft;

    public RilnCommandExecutor(Rilncraft rilncraft) {
        this.rilncraft = rilncraft;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String labal, String[] args) {
        if( !( cs instanceof Player ) )
        {
            return false;
        }
        
        Rilncrafter rilncrafter = rilncraft.getRilncrafter( (Player) cs );
        int rilnBalance = rilncrafter.getRilnBalance();
        cs.sendMessage(String.format("%d Riln", rilnBalance));
        
        return true;
    }
    
}
