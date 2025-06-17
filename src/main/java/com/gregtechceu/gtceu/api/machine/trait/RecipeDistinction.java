package com.gregtechceu.gtceu.api.machine.trait;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.MapColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RecipeDistinction {
    // IMPORTANT: Keep the first 16 in the same order as the vanilla DyeColors
    // for the colorToDistinction map
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK,
    INDISTINCT,
    BUS_DISTINCT,;

    static Map<Integer, RecipeDistinction> colorToDistinction = new HashMap();
    static {
        DyeColor[] dyeValues = DyeColor.values();
        RecipeDistinction[] distributionValues = values();
        for(int i=0;i<16;i++){
            colorToDistinction.put(dyeValues[i].getTextColor(), distributionValues[i]);
        }
    }

    public static RecipeDistinction fromColor(int color){
        return colorToDistinction.getOrDefault(color, INDISTINCT);
    }

    // First bus-distincts, then all the colors in order, then indistinct;
    public static List<RecipeDistinction> order = Arrays.asList(BUS_DISTINCT,
            WHITE,
            ORANGE,
            MAGENTA,
            LIGHT_BLUE,
            YELLOW,
            LIME,
            PINK,
            GRAY,
            LIGHT_GRAY,
            CYAN,
            PURPLE,
            BLUE,
            BROWN,
            GREEN,
            RED,
            BLACK,
            INDISTINCT);

}
