package com.stormcape.client;

import com.stormcape.StormCapeMod;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/** Client-only mod-bus events: add the Storm Cape wings layer to every player skin renderer. */
@EventBusSubscriber(modid = StormCapeMod.MOD_ID, value = Dist.CLIENT)
public final class StormCapeClientEvents {
    private StormCapeClientEvents() {}

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerModelType skin : event.getSkins()) {
            AvatarRenderer<AbstractClientPlayer> renderer = event.getPlayerRenderer(skin);
            if (renderer != null) {
                renderer.addLayer(new StormCapeWingsLayer<AvatarRenderState, PlayerModel>(renderer, event.getEntityModels()));
            }
        }
    }
}
