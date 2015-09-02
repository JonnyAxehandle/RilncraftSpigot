/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twidgysoft.rilncraft;

import java.util.function.BiConsumer;

/**
 *
 * @author Jonny
 */
public class SkillSender implements BiConsumer<String,SkillDefinition> {
    
    private final Rilncrafter rilncrafter;
    private final String prefix;

    public SkillSender( Rilncrafter Rilncrafter )
    {
        this.rilncrafter = Rilncrafter;
        this.prefix = "";
    }
    
    public SkillSender( Rilncrafter Rilncrafter , String prefix )
    {
        this.rilncrafter = Rilncrafter;
        this.prefix = prefix;
    }
    
    @Override
    public void accept(String rawID, SkillDefinition definition) {
        String skillID = prefix+rawID;
        Skill skill = rilncrafter.getSkill(skillID);
        
        String label = String.format("%s%s§r",skill.getRank().prefix,definition.title);
        
        String rank = String.format("(%s%s§r)",skill.getRank().prefix,skill.getRank().name());
        
        String display = String.format("%s: %d %s",label,skill.getLevel(),rank);
        
        rilncrafter.getPlayer().sendMessage(display);
    }
    
}
