package me.zenox.superitems.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class AbilityRegistry {
    public static final Ability SOUL_RIFT = new ItemAbility("soul_rift", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 100, 50, ItemAbility::soulRiftAbility);
    public static final Ability MAGIC_MISSILE_COMBUST_6 = new ItemAbility("magic_missile", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 0, 0, (Event event, Player p, ItemStack item) -> ItemAbility.magicMissileAbility(event, p, item,true, 6));
    public static final Ability MAGIC_MISSILE_DEV = new ItemAbility("magic_missile_dev", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 0, 0, (Event event, Player p, ItemStack item) -> ItemAbility.magicMissileAbility(event, p, item, false, 15));
    public static final Ability CENTRALIZE = new ItemAbility("centralize", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 150, 0, (Event event, Player p, ItemStack item) -> ItemAbility.centralizeAbility(event, p, item, false, 30));
    public static final Ability CENTRALIZE_CORRUPT = new ItemAbility("centralize_corrupt", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 250, 0, (Event event, Player p, ItemStack item) -> ItemAbility.centralizeAbility(event, p, item, true, 45));
    public static final Ability OBSIDIAN_SHARD = new ItemAbility("obsidian_shard", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 0, 0, ItemAbility::obsidianShardAbility);
    public static final Ability TARHELM = new ItemAbility("tarhelm", ItemAbility.AbilityAction.SHIFT_RIGHT_CLICK, 150, 30, ItemAbility::tarhelmAbility);
    public static final Ability JUSTICE = new AttackAbility("justice", 0, 0, AttackAbility::justiceAbility);
    public static final Ability DARK_FURY = new AttackAbility("dark_fury", 10, 0, AttackAbility::darkFuryAbility);
    public static final Ability TEST_FULLSET = new FullSetAttackAbility("test_fullset", 10, 0, FullSetAttackAbility::testFullSetAbility);
    public static final Ability TERRA_STRIKE = new ItemAbility("terra_strike", ItemAbility.AbilityAction.SHIFT_RIGHT_CLICK, 10, 5, ItemAbility::terraStrikeAbility);
}
