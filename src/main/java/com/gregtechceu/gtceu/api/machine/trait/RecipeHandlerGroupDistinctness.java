package com.gregtechceu.gtceu.api.machine.trait;

import java.util.Objects;

public class RecipeHandlerGroupDistinctness implements RecipeHandlerGroup{

    public boolean isDistinct;

    public RecipeHandlerGroupDistinctness(boolean isDistinct){
        this.isDistinct = isDistinct;
    }

    public static RecipeHandlerGroup BUS_DISTINCT = new RecipeHandlerGroupDistinctness(true);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecipeHandlerGroupDistinctness that = (RecipeHandlerGroupDistinctness) o;
        return isDistinct == that.isDistinct;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isDistinct);
    }
}
