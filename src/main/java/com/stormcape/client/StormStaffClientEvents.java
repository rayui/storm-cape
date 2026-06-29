package com.stormcape.client;

import com.stormcape.StormCapeMod;
import com.stormcape.net.StormNetwork;
import com.stormcape.registry.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Client-side left-click detection for the Storm Staff. Left-click is client-only input; when the
 * player swings the staff (at air or at a block) we send a packet so the server casts lightning.
 */
@EventBusSubscriber(modid = StormCapeMod.MOD_ID, value = Dist.CLIENT)
public final class StormStaffClientEvents {
    private StormStaffClientEvents() {}

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        tryCast(event.getEntity());
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (holdingStaff(event.getEntity())) {
            event.setCanceled(true); // don't start breaking the block; cast instead
            tryCast(event.getEntity());
        }
    }

    private static boolean holdingStaff(Player player) {
        return player.getMainHandItem().is(ModItems.STORM_STAFF.get());
    }

    private static void tryCast(Player player) {
        if (holdingStaff(player)) {
            ClientPacketDistributor.sendToServer(new StormNetwork.CastLightning());
        }
    }
}
