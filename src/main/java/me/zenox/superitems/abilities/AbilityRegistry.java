package me.zenox.superitems.abilities;

public class AbilityRegistry {
    public static Ability SOUL_RIFT = new ItemAbility("soul_rift", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 100, 50, ItemAbility::soulRiftAbility);
    public static Ability MAGIC_MISSILE_COMBUST_6 = new ItemAbility("magic_missile", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 0, 0, event -> ItemAbility.magicMissileAbility(event, true, 6));
    public static Ability MAGIC_MISSILE_DEV = new ItemAbility("magic_missile_dev", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 0, 0, event -> ItemAbility.magicMissileAbility(event, false, 15));
    public static Ability CENTRALIZE = new ItemAbility("centralize", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 150, 0, event -> ItemAbility.centralizeAbility(event, false, 30));
    public static Ability CENTRALIZE_CORRUPT = new ItemAbility("centralize_corrupt", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 250, 0, event -> ItemAbility.centralizeAbility(event, true, 45));
    public static Ability OBSIDIAN_SHARD = new ItemAbility("obsidian_shard", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 0, 0, ItemAbility::obsidianShardAbility);
    public static Ability TARHELM = new ItemAbility("tarhelm", ItemAbility.AbilityAction.SHIFT_RIGHT_CLICK, 150, 30, ItemAbility::tarhelmAbility);

    public static Ability JUSTICE = new AttackAbility("justice", 0, 0, AttackAbility::justiceAbility);
}
