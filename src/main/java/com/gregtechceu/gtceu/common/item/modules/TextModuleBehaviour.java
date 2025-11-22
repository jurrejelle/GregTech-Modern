package com.gregtechceu.gtceu.common.item.modules;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.component.IMonitorModuleItem;
import com.gregtechceu.gtceu.api.placeholder.MultiLineComponent;
import com.gregtechceu.gtceu.api.placeholder.PlaceholderContext;
import com.gregtechceu.gtceu.api.placeholder.PlaceholderHandler;
import com.gregtechceu.gtceu.client.renderer.monitor.IMonitorRenderer;
import com.gregtechceu.gtceu.client.renderer.monitor.MonitorTextRenderer;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.CentralMonitorMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.monitor.MonitorGroup;
import com.gregtechceu.gtceu.common.network.GTNetwork;
import com.gregtechceu.gtceu.common.network.packets.SCPacketMonitorGroupNBTChange;
import com.gregtechceu.gtceu.data.item.GTDataComponents;

import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.codeeditor.CodeEditorWidget;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TextModuleBehaviour implements IMonitorModuleItem {

    private void updateText(ItemStack stack, CentralMonitorMachine machine, MonitorGroup group) {
        StringBuilder formatStringLines = new StringBuilder();
        List<String> tag = stack.get(GTDataComponents.TEXT_MODULE_STRING_LINES);
        for (String value : tag) {
            formatStringLines.append(value).append('\n');
        }
        MultiLineComponent text = PlaceholderHandler.processPlaceholders(
                formatStringLines.toString(),
                new PlaceholderContext(
                        machine.getLevel(),
                        group.getTarget(machine.getLevel()),
                        group.getTargetCoverSide(),
                        group.getPlaceholderSlotsHandler(),
                        group.getTargetCover(machine.getLevel()),
                        null));
        stack.set(GTDataComponents.TEXT_MODULE_TEXT, text.toImmutable());
    }

    @Override
    public void tick(ItemStack stack, CentralMonitorMachine machine, MonitorGroup group) {
        this.updateText(stack, machine, group);
    }

    @Override
    public IMonitorRenderer getRenderer(ItemStack stack, CentralMonitorMachine machine, MonitorGroup group) {
        return new MonitorTextRenderer(
                stack.getOrDefault(GTDataComponents.TEXT_MODULE_TEXT, List.<Component>of()),
                Math.max(stack.getOrDefault(GTDataComponents.TEXT_MODULE_SCALE, 1.0).doubleValue(), .0001));
    }

    @Override
    public Widget createUIWidget(ItemStack stack, CentralMonitorMachine machine, MonitorGroup group) {
        WidgetGroup builder = new WidgetGroup();
        CodeEditorWidget editor = new CodeEditorWidget(0, 0, 120, 80);
        TextFieldWidget scaleInput = new TextFieldWidget(
                -50, 47,
                40, 10,
                null,
                null);
        ButtonWidget saveButton = new ButtonWidget(-40, 22, 20, 20, click -> {
            if (!click.isRemote) return;
            stack.set(GTDataComponents.TEXT_MODULE_STRING_LINES, editor.getLines());
            try {
                stack.set(GTDataComponents.TEXT_MODULE_SCALE, Double.parseDouble(scaleInput.getCurrentString()));
            } catch (NumberFormatException ignored) {}
            GTNetwork.sendToServer(new SCPacketMonitorGroupNBTChange(stack, group, machine));
        });
        saveButton.setButtonTexture(GuiTextures.BUTTON_CHECK);
        List<Boolean> tmp = new ArrayList<>();
        Supplier<String> scaleInputSupplier = () -> {
            if (tmp.isEmpty()) tmp.add(true);
            else scaleInput.setTextSupplier(null);
            if (!stack.has(GTDataComponents.TEXT_MODULE_SCALE)) {
                stack.set(GTDataComponents.TEXT_MODULE_SCALE, 1.0);
                GTNetwork.sendToServer(new SCPacketMonitorGroupNBTChange(stack, group, machine));
                return "1";
            }
            return String.valueOf(Mth.clamp(stack.get(GTDataComponents.TEXT_MODULE_SCALE), .0001, 1000));
        };
        scaleInput.setTextSupplier(scaleInputSupplier);
        scaleInput.setHoverTooltips(Component.translatable("gtceu.gui.central_monitor.text_scale"));
        List<String> formatStringLines = stack.getOrDefault(GTDataComponents.TEXT_MODULE_STRING_LINES, List.of());
        editor.setLines(formatStringLines);
        builder.addWidget(editor);
        builder.addWidget(saveButton);
        Widget placeholderReference = PlaceholderHandler.getPlaceholderHandlerUI("");
        builder.addWidget(scaleInput);
        placeholderReference.setSelfPosition(-100, -50);
        builder.addWidget(placeholderReference);
        return builder;
    }
}
