package com.stormcape.registry;

import com.stormcape.StormCapeMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers the Storm Cape item. */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StormCapeMod.MOD_ID);

    // Elytra-like cape: GLIDER makes it glide; EQUIPPABLE (chest slot, no asset) makes it wearable
    // without vanilla drawing wings — our own StormCapeWingsLayer renders the 1.5x electric cape.
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
                                    .setDamageOnHurt(false)
                                    .build()));

    private ModItems() {}

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
