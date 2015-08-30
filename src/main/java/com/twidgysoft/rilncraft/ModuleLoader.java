/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Jonny
 */
public class ModuleLoader {
    
    private List<String> modulesEnabled;
    private final List<Module> modulesLoaded;
    
    public ModuleLoader()
    {
        this.modulesLoaded = new ArrayList<>();
    }
    
    public void setModulesEnabled( List<String> modulesEnabled )
    {
        this.modulesEnabled = modulesEnabled;
    }
    
    public void load( Rilncraft rilncraft )
    {
        String filename;
        this.modulesLoaded.clear();
        for( String moduleID : modulesEnabled )
        {
            
            filename = String.format("modules/%s.yml",moduleID);
            loadModule( rilncraft , filename );
        }
    }
    
    private void loadModule( Rilncraft rilncraft , String filename )
    {
        YamlConfiguration definition;
        InputStream resource;
        InputStreamReader reader = null;
        
        try {
            
            resource = rilncraft.getResource( filename );
            reader = new InputStreamReader(resource, "UTF8");
            definition = YamlConfiguration.loadConfiguration(reader);
            initModuleClass( rilncraft , definition );
            
        } catch (UnsupportedEncodingException ex) {
            
            Logger.getLogger(ModuleLoader.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally {
            
            closeStream( reader );

        }
    }
    
    private void closeStream( InputStreamReader reader )
    {
        if( reader == null )
        {
            return;
        }
        
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(ModuleLoader.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    private void initModuleClass(Rilncraft rilncraft,YamlConfiguration definition) {
        
        ConfigurationSection module;
        String className;

        module = definition.getConfigurationSection("module");
        if( module == null )
        {
            return;
        }

        className = module.getString("main");
        
        try {
            
            Class<Module> _tempClass = (Class<Module>) Class.forName(className);
            Constructor<Module> ctor = _tempClass.getDeclaredConstructor(Rilncraft.class,Configuration.class);
            Module newInstance = ctor.newInstance( rilncraft , definition );
            modulesLoaded.add( newInstance );
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ModuleLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void enable()
    {
        for( Module module : modulesLoaded )
        {
            module.onEnable();
        }
    }
    
    public void disable()
    {
        for( Module module : modulesLoaded )
        {
            module.onDisable();
        }
    }
    
    public boolean isModule( String moduleID )
    {
        return modulesEnabled.contains( moduleID );
    }
    
    public Module getModule( String moduleID )
    {
        return null;
    }

}
