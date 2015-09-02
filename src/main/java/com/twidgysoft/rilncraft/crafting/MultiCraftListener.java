/*
 *
 */
package com.twidgysoft.rilncraft.crafting;

import com.google.common.base.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listens for shift+clicks to craft multiple items
 * 
 * CODE CREDIT: http://pastebin.com/XjmH5N6A
 * @author Jonny
 */
class MultiCraftListener implements Listener {
    private final CraftingModule module;

    public MultiCraftListener(CraftingModule module) {
        this.module = module;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemCrafted( CraftItemEvent e )
    {
        if( !validateEvent( e ) )
        {
            return;
        }
        
        final Player player = (Player) e.getWhoClicked();
        final ItemStack toCraft = e.getCurrentItem();
        ItemStack toStore = e.getCursor();
        
        final ItemStack[] preInv = player.getInventory().getContents();
        final int ticks = 1;
        
        for (int i = 0; i < preInv.length; i++) {
            preInv[i] = preInv[i] != null ? preInv[i].clone() : null;
        }
        
        final CraftHandler handler = new CraftHandler( module , player );
        
        final ItemStack[] ingredientsUsed = e.getInventory().getMatrix();
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(module.getRilncraft(), new Runnable() {
        @Override
        public void run() {
            final ItemStack[] postInv = player.getInventory().getContents();
            int newItemsCount = 0;
           
            for (int i = 0; i < preInv.length; i++) {
                ItemStack pre = preInv[i];
                ItemStack post = postInv[i];
                
                // We're only interested in filled slots that are different
                if (hasSameItem(toCraft, post) && (!hasSameItem(toCraft, pre) || pre == null)) {
                    newItemsCount += post.getAmount() - (pre != null ? pre.getAmount() : 0);
                    
                    handler.addItem(post, ingredientsUsed);
                    
                    /*player.sendMessage( String.format("%s -> %s",
                        ( pre != null ? pre.getType().toString() : "NULL" ),
                        post.getType().toString()) );*/
                }
            }
           
            if (newItemsCount > 0) {
                // player.sendMessage( String.format("You crafted %d items",newItemsCount) );
                handler.run();
            }
        }
        }, ticks);
    }

    private boolean validateEvent(CraftItemEvent e) {
        if( !e.isShiftClick() )
        {
            return false;
        }
        
        if( !( e.getWhoClicked() instanceof Player ) )
        {
            return false;
        }
        
        if( e.getInventory() == null )
        {
            return false;
        }
        
        ItemStack itemCrafted = e.getCurrentItem();
        Material itemCraftedType = itemCrafted.getType();
        ItemStack[] ingredientsUsed = e.getInventory().getMatrix();
        
        /* Make sure none of the items are on the blacklist */
        if( module.isBlacklisted(itemCraftedType) || module.isBlacklisted(ingredientsUsed) )
        {
            return false;
        }
        
        switch( e.getInventory().getType() )
        {
            case CRAFTING:
            case WORKBENCH:
                break;
            default:
                return false;
        }
        
        return true;
    }
    
    private boolean hasSameItem(ItemStack a, ItemStack b) {
        if (a == null) return b == null;
        if (b == null) return false;
        
        return a.getTypeId() == b.getTypeId() &&
               a.getDurability() == b.getDurability() &&
                
               Objects.equal(a.getData(), b.getData()) &&
               Objects.equal(a.getEnchantments(), b.getEnchantments());
                
               //a.getData().equals( b.getData() ) &&
               //a.getEnchantments().equals( b.getEnchantments() );
    }
    
}
