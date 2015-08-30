/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.crafting;

import com.twidgysoft.rilncraft.GainSkillEvent;
import com.twidgysoft.rilncraft.SkillDefinition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Jonny
 */
class CraftListener implements Listener {
    private final CraftingModule module;

    public CraftListener(CraftingModule module) {
        this.module = module;
    }
    
    @EventHandler
    public void onItemCrafted( CraftItemEvent e )
    {
        if( !( e.getWhoClicked() instanceof Player ) )
        {
            return;
        }
        
        ItemStack itemCrafted = e.getCurrentItem();
        int itemCraftedAmount = itemCrafted.getAmount();
        Material itemCraftedType = itemCrafted.getType();
        Player player = (Player) e.getWhoClicked();
        ItemStack[] matrix = e.getInventory().getMatrix();
        
        if( module.isBlacklisted(itemCraftedType) || module.isBlacklisted(matrix) )
        {
            return;
        }
        
        InventoryAction action = e.getAction();
        switch( action )
        {
            case NOTHING:
                return;
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case PICKUP_SOME:
            case DROP_ONE_SLOT:
            case DROP_ALL_SLOT:
                awardCraftXP( player , itemCraftedType , itemCraftedAmount );
                awardMaterialXP( player , matrix );
                return;
            case MOVE_TO_OTHER_INVENTORY:
                return;
            default:
                return;
        }
    }

    private void awardCraftXP(Player player, Material itemCraftedType, int itemCraftedAmount) {
        ItemDefinition itemDefinition = module.getItemDefinition(itemCraftedType);
        if( itemDefinition == null )
        {
            return;
        }
        
        String craftSkillID = itemDefinition.getCraftSkillID();
        int craftSkillXP = itemDefinition.getCraftSkillXP();
        if( craftSkillID == null || craftSkillXP == 0 )
        {
            return;
        }
        
        int xp = itemCraftedAmount * craftSkillXP;
        SkillDefinition defintion = module.skills.get("craft").get( craftSkillID );
        
        GainSkillEvent e = new GainSkillEvent(player,defintion,"crafting.craft."+craftSkillID,xp);
        Bukkit.getServer().getPluginManager().callEvent(e);
    }

    private void awardMaterialXP(Player player, ItemStack[] matrix) {
        for( ItemStack item : matrix )
        {
            if( item != null ) awardMaterialXP(player,item.getType());
        }
    }
    
    private void awardMaterialXP(Player player, Material material) {
        ItemDefinition itemDefinition = module.getItemDefinition(material);
        if( itemDefinition == null )
        {
            return;
        }
        
        String materialSkillID = itemDefinition.getMaterialSkillID();
        int materialSkillXP = itemDefinition.getMaterialSkillXP();
        
        if( materialSkillID == null || materialSkillXP == 0 )
        {
            return;
        }
        
        int xp = materialSkillXP;
        SkillDefinition defintion = module.skills.get("materials").get( materialSkillID );
        
        GainSkillEvent e = new GainSkillEvent(player,defintion,"crafting.materials."+materialSkillID,xp);
        Bukkit.getServer().getPluginManager().callEvent(e);
    }
    
}
