package me.zenox.evocraft.gameclass.tree;

import me.zenox.evocraft.abilities.ClassAbility;
import me.zenox.evocraft.gameclass.ClickCombination;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a global ability tree, including GUI and all
 * <p>
 * Player information can be found in the PlayerData class
 */
public record AbilityTree (ClassAbility LL_Ability, ClassAbility LR_Ability,
                           ClassAbility RL_Ability, ClassAbility RR_Ability, List<Path> paths){

    public AbilityTree(ClassAbility LL_Ability, ClassAbility LR_Ability, ClassAbility RL_Ability, ClassAbility RR_Ability, Path ... paths) {
        this(LL_Ability, LR_Ability, RL_Ability, RR_Ability, new ArrayList<>(List.of(
                new AbilityPath(LL_Ability.getId(), LL_Ability),
                new AbilityPath(LR_Ability.getId(), LR_Ability),
                new AbilityPath(RL_Ability.getId(), RL_Ability),
                new AbilityPath(RR_Ability.getId(), RR_Ability))));
        this.paths.addAll(List.of(paths));
    }

    public ClassAbility getAbility(ClickCombination cc){
        return switch(cc){
            case LEFT_LEFT -> LL_Ability;
            case LEFT_RIGHT -> LR_Ability;
            case RIGHT_LEFT -> RL_Ability;
            case RIGHT_RIGHT -> RR_Ability;
        };
    }

    public Path path(ClassAbility ability) {
        return paths.stream()
                .filter(path -> (path instanceof AbilityPath) && ((AbilityPath) path).ability.equals(ability))
                .findFirst()
                .orElse(null);
    }

    public Path path(String id) {
        return paths.stream()
                .filter(path -> path.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
