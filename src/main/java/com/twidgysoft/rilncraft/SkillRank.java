/*
 * 
 */
package com.twidgysoft.rilncraft;

/**
 *
 * @author Jonny
 */
public enum SkillRank {
    
    F("§7",0),
    E("§3",1),
    D("§2",2),
    C("§6",3),
    B("§4",4),
    A("§d",5),
    S("§e",6);

    static SkillRank forLevel(int level) {
        if( level >= 100 ) return S;
        if( level >= 60 ) return A;
        if( level >= 35 ) return B;
        if( level >= 20 ) return C;
        if( level >= 10 ) return D;
        if( level >= 5 ) return E;
        return F;
    }
    
    public final String prefix;
    public final int id;
    
    SkillRank( String prefix , int id )
    {
        this.prefix = prefix;
        this.id = id;
    }
}
