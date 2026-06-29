package com.stormcape.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stormcape.StormCapeMod;
import com.stormcape.registry.ModItems;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

/**
 * Renders the Storm Cape as an elytra model scaled to 1.5x (50% bigger), using our electric
 * texture. We render this ourselves (instead of the vanilla wings layer) because vanilla can't
 * scale the wings — the cape's Equippable deliberately has no asset so vanilla skips it.
 */
public class StormCapeWingsLayer<S extends HumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private static final Identifier TEXTURE = StormCapeMod.id("textures/entity/equipment/wings/storm_cape.png");
    private static final float SCALE = 1.5F;

    private final ElytraModel capeModel;

    public StormCapeWingsLayer(RenderLayerParent<S, M> parent, EntityModelSet models) {
        super(parent);
        this.capeModel = new ElytraModel(models.bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int packedLight, S state, float yRot, float xRot) {
        if (!state.chestEquipment.is(ModItems.STORM_CAPE.get())) {
            return;
        }
        this.capeModel.setupAnim(state);
        pose.pushPose();
        pose.translate(0.0F, 0.0F, 0.125F);    // sit just behind the body, like the vanilla elytra
        pose.scale(SCALE, SCALE, SCALE);        // 50% bigger; wings pivot at y=0 so they stay on the back
        renderColoredCutoutModel(this.capeModel, TEXTURE, pose, collector, packedLight, state, -1, 0);
        pose.popPose();
    }
}
