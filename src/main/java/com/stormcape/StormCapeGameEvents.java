package com.stormcape;

import com.stormcape.registry.ModItems;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Per-tick Storm Cape behaviors (game bus): blue soul aura, electric sparks while gliding, and
 * shocking any living entity that touches the wearer.
 */
@EventBusSubscriber(modid = StormCapeMod.MOD_ID)
public final class StormCapeGameEvents {
    private static final float SHOCK_DAMAGE = 4.0F;          // 2 hearts
    private static final int SHOCK_COOLDOWN_TICKS = 15;      // per victim, ~0.75s
    private static final double TOUCH_RANGE = 0.35;          // how close counts as "touching"

    /** victim entity id -> game time of last shock, so we don't zap every tick. */
    private static final Map<Integer, Long> LAST_SHOCK = new HashMap<>();

    private StormCapeGameEvents() {}

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player wearer = event.getEntity();
        if (!wearer.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.STORM_CAPE.get())) {
            return;
        }
        if (!(wearer.level() instanceof ServerLevel level)) {
            return; // run everything server-side; sendParticles broadcasts to all nearby clients
        }

        spawnAura(level, wearer);
        if (wearer.isFallFlying()) {
            spawnFlightSparks(level, wearer);
        }
        shockNearbyEntities(level, wearer);
    }

    /** Blue soul-torch glow swirling around the wearer. */
    private static void spawnAura(ServerLevel level, Player wearer) {
        if (wearer.tickCount % 3 == 0) {
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    wearer.getX(), wearer.getY() + wearer.getBbHeight() * 0.5, wearer.getZ(),
                    2, 0.35, 0.5, 0.35, 0.0);
        }
    }

    /** Crackling electric sparks trailing the wearer while gliding. */
    private static void spawnFlightSparks(ServerLevel level, Player wearer) {
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                wearer.getX(), wearer.getY() + wearer.getBbHeight() * 0.6, wearer.getZ(),
                4, 0.4, 0.4, 0.4, 0.2);
    }

    /** Electrocute any living entity (mob or player) touching the wearer. */
    private static void shockNearbyEntities(ServerLevel level, Player wearer) {
        AABB area = wearer.getBoundingBox().inflate(TOUCH_RANGE);
        List<net.minecraft.world.entity.Entity> touching =
                level.getEntities(wearer, area, e -> e instanceof LivingEntity && e.isAlive());
        if (touching.isEmpty()) {
            return;
        }

        long now = level.getGameTime();
        if (LAST_SHOCK.size() > 512) {
            LAST_SHOCK.entrySet().removeIf(e -> now - e.getValue() > SHOCK_COOLDOWN_TICKS);
        }

        for (net.minecraft.world.entity.Entity entity : touching) {
            Long last = LAST_SHOCK.get(entity.getId());
            if (last != null && now - last < SHOCK_COOLDOWN_TICKS) {
                continue;
            }
            LAST_SHOCK.put(entity.getId(), now);

            entity.hurtOrSimulate(level.damageSources().lightningBolt(), SHOCK_DAMAGE);
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                    10, 0.3, 0.4, 0.3, 0.4);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.5F, 1.6F);
        }
    }
}
