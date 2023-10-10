package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AbilityRegistry {
    public static final ItemAbility SOUL_RIFT = new ItemAbility(new AbilitySettings()
            .setId("soul_rift")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(100)
            .setCooldown(50), ItemAbility::soulRiftAbility);
    public static final ItemAbility MAGIC_MISSILE_COMBUST_6 = new ItemAbility(new AbilitySettings()
            .setId("magic_missile")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(0)
            .setCooldown(0),
            (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.magicMissileAbility(event, p, item,true, 6));
    public static final ItemAbility MAGIC_MISSILE_DEV = new ItemAbility(new AbilitySettings()
            .setId("magic_missile_dev")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(0)
            .setCooldown(0), (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.magicMissileAbility(event, p, item, false, 15));
    public static final ItemAbility SMALL_EMBER_SHOOT = new ItemAbility(new AbilitySettings()
            .setId("small_ember_shoot")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(10)
            .setCooldown(0), ItemAbility::smallEmberShootAbility);
    public static final ItemAbility EMBER_SHOOT = new ItemAbility(new AbilitySettings()
            .setId("ember_shoot")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(25)
            .setCooldown(0), ItemAbility::emberShootAbility);
    public static final ItemAbility CENTRALIZE = new ItemAbility(new AbilitySettings()
            .setId("centralize")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(150)
            .setCooldown(0), (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.centralizeAbility(event, p, item, false, 30));

    public static final ItemAbility CENTRALIZE_CORRUPT = new ItemAbility(new AbilitySettings()/*"centralize_corrupt", ItemAbility.AbilityAction.RIGHT_CLICK_ALL, 250, 0, (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.centralizeAbility(event, p, item, true, 45)*/
            .setId("centralize_corrupt")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(250)
            .setCooldown(0), (PlayerInteractEvent event, Player p, ItemStack item) -> ItemAbility.centralizeAbility(event, p, item, true, 45));
    public static final ItemAbility OBSIDIAN_SHARD = new ItemAbility(new AbilitySettings()
            .setId("obsidian_shard")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(25)
            .setCooldown(0), ItemAbility::obsidianShardAbility);
    public static final ItemAbility TARHELM = new ItemAbility(new AbilitySettings()
            .setId("tarhelm")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(150)
            .setCooldown(30), ItemAbility::tarhelmAbility);
    public static final AttackAbility JUSTICE = new AttackAbility(new AbilitySettings()
            .setId("justice")
            .setManaCost(0)
            .setCooldown(0)
            .setSlot(Slot.MAIN_HAND), AttackAbility::justiceAbility);
    public static final AttackAbility VERTEX_ABILITY = new AttackAbility(new AbilitySettings()
            .setId("vertex_ability")
            .setManaCost(20)
            .setCooldown(0)
            .setSlot(Slot.MAIN_HAND), AttackAbility::vertexAbility);
    public static final AttackAbility DARK_FURY = new AttackAbility(new AbilitySettings()
            .setId("dark_fury")
            .setManaCost(20)
            .setCooldown(0)
            .setSlot(Slot.MAIN_HAND), AttackAbility::darkFuryAbility);
    public static final FullSetAttackAbility TEST_FULLSET = new FullSetAttackAbility(new AbilitySettings()
            .setId("test_fullset")
            .setManaCost(10)
            .setCooldown(0)
            .setSlot(Slot.ARMOR), FullSetAttackAbility::testFullSetAbility);
    public static final ItemAbility TERRA_STRIKE = new ItemAbility(new AbilitySettings()
            .setId("terra_strike")
            .setAbilityAction(ItemAbility.AbilityAction.SHIFT_RIGHT_CLICK)
            .setManaCost(10)
            .setCooldown(5), ItemAbility::terraStrikeAbility);
    public static final FullSetAttackAbility ROARING_FLAME = new FullSetAttackAbility(new AbilitySettings()
            .setId("roaring_flame")
            .setPassive(true)
            .setManaCost(0)
            .setCooldown(0)
            .setSlot(Slot.ARMOR), FullSetAttackAbility::roaringFlameAbility);
    public static final MoveAbility LAVA_GLIDER = new MoveAbility(new AbilitySettings()
            .setId("lava_glider")
            .setPassive(true)
            .setManaCost(0)
            .setCooldown(0)
            .setSlot(Slot.ARMOR), MoveAbility::lavaGliderAbility);
    public static final ItemAbility DARKCALL = new ItemAbility(new AbilitySettings()
            .setId("darkcall")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(0)
            .setCooldown(60), ItemAbility::darkcallerAbility);
    public static final ItemAbility GILDED_CONSUME = new ItemAbility(new AbilitySettings()
            .setId("gilded_consume")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(0)
            .setCooldown(0), ItemAbility::consumeAbility);
    public static final ItemAbility VOID_WARP = new ItemAbility(new AbilitySettings()
            .setId("void_warp")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(100)
            .setCooldown(1), ItemAbility::voidWarpAbility);
    public static final ItemAbility VOIDULAR_RECALL= new ItemAbility(new AbilitySettings()
            .setId("voidular_recall")
            .setAbilityAction(ItemAbility.AbilityAction.SHIFT_RIGHT_CLICK)
            .setManaCost(10)
            .setCooldown(1), ItemAbility::voidularRecallAbility);
    public static final ItemAbility START_BUTTON = new ItemAbility(new AbilitySettings()
            .setId("start_button")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(0)
            .setCooldown(0), ItemAbility::startButtonAbility);
    public static final ItemAbility PORTALIZER = new ItemAbility(new AbilitySettings()
            .setId("portalizer")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(0)
            .setCooldown(0), ItemAbility::portalizerAbility);
    public static final ItemAbility SNOW_SHOT = new ItemAbility(new AbilitySettings()
            .setId("snow_shot")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(5)
            .setCooldown(0), ItemAbility::snowShotAbility);
    public static final FullSetEntityDamagedAbility DIAMANTINE_SHIELD = new FullSetEntityDamagedAbility(new AbilitySettings()
            .setId("diamantine_shield")
            .setPassive(true)
            .setManaCost(0)
            .setCooldown(0)
            .setSlot(Slot.ARMOR), FullSetEntityDamagedAbility::diamantineShieldAbility);
    public static final FullSetDamagedAbility GOLEMS_HEART = new FullSetDamagedAbility(new AbilitySettings()
            .setId("golems_heart")
            .setPassive(true)
            .setManaCost(0)
            .setCooldown(0)
            .setSlot(Slot.ARMOR), FullSetDamagedAbility::golemsHeartAbility, List.of(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION));

    public static final FullSetDamagedAbility TITANS_HEART = new FullSetDamagedAbility(new AbilitySettings()
.setId("titans_heart")
            .setPassive(true)
            .setManaCost(0)
            .setCooldown(0)
            .setSlot(Slot.ARMOR), FullSetDamagedAbility::titansHeartAbility, List.of(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION));
    public static final ItemAbility CRYSTAL_SPIKE = new ItemAbility(new AbilitySettings()
            .setId("crystal_spike")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(75)
            .setCooldown(1), ItemAbility::crystalSpikeAbility);
    public static final ItemAbility MANA_BOOST = new ItemAbility(new AbilitySettings()
            .setId("mana_boost")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(0)
            .setCooldown(0), ItemAbility::manaBoostAbility);
    public static final ItemAbility COLOSSAL_SWEEP = new ItemAbility(new AbilitySettings()
            .setId("colossal_sweep")
            .setAbilityAction(ItemAbility.AbilityAction.RIGHT_CLICK_ALL)
            .setManaCost(100)
            .setCooldown(0), ItemAbility::colossalSweepAbility);


    public static void registerAbilities(){
        Util.logToConsole("Registering %s abilities.".formatted(ChatColor.GOLD + "" + Ability.registeredAbilities.size() + "" + ChatColor.RESET));
    }
}
