package me.zenox.superitems.item.abilities;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class ItemAbility extends Ability{
    private final AbilityAction action;

    public ItemAbility(String id, AbilityAction action, int manaCost, double cooldown) {
        super(id, manaCost, cooldown, PlayerInteractEvent.class, Slot.EITHER_HAND);
        this.action = action;
    }

    public ItemAbility(String name, String id, AbilityAction action, int manaCost, double cooldown) {
        super(name, id, manaCost, cooldown, PlayerInteractEvent.class, Slot.EITHER_HAND);
        this.action = action;
    }

    public ItemAbility(String name, String id, AbilityAction action, int manaCost, double cooldown, Slot slot) {
        super(name, id, manaCost, cooldown, PlayerInteractEvent.class, slot);
        this.action = action;
    }

    public AbilityAction getAction() {
        return action;
    }

    @Override
    public boolean checkEvent(PlayerEvent e) {
        if(!super.checkEvent(e)) return false;
        if (!action.isAction(((PlayerInteractEvent) e).getAction(), e.getPlayer().isSneaking())) {
            return false;
        }
        return true;
    }

//    public void useItemAbility(PlayerInteractEvent e){
//        super.useAbility(e);
//    }

    @Override
    protected void runExecutable(PlayerEvent e) {
        super.runExecutable(e);
    }

    public enum AbilityAction {

        LEFT_CLICK_BLOCK("LEFT CLICK", new Action[]{Action.LEFT_CLICK_BLOCK}, false), LEFT_CLICK_AIR("LEFT CLICK", new Action[]{Action.LEFT_CLICK_AIR}, false), LEFT_CLICK_ALL("LEFT CLICK", new Action[]{Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}, false), SHIFT_LEFT_CLICK("SHIFT LEFT CLICK", new Action[]{Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}, true),
        RIGHT_CLICK_BLOCK("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_AIR}, false), RIGHT_CLICK_AIR("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_BLOCK}, false), RIGHT_CLICK_ALL("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR}, false), SHIFT_RIGHT_CLICK("SHIFT RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}, true);

        private final String name;
        private final Action[] actionList;
        private final boolean requiresShift;

        AbilityAction(String name, Action[] actionList, boolean requiresShift) {
            this.name = name;
            this.actionList = actionList;
            this.requiresShift = requiresShift;
        }

        public String getName() {
            return this.name;
        }

        public boolean isAction(Action action, boolean isCrouching) {
            if (this.requiresShift && !isCrouching) return false;

            return Arrays.stream(actionList).anyMatch(action::equals);

        }
    }

    /**
     * Represents some built-in executables
     */
    static {

    }
}
