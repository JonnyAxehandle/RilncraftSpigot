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
public class SkillDefinition {
    
    public final String title;
    
    public SkillDefinition(String title)
    {
        this.title = title;
    }
    
    public int getXPForLevel( int level )
    {
        if( level < 1 )
        {
            return 0;
        }
        
        if( level == 1 )
        {
            return 10;
        }
        
        return ( 10 * level ) + getXPForLevel( level - 1 );
    }
    
}
