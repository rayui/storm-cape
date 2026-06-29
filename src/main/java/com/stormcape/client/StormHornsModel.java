package com.stormcape.client;

import com.stormcape.StormCapeMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

/**
 * Two yellow lightning-bolt "horns" that sit on the front of the head and zigzag up-and-out.
 * Rendered in head space by {@link StormHelmetHornsLayer}; the whole texture is yellow so the
 * cubes' UVs don't need to be precise.
 */
public class StormHornsModel extends EntityModel<HumanoidRenderState> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(StormCapeMod.id("storm_horns"), "main");

    public StormHornsModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Each horn is three short segments stacked into a zig-zag. Anchored at the top-front of the
        // head (head cube top is y=-8, front face is z=-4) and tilted up-and-outward.
        addHorn(root, "right_horn", 3.5F, -7.5F, -3.5F, 0.35F);   // player's right
        addHorn(root, "left_horn", -3.5F, -7.5F, -3.5F, -0.35F);  // player's left (mirrored tilt)

        return LayerDefinition.create(mesh, 16, 16);
    }

    private static void addHorn(PartDefinition root, String name, float x, float y, float z, float outwardTilt) {
        // Zig-zag built from three 1x4x1 segments, each offset sideways to alternate direction.
        CubeListBuilder bolt = CubeListBuilder.create()
                .texOffs(0, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F)   // lower segment
                .texOffs(0, 0).addBox(0.5F, -7.0F, -0.5F, 1.0F, 4.0F, 1.0F)    // middle zag (out)
                .texOffs(0, 0).addBox(-0.5F, -10.0F, -0.5F, 1.0F, 4.0F, 1.0F); // upper zag (back)
        // xRot tilts it up/forward; zRot fans it outward (sign differs per side).
        root.addOrReplaceChild(name, bolt, PartPose.offsetAndRotation(x, y, z, -0.5F, 0.0F, outwardTilt));
    }

    @Override
    public void setupAnim(HumanoidRenderState state) {
        // Static horns — no animation.
    }
}
