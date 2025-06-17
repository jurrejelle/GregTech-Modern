package com.gregtechceu.gtceu.api.machine.trait;

import java.util.Objects;

public class RecipeHandlerGroupColor implements RecipeHandlerGroup {

    // Note: An un-dyed hatch is the same as an "indistinct" hatch.
    public static RecipeHandlerGroup UNDYED = new RecipeHandlerGroupColor(-1);

    public int color;

    public RecipeHandlerGroupColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecipeHandlerGroupColor that = (RecipeHandlerGroupColor) o;
        return color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(color);
    }
}
