package com.twidgysoft.rilncraft;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

/**
 *
 * @author Jonny
 */
public class GainSkillEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final String skillID;
    private final int amount;
    private final SkillDefinition definition;

    public GainSkillEvent(Player player,SkillDefinition definition,String skillID,int amount)
    {
        this.player = player;
        this.skillID = skillID;
        this.amount = amount;
        this.definition = definition;
    }
     
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean bln) {
        this.cancelled = bln;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the skillID
     */
    public String getSkillID() {
        return skillID;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return the definition
     */
    public SkillDefinition getDefinition() {
        return definition;
    }

}
