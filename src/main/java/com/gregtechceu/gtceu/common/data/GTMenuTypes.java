package com.gregtechceu.gtceu.common.data;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import brachy.modularui.screen.ModularContainerMenu;

public class GTMenuTypes {

    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU,
            GTCEu.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ModularContainerMenu>> MODULAR_CONTAINER = MENU_TYPES
            .register(
                    "modular",
                    () -> new MenuType<>((i, inv) -> new ModularContainerMenu(i), FeatureFlagSet.of()));

    public static void init(IEventBus modBus) {
        MENU_TYPES.register(modBus);
    }
}
