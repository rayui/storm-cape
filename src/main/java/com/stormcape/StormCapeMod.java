package com.stormcape;

import com.stormcape.registry.ModItems;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Storm Cape — an elytra-like cape that is 50% bigger than a normal elytra, sparks with
 * electricity when you glide, bathes the wearer in blue soul light, and electrocutes any
 * mob or player that touches the wearer.
 *
 * <p>The cape does not set an Equippable "asset", so vanilla's wings layer ignores it; instead
 * {@code StormCapeWingsLayer} renders an elytra model scaled to 1.5x with our electric texture.</p>
 */
@Mod(StormCapeMod.MOD_ID)
public class StormCapeMod {
    public static final String MOD_ID = "stormcape";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public StormCapeMod(IEventBus modEventBus) {
        ModItems.register(modEventBus);
    }
}
