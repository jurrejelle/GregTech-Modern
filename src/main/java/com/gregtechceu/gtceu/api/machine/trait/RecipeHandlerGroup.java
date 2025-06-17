package com.gregtechceu.gtceu.api.machine.trait;

import java.util.Objects;

public record RecipeHandlerGroup (Object group) {
    public static RecipeHandlerGroup BUS_DISTINCT = new RecipeHandlerGroup("bus_distinct");
    public static RecipeHandlerGroup INDISTINCT = new RecipeHandlerGroup("indistinct");
    public boolean equals(RecipeHandlerGroup other){
        return Objects.equals(this, other);
    }
}
