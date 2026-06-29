package com.stormcape.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stormcape.StormCapeMod;
import com.stormcape.registry.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

/**
 * Draws the yellow lightning "horns" on top of the Storm Helmet, anchored to the head so they tilt
 * with the player's head movement.
 */
public class StormHelmetHornsLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {
    private static final Identifier TEXTURE = StormCapeMod.id("textures/entity/storm_horns/storm_horns.png");

    private final StormHornsModel horns;

    public StormHelmetHornsLayer(RenderLayerParent<S, M> parent, EntityModelSet models) {
        super(parent);
        this.horns = new StormHornsModel(models.bakeLayer(StormHornsModel.LAYER));
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int packedLight, S state, float yRot, float xRot) {
        if (!state.headEquipment.is(ModItems.STORM_HELMET.get())) {
            return;
        }
        pose.pushPose();
        this.getParentModel().head.translateAndRotate(pose); // move into head space (follows head tilt)
        renderColoredCutoutModel(this.horns, TEXTURE, pose, collector, packedLight, state, -1, 0);
        pose.popPose();
    }
}
