package com.gregtechceu.gtceu.common.network.packets.ui;

import com.gregtechceu.gtceu.client.mui.screen.ModularContainerMenu;
import com.gregtechceu.gtceu.client.mui.screen.ModularScreen;
import com.gregtechceu.gtceu.utils.NetworkUtils;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class SyncHandlerPacket implements IPacket {

    private String panel;
    private String key;
    private FriendlyByteBuf packet;

    @Override
    public void encode(FriendlyByteBuf buf) {
        NetworkUtils.writeStringSafe(buf, this.panel);
        NetworkUtils.writeStringSafe(buf, this.key, 64, true);
        NetworkUtils.writeByteBuf(buf, this.packet);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.panel = NetworkUtils.readStringSafe(buf);
        this.key = NetworkUtils.readStringSafe(buf);
        this.packet = NetworkUtils.readFriendlyByteBuf(buf);
    }

    @Override
    public void execute(IHandlerContext handler) {
        if (handler.isClient()) {
            ModularScreen screen = ModularScreen.getCurrent();
            if (screen != null) {
                screen.getSyncManager().receiveWidgetUpdate(this.panel, this.key, this.packet.readVarInt(),
                        this.packet);
            }
        } else {

            AbstractContainerMenu menu = handler.getPlayer().containerMenu;
            if (menu instanceof ModularContainerMenu modularMenu) {
                modularMenu.getSyncManager()
                        .receiveWidgetUpdate(this.panel, this.key, this.packet.readVarInt(), this.packet);
            }
        }
    }
}
