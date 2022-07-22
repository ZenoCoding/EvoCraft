package me.zenox.superitems.items;

import me.zenox.superitems.util.Executable;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemAbility {
    private String abilityName;
    private List<String> abilityLore = new ArrayList();
    private Executable abilityExecutable = (PlayerInteractEvent e) -> {};

    public ItemAbility(String abilityName, List<String> abilityLore, Executable abilityExecutable){
        this.abilityName = abilityName;
        this.abilityLore = abilityLore;
        this.abilityExecutable = abilityExecutable;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public void setAbilityName(String abilityName) {
        this.abilityName = abilityName;
    }

    public List<String> getAbilityLore() {
        return abilityLore;
    }

    public void setAbilityLore(List<String> abilityLore) {
        this.abilityLore = abilityLore;
    }

    public Executable getAbilityExecutable() {
        return abilityExecutable;
    }

    public void setAbilityExecutable(Executable abilityExecutable) {
        this.abilityExecutable = abilityExecutable;
    }
}
