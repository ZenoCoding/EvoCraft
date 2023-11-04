package me.zenox.evocraft.abilities;

import me.zenox.evocraft.data.TranslatableList;
import me.zenox.evocraft.data.TranslatableText;
import me.zenox.evocraft.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class Modifier {

    private final String id;
    private final TranslatableText name;
    private final TranslatableList lore;

    public enum Type {
        MANA_COST {
            @Override
            Modifier create(String id, double value) {
                return new Modifier(id) {
                    @Override
                    void modify(ClassAbility ability) {
                        ability.setManaCost(ability.getManaCost() + (int) value);
                    }
                };
            }
        },
        COOLDOWN {
            @Override
            Modifier create(String id, double value) {
                return new Modifier(id) {
                    @Override
                    void modify(ClassAbility ability) {
                        ability.setCooldown(ability.getCooldown() + value);
                    }
                };
            }
        },
        EXECUTABLE {
            @Override
            Modifier create(String id, TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable) {
                return new Modifier(id) {
                    @Override
                    void modify(ClassAbility ability) {
                        ability.setExecutable(executable);
                    }
                };
            }
        },
        RANGE {
            @Override
            Modifier create(String id, double value) {
                return new Modifier(id) {
                    @Override
                    void modify(ClassAbility ability) {
                        ability.setRange(ability.getRange() + (int) value);
                    }
                };
            }
        },
        CHARGE {
            @Override
            Modifier create(String id, double value) {
                return new Modifier(id) {
                    @Override
                    void modify(ClassAbility ability) {
                        ability.setCharges(ability.getCharges() + (int) value);
                    }
                };
            }
        };

        Modifier create(String id, double value) {
            throw new UnsupportedOperationException("This operation is not supported for type: " + this);
        }

        Modifier create(String id, TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable) {
            throw new UnsupportedOperationException("This operation is not supported for type: " + this);
        }
    }

    private Modifier(String id) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.Type.MODIFIER_NAME + "-" + id);
        this.lore = new TranslatableList(TranslatableText.Type.MODIFIER_LORE + "-" + id);
    }

    public static Modifier of(Type type, String id, double value) {
        return type.create(id, value);
    }

    public static Modifier of(Type type, String id, TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable) {
        return type.create(id, executable);
    }

    abstract void modify(ClassAbility ability);

    public String id() {
        return id;
    }

    public TranslatableText name() {
        return name;
    }

    public TranslatableList lore() {
        return lore;
    }
}
