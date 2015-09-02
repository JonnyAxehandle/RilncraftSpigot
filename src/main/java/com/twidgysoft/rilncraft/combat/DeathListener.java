/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.combat;

import com.twidgysoft.rilncraft.Rilncraft;
import com.twidgysoft.rilncraft.Rilncrafter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author Jonny
 */
class DeathListener implements Listener {
    private final CombatModule module;
    private final Random rand;
    private final HashMap<EntityType,String> mobHeads;
    
    DeathListener(CombatModule module) {
        this.rand = new Random();
        this.module = module;
        
        this.mobHeads = new HashMap<>();
        mobHeads.put(EntityType.BLAZE, "MHF_Blaze");
        mobHeads.put(EntityType.CAVE_SPIDER, "MHF_CaveSpider");
        mobHeads.put(EntityType.CHICKEN, "MHF_Chicken");
        mobHeads.put(EntityType.COW, "MHF_Cow");
        mobHeads.put(EntityType.ENDERMAN, "MHF_Enderman");
        mobHeads.put(EntityType.GHAST, "MHF_Ghast");
        mobHeads.put(EntityType.IRON_GOLEM, "MHF_Golem");
        mobHeads.put(EntityType.MAGMA_CUBE, "MHF_LavaSlime");
        mobHeads.put(EntityType.MUSHROOM_COW, "MHF_MushroomCow");
        mobHeads.put(EntityType.OCELOT, "MHF_Ocelot");
        mobHeads.put(EntityType.PIG, "MHF_Pig");
        mobHeads.put(EntityType.PIG_ZOMBIE, "MHF_PigZombie");
        mobHeads.put(EntityType.SHEEP, "MHF_Sheep");
        mobHeads.put(EntityType.SLIME, "MHF_Slime");
        mobHeads.put(EntityType.SPIDER, "MHF_Spider");
        mobHeads.put(EntityType.SQUID, "MHF_Squid");
        mobHeads.put(EntityType.VILLAGER, "MHF_Villager");
        
    }
    
    @EventHandler
    public void onCreatureKilled(EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        Player killer = entity.getKiller();
        
        if( killer == null )
        {
            return;
        }
        
        EntityType type = e.getEntity().getType();
        
        if( type == EntityType.PLAYER )
        {
            return;
        }
        
        Rilncraft rilncraft = module.getRilncraft();
        
        if( !rilncraft.getConfig().isConfigurationSection("mob_reward") )
        {
            killer.sendMessage("Error: mob_reward is not present in config!");
            return;
        }
        
        ConfigurationSection mob_reward = rilncraft.getConfig().getConfigurationSection("mob_reward");
        if( mob_reward.isConfigurationSection( type.toString() ) )
        {
            ConfigurationSection mob = mob_reward.getConfigurationSection( type.toString() );
            if( !mob.isInt("min") || !mob.isInt("max") )
            {
                return;
            }
            int reward = Math.abs( getRandom( mob.getInt("min") , mob.getInt("max") ) );
            Rilncrafter rilncrafter = rilncraft.getRilncrafter(killer);
            killer.sendMessage( String.format("§a+%d §eRiln", reward) );
            rilncrafter.addRiln( reward );
        }
        
        dropHead( e );
    }
    
    @EventHandler
    public void onPlayerKilled(EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        Player killer = entity.getKiller();
        
        if( killer == null )
        {
            return;
        }
        
        EntityType type = e.getEntity().getType();
        
        if( type != EntityType.PLAYER )
        {
            return;
        }
        
        Player target = (Player) entity;
        
        Rilncraft rilncraft = module.getRilncraft();
        
        Rilncrafter rilncrafter = rilncraft.getRilncrafter(killer);
        Rilncrafter rilncrafterTarget = rilncraft.getRilncrafter( target );
        int reward = rilncrafterTarget.getRilnBalance() / 2;
        
        killer.sendMessage( String.format("§a+%d §eRiln", reward) );
        rilncrafter.addRiln( reward );
        
        target.sendMessage( String.format("§4-%d §eRiln", reward) );
        rilncrafterTarget.addRiln(-1*reward);
        
        dropHead( e );
    }
    
    private int getRandom( int min , int max )
    {
        return rand.nextInt((max - min) + 1) + min;
    }

    private void dropHead(EntityDeathEvent e) {
        
        List<ItemStack> drops = e.getDrops();
        LivingEntity entity = e.getEntity();
        EntityType type = entity.getType();
        Player killer = entity.getKiller();
        ItemStack itemInHand = killer.getItemInHand();
        String weaponType = module.weaponTypes.get( itemInHand.getType().toString() );
        
        if( !"axes".equals(weaponType) )
        {
            return;
        }
        
        int axeLevel = module.getRilncraft().getRilncrafter(killer).getSkill("combat.weapons.axes").getLevel();
        int beheadChance = ( axeLevel / 4 ) * 3;
        int roll = getRandom(0,100);
        
        if( roll > beheadChance )
        {
            return;
        }
        
        ItemStack head;
        SkullMeta meta;
        switch( type )
        {
            case SKELETON:
                Skeleton skele = (Skeleton) entity;
                Skeleton.SkeletonType skeletonType = skele.getSkeletonType();
                
                if( skeletonType == Skeleton.SkeletonType.WITHER )
                {
                    head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.WITHER.ordinal());
                }
                else
                {
                    head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.SKELETON.ordinal());
                }
                break;
                
            case CREEPER:
                head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.CREEPER.ordinal());
                break;
                
            case ZOMBIE:
                head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.ZOMBIE.ordinal());
                break;
                
            case PLAYER:
                head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                meta = (SkullMeta) head.getItemMeta();
                Player target = (Player) entity;
                meta.setOwner( entity.getName() );
                head.setItemMeta(meta);
                break;
                
            default:
                if( !this.mobHeads.containsKey( type ) )
                {
                    return;
                }

                String playerName = mobHeads.get( type );

                head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                meta = (SkullMeta) head.getItemMeta();
                meta.setOwner( playerName );
                head.setItemMeta(meta);
                break;
        }
        
        drops.add(head);
        killer.sendMessage("§5*** BEHEADED! ***");
    }
    
}
