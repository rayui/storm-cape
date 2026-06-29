package com.stormcape;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * The Storm Staff. Right-click = a firework-style speed boost (stronger while gliding) with blue
 * and yellow sparks trailing behind. Left-click = shoot lightning (handled via a network packet in
 * {@link com.stormcape.net.StormNetwork}, since left-click needs a client→server message).
 */
public class StormStaffItem extends Item {
    private static final int BOOST_COOLDOWN_TICKS = 40; // 2s between boosts
    private static final int YELLOW = 0xFFE24A;

    public StormStaffItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }

        Vec3 look = player.getLookAngle();
        boolean gliding = player.isFallFlying();
        // Strong forward shove; an extra kick when you're already gliding with the cape.
        double power = gliding ? 2.4 : 1.1;
        Vec3 boost = look.scale(power).add(0.0, gliding ? 0.2 : 0.35, 0.0);

        if (!level.isClientSide()) {
            player.setDeltaMovement(player.getDeltaMovement().add(boost));
            player.hurtMarked = true; // makes the server sync the new velocity to the client
            player.getCooldowns().addCooldown(stack, BOOST_COOLDOWN_TICKS);
            player.resetFallDistance();

            ServerLevel server = (ServerLevel) level;
            Vec3 behind = player.position().subtract(look.scale(0.6)).add(0.0, player.getBbHeight() * 0.5, 0.0);
            DustParticleOptions yellow = new DustParticleOptions(YELLOW, 1.4F);
            server.sendParticles(yellow, behind.x, behind.y, behind.z, 12, 0.25, 0.25, 0.25, 0.05);
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK, behind.x, behind.y, behind.z, 12, 0.25, 0.25, 0.25, 0.2);
            server.sendParticles(ParticleTypes.FIREWORK, behind.x, behind.y, behind.z, 8, 0.2, 0.2, 0.2, 0.1);
            server.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 0.8F, 1.2F);
        }

        return InteractionResult.SUCCESS;
    }
}
