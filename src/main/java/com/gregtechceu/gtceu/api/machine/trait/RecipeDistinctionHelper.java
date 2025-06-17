package com.gregtechceu.gtceu.api.machine.trait;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.gregtechceu.gtceu.api.machine.trait.RecipeDistinction.*;

public class RecipeDistinctionHelper implements Iterable<Pair<RecipeDistinction, List<RecipeHandlerList>>> , Iterator<Pair<RecipeDistinction, List<RecipeHandlerList>>> {
    Map<RecipeDistinction, List<RecipeHandlerList>> map;
    Map<RecipeDistinction, Boolean> alreadyReturned;
    public RecipeDistinctionHelper(){
        map = new HashMap<>();
        alreadyReturned = new HashMap<>();
    }

    public void add(RecipeHandlerList handler){
        if(handler.isDistinct()){
            addWithDefault(BUS_DISTINCT, handler);
            return;
        }
        RecipeDistinction handlerDistinction = RecipeDistinction.fromColor(handler.getColor());
        addWithDefault(handlerDistinction, handler);
    }

    public void addWithDefault(RecipeDistinction key, RecipeHandlerList value){
        if(!map.containsKey(key)) map.put(key, new ArrayList<>());
        map.get(key).add(value);
    }


    @Override
    public boolean hasNext() {
        return alreadyReturned.size() != map.size();
    }

    @Override
    public Pair<RecipeDistinction, List<RecipeHandlerList>> next() {
        // Return the lists in the correct order
        List<RecipeHandlerList> indistinct_handlers = map.getOrDefault(INDISTINCT, null);
        for(RecipeDistinction distinction : RecipeDistinction.order){
            if(!map.containsKey(distinction)) continue;
            if(!alreadyReturned.getOrDefault(distinction, false)){
                alreadyReturned.put(distinction, true);
                List<RecipeHandlerList> listToReturn = map.get(distinction);
                // If our return list is a color and we have a list of indistinct handlers
                if(distinction != INDISTINCT && distinction != BUS_DISTINCT && indistinct_handlers != null) {
                    listToReturn.addAll(indistinct_handlers);
                }
                return Pair.of(distinction, listToReturn);
            }
        }
        throw new RuntimeException("RecipeDistinctionHelper.next() was called when all elements were already yielded.");
    }

    @Override
    public @NotNull Iterator<Pair<RecipeDistinction, List<RecipeHandlerList>>> iterator() {
        return this;
    }
}
