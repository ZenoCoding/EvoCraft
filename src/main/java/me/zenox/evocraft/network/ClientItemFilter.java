package me.zenox.evocraft.network;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class ClientItemFilter {

    public ClientItemFilter(JavaPlugin pluginInstance, ProtocolManager protocolManager) {
        PacketAdapter.AdapterParameteters params = PacketAdapter.params().plugin(pluginInstance)
                .listenerPriority(ListenerPriority.HIGH)
                .types(PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS);

        protocolManager.addPacketListener(new PacketAdapter(params) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                if (event.getPacketType() == PacketType.Play.Server.SET_SLOT && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    ItemStack itemStack = packet.getItemModifier().read(0);
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        filterItem(itemStack);
                    }
                } else if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                    for (ItemStack itemStack : itemStacks) {
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            filterItem(itemStack);
                        }
                    }
                }

                event.setPacket(packet);
            }
        });
    }

    abstract void filterItem(ItemStack itemStack);
}