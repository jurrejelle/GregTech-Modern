package com.gregtechceu.gtceu.integration.ldlib;

import org.apache.logging.log4j.LogManager;

import com.gregtechceu.gtceu.data.inject.GTSyncedFieldAccessors;

import com.lowdragmc.lowdraglib.plugin.ILDLibPlugin;
import com.lowdragmc.lowdraglib.plugin.LDLibPlugin;

@SuppressWarnings("unused")
@LDLibPlugin
public class GTLDLibPlugin implements ILDLibPlugin {

    @Override
    public void onLoad() {
        LogManager.getLogger().warn("LDLib plugin is loading!");
        GTSyncedFieldAccessors.init();
    }
}
