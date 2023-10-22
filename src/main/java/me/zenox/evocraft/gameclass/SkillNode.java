package me.zenox.evocraft.gameclass;

import me.zenox.evocraft.abilities.Ability;

import java.util.List;
import java.util.Objects;

public final class SkillNode {
    private final String id;
    private final String name;
    private final String description;
    private final List<SkillNode> prerequisiteSkills;
    private final List<SkillNode> children;
    private final Ability ability;
    private final PassiveEffect passiveEffect;

    public SkillNode(String id, String name, String description, List<SkillNode> prerequisiteSkills, List<SkillNode> children, Ability ability, PassiveEffect passiveEffect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.prerequisiteSkills = prerequisiteSkills;
        this.children = children;
        this.ability = ability;
        this.passiveEffect = passiveEffect;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public List<SkillNode> prerequisiteSkills() {
        return prerequisiteSkills;
    }

    public List<SkillNode> children() {
        return children;
    }

    public Ability ability() {
        return ability;
    }

    public PassiveEffect passiveEffect() {
        return passiveEffect;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SkillNode) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, prerequisiteSkills, children, ability, passiveEffect);
    }

    @Override
    public String toString() {
        return "SkillNode[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "prerequisiteSkills=" + prerequisiteSkills + ", " +
                "children=" + children + ", " +
                "ability=" + ability + ", " +
                "passiveEffect=" + passiveEffect + ']';
    }

}
