package me.zenox.evocraft.abilities;

import me.zenox.evocraft.Slot;
import me.zenox.evocraft.abilities.itemabilities.*;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AbilityRegistry {
    public static final ItemAbility SOUL_RIFT = new ItemAbility(new AbilitySettings()
            .id("soul_rift")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(100)
            .cooldown(50), ItemAbility::soulRiftAbility);
    public static final ItemAbility MAGIC_MISSILE_COMBUST_6 = new ItemAbility(new AbilitySettings()
            .id("magic_missile")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(0)
            .cooldown(0),
            (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.magicMissileAbility(event, p, item,true, 6));
    public static final ItemAbility MAGIC_MISSILE_DEV = new ItemAbility(new AbilitySettings()
            .id("magic_missile_dev")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(0)
            .cooldown(0), (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.magicMissileAbility(event, p, item, false, 15));
    public static final ItemAbility SMALL_EMBER_SHOOT = new ItemAbility(new AbilitySettings()
            .id("small_ember_shoot")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(10)
            .cooldown(0), ItemAbility::smallEmberShootAbility);
    public static final ItemAbility EMBER_SHOOT = new ItemAbility(new AbilitySettings()
            .id("ember_shoot")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(50)
            .cooldown(0), ItemAbility::emberShootAbility);
    public static final ItemAbility CENTRALIZE = new ItemAbility(new AbilitySettings()
            .id("centralize")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(150)
            .cooldown(0), (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.centralizeAbility(event, p, item, false, 30));

    public static final ItemAbility CENTRALIZE_CORRUPT = new ItemAbility(new AbilitySettings()/*"centralize_corrupt", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 250, 0, (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.centralizeAbility(event, p, item, true, 45)*/
            .id("centralize_corrupt")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(250)
            .cooldown(0), (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.centralizeAbility(event, p, item, true, 45));
    public static final ItemAbility OBSIDIAN_SHARD = new ItemAbility(new AbilitySettings()
            .id("obsidian_shard")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(25)
            .cooldown(0), ItemAbility::obsidianShardAbility);
    public static final ItemAbility TARHELM = new ItemAbility(new AbilitySettings()
            .id("tarhelm")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(150)
            .cooldown(30), ItemAbility::tarhelmAbility);
    public static final AttackAbility JUSTICE = new AttackAbility(new AbilitySettings()
            .id("justice")
            .manaCost(0)
            .cooldown(0)
            .slot(Slot.MAIN_HAND), AttackAbility::justiceAbility);
    public static final AttackAbility VERTEX_ABILITY = new AttackAbility(new AbilitySettings()
            .id("vertex_ability")
            .manaCost(20)
            .cooldown(0)
            .slot(Slot.MAIN_HAND), AttackAbility::vertexAbility);
    public static final AttackAbility DARK_FURY = new AttackAbility(new AbilitySettings()
            .id("dark_fury")
            .manaCost(20)
            .cooldown(0)
            .slot(Slot.MAIN_HAND), AttackAbility::darkFuryAbility);
    public static final FullSetAttackAbility TEST_FULLSET = new FullSetAttackAbility(new AbilitySettings()
            .id("test_fullset")
            .manaCost(10)
            .cooldown(0)
            .slot(Slot.ARMOR), FullSetAttackAbility::testFullSetAbility);
    public static final ItemAbility TERRA_STRIKE = new ItemAbility(new AbilitySettings()
            .id("terra_strike")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(10)
            .cooldown(5), ItemAbility::terraStrikeAbility);
    public static final FullSetAttackAbility ROARING_FLAME = new FullSetAttackAbility(new AbilitySettings()
            .id("roaring_flame")
            .passive(true)
            .manaCost(0)
            .cooldown(0)
            .slot(Slot.ARMOR), FullSetAttackAbility::roaringFlameAbility);
    public static final ItemAbility DARKCALL = new ItemAbility(new AbilitySettings()
            .id("darkcall")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(0)
            .cooldown(60), ItemAbility::darkcallerAbility);
    public static final ItemAbility GILDED_CONSUME = new ItemAbility(new AbilitySettings()
            .id("gilded_consume")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(0)
            .cooldown(0), ItemAbility::consumeAbility);
    public static final ItemAbility VOID_WARP = new ItemAbility(new AbilitySettings()
            .id("void_warp")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(100)
            .cooldown(1), ItemAbility::voidWarpAbility);
    public static final ItemAbility SNOW_SHOT = new ItemAbility(new AbilitySettings()
            .id("snow_shot")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(5)
            .cooldown(0), ItemAbility::snowShotAbility);
    public static final FullSetEntityDamagedAbility DIAMANTINE_SHIELD = new FullSetEntityDamagedAbility(new AbilitySettings()
            .id("diamantine_shield")
            .passive(true)
            .manaCost(0)
            .cooldown(0)
            .slot(Slot.ARMOR), FullSetEntityDamagedAbility::diamantineShieldAbility);
    public static final FullSetDamagedAbility GOLEMS_HEART = new FullSetDamagedAbility(new AbilitySettings()
            .id("golems_heart")
            .passive(true)
            .manaCost(0)
            .cooldown(0)
            .slot(Slot.ARMOR), FullSetDamagedAbility::golemsHeartAbility, List.of(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION));

    public static final FullSetDamagedAbility TITANS_HEART = new FullSetDamagedAbility(new AbilitySettings()
            .id("titans_heart")
            .passive(true)
            .manaCost(0)
            .cooldown(0)
            .slot(Slot.ARMOR), FullSetDamagedAbility::titansHeartAbility, List.of(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION));
    public static final ItemAbility CRYSTAL_SPIKE = new ItemAbility(new AbilitySettings()
            .id("crystal_spike")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(75)
            .cooldown(1), ItemAbility::crystalSpikeAbility);
    public static final ItemAbility MANA_BOOST = new ItemAbility(new AbilitySettings()
            .id("mana_boost")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(0)
            .cooldown(0), ItemAbility::manaBoostAbility);
    public static final ItemAbility COLOSSAL_SWEEP = new ItemAbility(new AbilitySettings()
            .id("colossal_sweep")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(100)
            .cooldown(0), ItemAbility::colossalSweepAbility);
    public static final ItemAbility THUNDERSTRIKE = new ItemAbility(new AbilitySettings()
            .id("thunderstrike")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .manaCost(50)
            .cooldown(0), ItemAbility::thunderstrikeAbility);
    public static final ItemAbility RAPID_FIRE = new ItemAbility(new AbilitySettings()
            .id("rapid_fire")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            , ItemAbility::rapidFireAbility);

    // Class Abilities
    public static final ClassAbility TELEPORT = new ClassAbility(new AbilitySettings()
            .id("mage_teleport")
            .manaCost(35)
            .cooldown(0)
            .range(10)
            .strength(1)
            .charges(1)
            .chargeTime(15)
            .modifiers(Modifier.of(Modifier.Type.RANGE, "teleport_range_1", +2),
                    Modifier.of(Modifier.Type.MANA_COST, "teleport_mana_1", -5),
                    Modifier.of(Modifier.Type.CHARGE, "teleport_charge_1", +1),
                    Modifier.of(Modifier.Type.EXECUTABLE, "teleport_dark", ClassAbility::darkTeleport),
                    Modifier.of(Modifier.Type.STRENGTH, "teleport_strength_1", +1),
                    Modifier.of(Modifier.Type.CHARGE, "teleport_charge_2", +1),
                    Modifier.of(Modifier.Type.EXECUTABLE, "teleport_surge", ClassAbility::surgeTeleport),
                    Modifier.of(Modifier.Type.RANGE, "teleport_range_2", +4),
                    Modifier.of(Modifier.Type.EXECUTABLE, "teleport_arcane", ClassAbility::arcaneTeleport))
            , ClassAbility::teleportAbility);

    public static final ClassAbility MANA_BALL = new ClassAbility(new AbilitySettings()
            .id("mage_mana_ball")
            .manaCost(15)
            .cooldown(0)
            .range(20)
            .strength(1)
            .charges(-1)
            .chargeTime(-1)
            .modifiers(Modifier.of(Modifier.Type.MANA_COST, "mana_ball_mana_1", -5),
                    Modifier.of(Modifier.Type.RANGE, "mana_ball_range_1", +2),
                    Modifier.of(Modifier.Type.STRENGTH, "mana_ball_strength_1", +1),
                    Modifier.of(Modifier.Type.EXECUTABLE, "mana_ball_homing", ClassAbility::homingManaBall),
                    Modifier.of(Modifier.Type.MANA_COST, "mana_ball_strength_2", +1),
                    Modifier.of(Modifier.Type.RANGE, "mana_ball_range", +5),
                    Modifier.of(Modifier.Type.EXECUTABLE, "mana_ball_multishot", ClassAbility::multishotManaBall),
                    Modifier.of(Modifier.Type.MANA_COST, "mana_ball_strength_2", +1),
                    Modifier.of(Modifier.Type.MULTI, "mana_ball_am",
                            Modifier.of(Modifier.Type.MANA_COST, "mana_ball_am_mana_boost", +50),
                            Modifier.of(Modifier.Type.EXECUTABLE, "mana_ball_am_arcane_multishot", ClassAbility::multishotArcaneSingularity),
                            Modifier.of(Modifier.Type.RANGE, "mana_ball_am_range", +15)))
            , ClassAbility::manaBallAbility);
    public static final ClassAbility RIFT_BEAM = new ClassAbility(new AbilitySettings()
            .id("mage_rift_beam")
            .manaCost(40)
            .cooldown(10)
            .range(20)
            .strength(2)
            .charges(0)
            .chargeTime(0)
            .modifiers(
                    Modifier.of(Modifier.Type.RANGE, "rift_beam_extend", +5),
                    Modifier.of(Modifier.Type.STRENGTH, "rift_beam_energize", +1),
                    Modifier.of(Modifier.Type.EXECUTABLE, "rift_beam_mark", ClassAbility::riftBeamMark),
                    Modifier.of(Modifier.Type.MANA_COST, "rift_beam_efficiency", -10),
                    Modifier.of(Modifier.Type.EXECUTABLE, "rift_beam_chain", ClassAbility::riftBeamChain),
                    Modifier.of(Modifier.Type.EXECUTABLE, "rift_beam_dimensional_rupture", ClassAbility::riftBeamDimensionalRupture),
                    Modifier.of(Modifier.Type.EXECUTABLE, "rift_beam_apex", ClassAbility::riftBeamApex)
            ), ClassAbility::riftBeamAbility);
    public static final ClassAbility RUNE_SHIELD = new ClassAbility(new AbilitySettings()
            .id("mage_rune_shield")
            .manaCost(50)
            .cooldown(0)
            .range(20)
            .strength(1)
,
 ClassAbility::runeShieldAbility);

    public static void registerAbilities(){
        Util.logToConsole("Registering %s abilities.".formatted(ChatColor.GOLD + "" + Ability.registeredAbilities.size() + ChatColor.RESET));
    }
}
