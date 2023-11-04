package me.zenox.evocraft.gameclass.tree;

import me.zenox.evocraft.abilities.ClassAbility;
import me.zenox.evocraft.data.PlayerData;

/**
 * Represents an ability path, which is a linear progression of ability upgrades
 */
public class AbilityPath extends Path {

    public final ClassAbility ability;

    public AbilityPath(String id, ClassAbility ability) {
        super(id);
        this.ability = ability;
    }

    public ClassAbility getAbility() {
        return ability;
    }

    @Override
    public void apply(PlayerData playerData) {
        // Nothing, this is just a linear progression with no side effects
    }
}
