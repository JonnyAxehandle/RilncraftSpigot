/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.worlds;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

/**
 *
 * @author Jonny
 */
class GrassPopulator extends BlockPopulator {

    public GrassPopulator() {
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        ArrayList<Block> potentialTrees = new ArrayList<>();
        
        for (int x=0; x<16; x++)
        {
            for (int z=0;z<16;z++)
            {
                Block blockAt;
                Block blockBelow;
                int realX = x + chunk.getX() * 16;
                int realZ = z + chunk.getZ() * 16;
                int y;
                for(y = 64; y < 200; y++ )
                {
                    blockAt = world.getBlockAt(realX, y, realZ);
                    blockBelow = world.getBlockAt(realX, y-1, realZ);
                    if( blockAt.getType() == Material.AIR && blockBelow.getType() == Material.STONE )
                    {
                        blockBelow.setType(Material.GRASS);
                        world.getBlockAt(realX, y-2, realZ).setType(Material.DIRT);
                        
                        if( y - 1 > 69 )
                        {
                            potentialTrees.add(blockAt);
                        }
                        
                        break;
                    }
                }
            }
        }
        
        int size = potentialTrees.size();
        if( size > 0 )
        {
            for(int i = 0;i < 3;i++)
            {
                int nextInt = random.nextInt( size );
                Block treeSpotget = potentialTrees.get( nextInt );
                world.generateTree( treeSpotget.getLocation() , TreeType.TREE );
            }
            
            for(int i = 0;i < 3;i++)
            {
                int nextInt = random.nextInt( size );
                Block treeSpotget = potentialTrees.get( nextInt );
                Block blockAt = world.getBlockAt( treeSpotget.getLocation() );
                if( blockAt.getType() == Material.AIR )
                {
                    blockAt.setType(Material.GLOWSTONE);
                }
            }
        }
        
    }
    
}
