/*
 * 
 */
package com.twidgysoft.rilncraft.crafting;

import com.twidgysoft.rilncraft.GainSkillEvent;
import com.twidgysoft.rilncraft.SkillDefinition;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
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
class CraftListener implements Listener {
    private final CraftingModule module;

    public CraftListener(CraftingModule module) {
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
                awardCraftXP( player , itemCraftedType , itemCraftedAmount );
                awardMaterialXP( player , ingredientsUsed );
                return;
            
            /* Action for shift+clicking to craft */
            case MOVE_TO_OTHER_INVENTORY:
                
                // TODO: Figure out shift clicking
                
                return;
                
            /* In case we missed something, this will let us know */
            default:
                String msg = String.format("WARNING: InventoryAction '%s' unaccounted for",action.name());
                player.sendMessage(msg);
                Logger.getLogger(getClass().getName()).log(Level.WARNING,msg);
        }
    }

    /**
     * Awards the player XP for crafting an item
     * @param player
     * @param itemCraftedType
     * @param itemCraftedAmount 
     */
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

    /**
     * Awards the player XP for using materials in a crafting matrix
     * @param player
     * @param matrix 
     */
    private void awardMaterialXP(Player player, ItemStack[] matrix) {
        for( ItemStack item : matrix )
        {
            if( item != null ) awardMaterialXP(player,item.getType());
        }
    }
    
    /**
     * Awards the player XP for using a material
     * @param player
     * @param material 
     */
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
