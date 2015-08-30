/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.worlds;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

/**
 *
 * @author Jonny
 */
class WaterPopulator extends BlockPopulator {

    public WaterPopulator() {
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int x=0; x<16; x++)
        {
            for (int z=0;z<16;z++)
            {
                Block blockAt;
                int realX = x + chunk.getX() * 16;
                int realZ = z + chunk.getZ() * 16;
                int y;
                for(y = 0; y < 65; y++ )
                {
                    blockAt = world.getBlockAt(realX, y, realZ);
                    if( blockAt.getType() == Material.AIR )
                    {
                        blockAt.setType(Material.WATER);
                    }
                }
            }
        }
    }
    
}
