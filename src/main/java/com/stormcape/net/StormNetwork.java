package com.stormcape.net;

import com.stormcape.StormCapeMod;
import com.stormcape.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Networking for the Storm Staff's left-click lightning. Left-click is a client-only input, so the
 * client sends this (empty) payload and the server strikes lightning at the ground the player aims at.
 */
@EventBusSubscriber(modid = StormCapeMod.MOD_ID)
public final class StormNetwork {
    private static final int LIGHTNING_COOLDOWN_TICKS = 30; // 1.5s between bolts
    private static final double REACH = 24.0;

    private StormNetwork() {}

    /** Empty marker payload: "I left-clicked with the Storm Staff." */
    public record CastLightning() implements CustomPacketPayload {
        public static final Type<CastLightning> TYPE = new Type<>(StormCapeMod.id("cast_lightning"));
        public static final StreamCodec<Object, CastLightning> CODEC = StreamCodec.unit(new CastLightning());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(CastLightning.TYPE, CastLightning.CODEC, StormNetwork::handleCastLightning);
    }

    private static void handleCastLightning(CastLightning payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }
        // enqueueWork hops to the main server thread before touching the world.
        context.enqueueWork(() -> {
            ItemStack stack = player.getMainHandItem();
            if (!stack.is(ModItems.STORM_STAFF.get()) || player.getCooldowns().isOnCooldown(stack)) {
                return;
            }
            if (!(player.level() instanceof ServerLevel level)) {
                return;
            }

            // Aim a ray from the eyes; strike where it hits the ground (or the far point ahead).
            Vec3 eye = player.getEyePosition();
            Vec3 end = eye.add(player.getLookAngle().scale(REACH));
            BlockHitResult hit = level.clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
            Vec3 target = hit.getType() == HitResult.Type.BLOCK ? hit.getLocation() : end;

            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
            if (bolt != null) {
                bolt.snapTo(target.x, target.y, target.z);
                bolt.setCause(player);
                level.addFreshEntity(bolt);
            }
            player.getCooldowns().addCooldown(stack, LIGHTNING_COOLDOWN_TICKS);
            player.swing(InteractionHand.MAIN_HAND, true);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 0.6F, 1.0F);
        });
    }
}
