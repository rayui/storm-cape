package com.stormcape;

import com.stormcape.registry.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/** Mod-bus events: put the Storm Cape in the creative menu. */
@EventBusSubscriber(modid = StormCapeMod.MOD_ID)
public final class StormCapeModEvents {
    private StormCapeModEvents() {}

    @SubscribeEvent
    public static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.STORM_CAPE.get());
            event.accept(ModItems.STORM_STAFF.get());
        }
    }
}
