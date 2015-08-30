/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft;

import org.bukkit.configuration.Configuration;
import org.bukkit.event.Listener;

/**
 *
 * @author Jonny
 */
public abstract class Module implements Listener {
    
    protected final Rilncraft rilncraft;
    protected final Configuration definition;
    
    public Module(Rilncraft rilncraft, Configuration definition)
    {
        this.rilncraft = rilncraft;
        this.definition = definition;
    }
    
    public Rilncraft getRilncraft()
    {
        return this.rilncraft;
    }
    
    public abstract void onEnable();
    public abstract void onDisable();
    
}
