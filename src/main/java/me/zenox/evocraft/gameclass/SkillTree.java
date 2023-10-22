package me.zenox.evocraft.gameclass;

import java.util.List;
import java.util.Objects;

public final class SkillTree {
    private final SkillNode root;
    private final List<SkillNode> nodes;

    public SkillTree(SkillNode root, List<SkillNode> nodes) {
        this.root = root;
        this.nodes = nodes;
    }

    public SkillTree() {
        this(new SkillNode("test", "BANG", "boom boom shot", List.of(), List.of(), null, null), List.of());
    }

    public SkillNode root() {
        return root;
    }

    public List<SkillNode> nodes() {
        return nodes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SkillTree) obj;
        return Objects.equals(this.root, that.root) &&
                Objects.equals(this.nodes, that.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, nodes);
    }

    @Override
    public String toString() {
        return "SkillTree[" +
                "root=" + root + ", " +
                "nodes=" + nodes + ']';
    }

}
