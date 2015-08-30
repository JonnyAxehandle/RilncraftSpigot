/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft.worlds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

/**
 *
 * @author Jonny
 */
public class RilncraftWorldGenerator extends ChunkGenerator {

    ArrayList<BlockPopulator> pops;
    SimplexOctaveGenerator gen1;
    
    public RilncraftWorldGenerator() {
        pops = new ArrayList<>();
        pops.add( new BeachPopulator() );
        pops.add( new GrassPopulator() );
    }
    
    @Override
    /**
    * @param world The world the chunk belongs to
    * @param rand Don't use this, make a new random object using the world seed (world.getSeed())
    * @param biome Use this to set/get the current biome
    * @param ChunkX and ChunkZ The x and z co-ordinates of the current chunk.
    */
    public byte[][] generateBlockSections(World world, Random rand, int ChunkX,
    int ChunkZ, BiomeGrid biome)
    {
        gen1 = new SimplexOctaveGenerator(world,8);
        gen1.setScale(1/64.0);
        Location spawnLocation = world.getSpawnLocation();
        
        byte[][] chunk = new byte[world.getMaxHeight() / 16][];
        
        double frequency = 0.5; // the reciprocal of the distance between points
        double amplitude = 0.15; // The distance between largest min and max values
        int multitude = 14; //how much we multiply the value between -1 and 1. It will determine how "steep" the hills will be.
        int sea_level = 64;
        
        for (int x=0; x<16; x++)
        { //loop through all of the blocks in the chunk that are lower than maxHeight
            for (int z=0; z<16; z++)
            {
                
                int realX = x + ChunkX * 16; //used so that the noise function gives us
                int realZ = z + ChunkZ * 16; //different values each chunk
                
                double maxHeight = gen1.noise(realX, realZ, frequency, amplitude) * multitude + sea_level;
                
                for (int y=0;y<sea_level;y++)
                {
                    setBlock(x,y,z,chunk,Material.WATER);
                }
                
                for (int y=0;y<maxHeight;y++)
                {
                    setBlock(x,y,z,chunk,Material.STONE); //set the current block to stone
                }
                
                for (int y=0;y<4;y++)
                {
                    setBlock(x,y,z,chunk,Material.BEDROCK);
                }
                
                for (int y=4;y<maxHeight;y++)
                {
                    double density = gen1.noise(realX,y, realZ, 0.1, 0.1);
                    double threshold = 0.75;
                    if (density > threshold) {
                        setBlock(x,y,z,chunk,Material.AIR);
                    }
                }
                
                for (int y=128;y<240;y++) {
                    Location current = new Location(spawnLocation.getWorld(),realX,spawnLocation.getY(),realZ);
                    double distance = spawnLocation.distance( current );
                    
                    if( distance < 65 )
                    {
                        continue;
                    }
                    
                    double threshold = 0.75 + getDistanceBonus( distance ) + getHeightBonus( y );
                    
                    if( threshold > 1 || threshold < -1 )
                    {
                        continue;
                    }
                    
                    double density = gen1.noise(realX,y, realZ, 0.1, 0.1);
                    
                    if (density > threshold) {
                        setBlock(x,y,z,chunk,Material.STONE);
                    }                                        
                }
                
                
            }
        }
        
        return chunk;
    }
    
    /**
    * Returns a list of all of the block populators (that do "little" features)
    * to be called after the chunk generator
     * @param world
     * @return 
    */
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world)
    {
        return pops;
    }
    
    /**
    *
    * @param x
    * X co-ordinate of the block to be set in the array
    * @param y
    * Y co-ordinate of the block to be set in the array
    * @param z
    * Z co-ordinate of the block to be set in the array
    * @param chunk
    * An array containing the Block id's of all the blocks in the chunk. The first offset
    * is the block section number. There are 16 block sections, stacked vertically, each of which
    * 16 by 16 by 16 blocks.
    * @param material
    * The material to set the block to.
    */
    void setBlock(int x, int y, int z, byte[][] chunk, Material material)
    {
        if (y < 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0)
        { 
            if (chunk[y >> 4] == null)
            {
                chunk[y >> 4] = new byte[16 * 16 * 16];
            }
            
            chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (byte) material.getId();
        }
    }

    private double getDistanceBonus(double distance) {
        if( distance < 64 )
        {
            return 1;
        }
        
        if( distance > 128 )
        {
            return 0;
        }
        
        return ( 64 - distance ) / 64;
    }

    private double getHeightBonus(int y) {
        return Math.abs( Math.pow( (184 - y ) / 56 , 2 ) );
    }
    
}
