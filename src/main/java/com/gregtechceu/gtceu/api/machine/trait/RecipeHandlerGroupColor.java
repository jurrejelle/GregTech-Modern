package com.gregtechceu.gtceu.api.machine.trait;

public record RecipeHandlerGroupColor(int color) implements RecipeHandlerGroup {

    // Note: An un-dyed hatch is the same as an "indistinct" hatch.
    public static RecipeHandlerGroup UNDYED = new RecipeHandlerGroupColor(-1);
}
