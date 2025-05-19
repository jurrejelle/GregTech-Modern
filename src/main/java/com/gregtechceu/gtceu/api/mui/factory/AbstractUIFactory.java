package com.gregtechceu.gtceu.api.mui.factory;

import com.gregtechceu.gtceu.api.mui.base.IUIHolder;
import com.gregtechceu.gtceu.api.mui.base.UIFactory;
import com.gregtechceu.gtceu.api.mui.value.sync.PanelSyncManager;
import com.gregtechceu.gtceu.client.mui.screen.ModularPanel;
import com.gregtechceu.gtceu.client.mui.screen.ModularScreen;
import com.gregtechceu.gtceu.client.mui.screen.UISettings;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractUIFactory<T extends GuiData> implements UIFactory<T> {

    private final ResourceLocation name;

    protected AbstractUIFactory(ResourceLocation name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public final @NotNull ResourceLocation getFactoryName() {
        return this.name;
    }

    @NotNull
    public abstract IUIHolder<T> getGuiHolder(T data);

    @Override
    public ModularPanel createPanel(T guiData, PanelSyncManager syncManager, UISettings settings) {
        IUIHolder<T> guiHolder = Objects.requireNonNull(getGuiHolder(guiData), "Gui holder must not be null!");
        return guiHolder.buildUI(guiData, syncManager, settings);
    }

    @Override
    public ModularScreen createScreen(T guiData, ModularPanel mainPanel) {
        IUIHolder<T> guiHolder = Objects.requireNonNull(getGuiHolder(guiData), "Gui holder must not be null!");
        return guiHolder.createScreen(guiData, mainPanel);
    }

    @SuppressWarnings("unchecked")
    protected IUIHolder<T> castUIHolder(Object o) {
        if (!(o instanceof IUIHolder)) return null;
        try {
            return (IUIHolder<T>) o;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
