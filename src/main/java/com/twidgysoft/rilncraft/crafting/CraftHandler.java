package com.twidgysoft.rilncraft.crafting;

import com.twidgysoft.rilncraft.GainSkillEvent;
import com.twidgysoft.rilncraft.Rilncraft;
import com.twidgysoft.rilncraft.Rilncrafter;
import com.twidgysoft.rilncraft.SkillDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 
 * @author Jonny
 */
class CraftHandler {
    
    private final ArrayList<ItemStack> itemsCrafted;
    private final ArrayList<ItemStack[]> recipiesUsed;
    private final Rilncraft rilncraft;
    private final Player player;
    private final Rilncrafter rilncrafter;
    private final CraftingModule module;
    private HashMap<String,Integer> craftXPAwards;
    private HashMap<String,Integer> materialXPAwards;
    
    CraftHandler( CraftingModule module , Player player )
    {
        itemsCrafted = new ArrayList<>();
        recipiesUsed = new ArrayList<>();
        this.module = module;
        this.rilncraft = module.getRilncraft();
        this.player = player;
        rilncrafter = rilncraft.getRilncrafter(player);
    }
    
    void addItem( ItemStack itemCrafted , ItemStack[] itemsUsed )
    {
        itemsCrafted.add(itemCrafted);
        recipiesUsed.add(itemsUsed);
    }
    
    void run()
    {
        craftXPAwards = new HashMap<>();
        materialXPAwards = new HashMap<>();
        
        for( ItemStack itemCrafted : itemsCrafted )
        {
            processItemCrafted( itemCrafted );
        }
        
        for( ItemStack[] recipieUsed : recipiesUsed )
        {
            processRecipiesUsed( recipieUsed );
        }
        
        processCraftXPAwards();
        processMaterialXPAwards();
    }

    private void processItemCrafted(ItemStack itemCrafted) {
        ItemDefinition itemDefinition = module.getItemDefinition(itemCrafted.getType());
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
        
        int xp = itemCrafted.getAmount() * craftSkillXP;
        
        if( !craftXPAwards.containsKey( craftSkillID ) )
        {
            craftXPAwards.put(craftSkillID, xp);
            return;
        }
        
        craftXPAwards.put(craftSkillID, craftXPAwards.get(craftSkillID) + xp );
    }

    private void processCraftXPAwards() {
        
        for( Map.Entry<String,Integer> entry : craftXPAwards.entrySet() )
        {
            String craftSkillID = entry.getKey();
            int xp = entry.getValue();
            
            SkillDefinition definition = module.skills.get("craft").get( craftSkillID );
            
            if( definition == null )
            {
                String msg = String.format("craft.%s is null",craftSkillID);
                return;
            }
            
            GainSkillEvent e = new GainSkillEvent(player,definition,"crafting.craft."+craftSkillID,xp);
            Bukkit.getServer().getPluginManager().callEvent(e);
        }
        
    }

    private void processRecipiesUsed(ItemStack[] recipieUsed) {
        
        for( ItemStack materialUsed : recipieUsed )
        {
            if( materialUsed != null ) processMaterialUsed( materialUsed );
        }
        
    }

    private void processMaterialUsed(ItemStack materialUsed) {
        
        ItemDefinition itemDefinition = module.getItemDefinition(materialUsed.getType());
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
        
        if( !materialXPAwards.containsKey( materialSkillID ) )
        {
            materialXPAwards.put(materialSkillID, xp);
            return;
        }
        
        materialXPAwards.put(materialSkillID, materialXPAwards.get(materialSkillID) + xp );
    }

    private void processMaterialXPAwards() {
        
        for (Map.Entry<String,Integer> entry : materialXPAwards.entrySet()) {
            String craftSkillID = entry.getKey();
            int xp = entry.getValue();
            
            SkillDefinition defintion = module.skills.get("materials").get( craftSkillID );
            
            GainSkillEvent e = new GainSkillEvent(player,defintion,"crafting.materials."+craftSkillID,xp);
            Bukkit.getServer().getPluginManager().callEvent(e);
        }
        
    }
    
}