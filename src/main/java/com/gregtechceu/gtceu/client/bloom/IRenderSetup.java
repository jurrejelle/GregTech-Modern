package com.gregtechceu.gtceu.client.bloom;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IRenderSetup {

    /**
     * Run any pre render gl code here.
     *
     * @param buffer Buffer builder
     */
    @OnlyIn(Dist.CLIENT)
    void preDraw(BufferBuilder buffer);

    /**
     * Run any post render gl code here.
     *
     * @param buffer Buffer builder
     */
    @OnlyIn(Dist.CLIENT)
    void postDraw(BufferBuilder buffer);
}
