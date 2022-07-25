package me.zenox.superitems.items;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Executable;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemAbility {
    private String name;
    private String id;
    private List<String> lore;
    private AbilityAction action;
    private Executable executable;
    private int cooldown;

    public ItemAbility(String name, String id, List<String> lore, AbilityAction action, Executable executable, int cooldown){
        this.name = name;
        this.lore = lore;
        this.action = action;
        this.executable = executable;
        this.cooldown = cooldown;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public AbilityAction getAction(){
        return action;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Executable getExecutable() {
        return executable;
    }

    public void runExecutable(PlayerInteractEvent e){
        if(action.isAction(e.getAction(), e.getPlayer().isSneaking())) {

            PersistentDataContainer container = e.getPlayer().getPersistentDataContainer();
            NamespacedKey cooldownKey = new NamespacedKey(SuperItems.getPlugin(), getId() + "_cooldown");
            Long cooldown = container.get(cooldownKey, PersistentDataType.LONG);

            if (cooldown != null && Math.ceil((cooldown - System.currentTimeMillis()) / 1000) > 0) {
                Util.sendMessage(e.getPlayer(), "This ability is on cooldown for " + ChatColor.RED + Math.ceil((cooldown - System.currentTimeMillis()) / 1000) + " seconds");
                return;
            }

            this.getExecutable().run(e);


            container.set(cooldownKey, PersistentDataType.LONG, System.currentTimeMillis() + (getCooldown() * 1000));
        }
    }


    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    enum AbilityAction {

        LEFT_CLICK_BLOCK("LEFT CLICK", new Action[]{Action.LEFT_CLICK_BLOCK}, false), LEFT_CLICK_AIR("LEFT CLICK", new Action[]{Action.LEFT_CLICK_AIR}, false), LEFT_CLICK_ALL("LEFT CLICK", new Action[]{Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}, false), SHIFT_LEFT_CLICK("SHIFT LEFT CLICK", new Action[]{Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}, true),
        RIGHT_CLICK_BLOCK("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_AIR}, false), RIGHT_CLICK_AIR("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_BLOCK}, false), RIGHT_CLICK_ALL("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR}, false), SHIFT_RIGHT_CLICK("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}, true);

        private String name;
        private Action[] actionList;
        private boolean requiresShift;

        AbilityAction(String name, Action[] actionList, boolean requiresShift){
            this.name = name;
            this.actionList = actionList;
            this.requiresShift = requiresShift;
        }

        public String getName(){
            return this.name;
        }

        public boolean isAction(Action action, boolean isCrouching){
            List<Action> leftClickActions = new ArrayList<>();
            leftClickActions.add(Action.LEFT_CLICK_AIR);
            leftClickActions.add(Action.LEFT_CLICK_BLOCK);

            List<Action> rightClickActions = new ArrayList<>();
            rightClickActions.add(Action.RIGHT_CLICK_AIR);
            rightClickActions.add(Action.RIGHT_CLICK_BLOCK);

            if(this.requiresShift && !isCrouching) return false;

            if(Arrays.stream(actionList).anyMatch(action::equals)) return true;

            return false;

        }
    }
}
