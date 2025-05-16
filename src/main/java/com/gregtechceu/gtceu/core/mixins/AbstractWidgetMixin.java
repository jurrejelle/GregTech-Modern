package com.gregtechceu.gtceu.core.mixins;

import com.gregtechceu.gtceu.api.mui.overlay.OverlayStack;

import net.minecraft.client.gui.components.AbstractWidget;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * This mixin fixes some visual bugs that can happen with overlays.
 */
@Mixin(AbstractWidget.class)
public abstract class AbstractWidgetMixin {

    @ModifyExpressionValue(method = "render",
                           at = @At(value = "FIELD",
                                    opcode = Opcodes.PUTFIELD,
                                    target = "Lnet/minecraft/client/gui/components/AbstractWidget;isHovered:Z"))
    public boolean gtceu$fixHoveredState(boolean original) {
        // fixes buttons being hovered when an overlay element is already hovered
        return original && !OverlayStack.isHoveringOverlay();
    }
}
