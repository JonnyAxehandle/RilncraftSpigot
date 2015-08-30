/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft;

/**
 *
 * @author Jonny
 */
public class Skill {
    
    private int xp;
    private int level;
    
    public Skill( int xp, int level )
    {
        this.xp = xp;
        this.level = level;
    }

    /**
     * @return the xp
     */
    public int getXp() {
        return xp;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }
    
    public void addXP( int xp )
    {
        this.xp += xp;
    }
    
    public void addLevel()
    {
        this.level++;
    }
    
}
