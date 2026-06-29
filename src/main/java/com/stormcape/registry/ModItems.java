package com.stormcape.registry;

import com.stormcape.StormCapeMod;
import com.stormcape.StormStaffItem;
import java.util.Map;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers the Storm Cape item. */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StormCapeMod.MOD_ID);

    /**
     * Equipment-asset key for the cape. We MUST set an asset so the client populates
     * {@code chestEquipment} in the render state (it only does so for equippables with an asset) —
     * that's what our {@link com.stormcape.client.StormCapeWingsLayer} reads. The asset's JSON
     * (assets/stormcape/equipment/storm_cape.json) is intentionally EMPTY so vanilla draws nothing;
     * our layer draws the 1.5x electric wings instead.
     */
    public static final ResourceKey<EquipmentAsset> STORM_CAPE_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID, StormCapeMod.id("storm_cape"));

    /** Equipment asset for the helmet's worn (armor) texture. The yellow lightning "horns" are
     *  extra 3D geometry drawn by {@link com.stormcape.client.StormHelmetHornsLayer}. */
    public static final ResourceKey<EquipmentAsset> STORM_HELMET_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID, StormCapeMod.id("storm_helmet"));

    /** Custom armor material for the helmet (iron-ish protection, our own texture). */
    public static final ArmorMaterial STORM_ARMOR = new ArmorMaterial(
            220,                                  // base durability
            Map.of(ArmorType.HELMET, 2),          // 2 armor points (like iron helmet)
            12,                                    // enchantment value
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,                                  // toughness
            0.0F,                                  // knockback resistance
            Tags.Items.INGOTS_COPPER,             // repair with copper
            STORM_HELMET_ASSET);

    // Elytra-like cape: GLIDER makes it glide; EQUIPPABLE (chest slot) makes it wearable.
    public static final DeferredItem<Item> STORM_CAPE = ITEMS.registerItem(
            "storm_cape",
            Item::new,
            () -> new Item.Properties()
                    .durability(600)
                    .rarity(Rarity.EPIC)
                    .component(DataComponents.GLIDER, Unit.INSTANCE)
                    .component(
                            DataComponents.EQUIPPABLE,
                            Equippable.builder(EquipmentSlot.CHEST)
                                    .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA)
                                    .setAsset(STORM_CAPE_ASSET)
                                    .setDamageOnHurt(false)
                                    .build()));

    // The Storm Staff: right-click boosts you (see StormStaffItem), left-click casts lightning
    // (see com.stormcape.net.StormNetwork). One per stack.
    public static final DeferredItem<Item> STORM_STAFF = ITEMS.registerItem(
            "storm_staff",
            StormStaffItem::new,
            () -> new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    // The Storm Helmet: standard head armor with our black/blue texture; the yellow lightning horns
    // are rendered by a custom layer (StormHelmetHornsLayer).
    public static final DeferredItem<Item> STORM_HELMET = ITEMS.registerItem(
            "storm_helmet",
            Item::new,
            () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(STORM_ARMOR, ArmorType.HELMET));

    private ModItems() {}

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
