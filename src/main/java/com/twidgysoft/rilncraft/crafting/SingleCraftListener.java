/*
 * 
 */
package com.twidgysoft.rilncraft.crafting;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

/**
 * Listens for items being crafted, awards players XP, and rolls for perks
 * @author Jonny
 */
class SingleCraftListener implements Listener {
    private final CraftingModule module;

    public SingleCraftListener(CraftingModule module) {
        this.module = module;
    }
    
    /**
     * Handles the event of an item being crafted on a grid
     * @param e 
     */
    @EventHandler
    public void onItemCrafted( CraftItemEvent e )
    {
        ItemStack[] ingredientsUsed;
        ItemStack itemCrafted;
        int itemCraftedAmount;
        Material itemCraftedType;
        Player player;
        
        /* Make sure it's a player */
        if( !( e.getWhoClicked() instanceof Player ) )
        {
            return;
        }
        
        itemCrafted = e.getCurrentItem();
        itemCraftedAmount = itemCrafted.getAmount();
        itemCraftedType = itemCrafted.getType();
        
        player = (Player) e.getWhoClicked();
        
        ingredientsUsed = e.getInventory().getMatrix();
        
        /* Make sure none of the items are on the blacklist */
        if( module.isBlacklisted(itemCraftedType) || module.isBlacklisted(ingredientsUsed) )
        {
            return;
        }
        
        /* Make sure something actually got made */
        InventoryAction action = e.getAction();
        switch( action )
        {
            /* "NOTHING" refers to a craft action that failed, such as clicking
            the item in the crafting result slot with a different item or non
            stacking item */
            case NOTHING:
                return;
                    
            /* Actions for a successful crafting */
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case PICKUP_SOME:
            case DROP_ONE_SLOT:
            case DROP_ALL_SLOT:
                CraftHandler handler = new CraftHandler( module , player );
                handler.addItem(itemCrafted, ingredientsUsed);
                handler.run();
                return;
                
            case MOVE_TO_OTHER_INVENTORY:
                return;
                
            /* In case we missed something, this will let us know */
            default:
                String msg = String.format("WARNING: InventoryAction '%s' unaccounted for",action.name());
                player.sendMessage(msg);
                Logger.getLogger(getClass().getName()).log(Level.WARNING,msg);
        }
    }
    
}
