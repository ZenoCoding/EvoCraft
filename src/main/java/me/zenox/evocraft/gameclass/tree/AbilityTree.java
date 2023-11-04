package me.zenox.evocraft.gameclass.tree;

import me.zenox.evocraft.abilities.ClassAbility;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a global ability tree, including GUI and all
 * <p>
 * Player information can be found in the PlayerData class
 */
public record AbilityTree (ClassAbility LL_Ability, ClassAbility LR_Ability,
                           ClassAbility RL_Ability, ClassAbility RR_Ability, List<Path> paths){

    public AbilityTree(ClassAbility LL_Ability, ClassAbility LR_Ability, ClassAbility RL_Ability, ClassAbility RR_Ability) {
        this(LL_Ability, LR_Ability, RL_Ability, RR_Ability, new ArrayList<>());
    }


}
