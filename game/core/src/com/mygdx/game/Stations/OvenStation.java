package com.mygdx.game.Stations;

import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import java.util.ArrayList;
import java.util.Arrays;

public class OvenStation extends Station {

    boolean interacted;
    boolean ready;
    public float maxProgress;
    public static ArrayList<ItemEnum> ItemWhiteList;


    public OvenStation() {
        ready = false;
        maxProgress = 10;
        if (ItemWhiteList.isEmpty()) {
            ItemWhiteList = new ArrayList<ItemEnum>(Arrays.<ItemEnum>asList());
        }
    }


    @Override
    public boolean GiveItem(Item item) {
        if (this.item != null)
            return false;
        changeItem(item);
        checkItem();
        return true;
    }


    @Override
    public Item RetrieveItem() {
        if (item != null) {
            returnItem = item;
            deleteItem();
            currentRecipe = null;
            return returnItem;
        }
        return null;
    }


    public boolean CanRetrieve() {
        return true;
    }


    public boolean CanGive() {
        return true;
    }


    public void checkItem() {
        if (ItemWhiteList.contains(item.name))
            currentRecipe = recipes.RecipeMap.get(item.name);
        else
            currentRecipe = null;
    }

    public void burnItem() {
        changeItem(new Item(ItemEnum.Cinder));
    }

    public void Cook(float dt) {
        ready = currentRecipe.RecipeSteps.get(item.step).timeStep(item, dt, interacted, maxProgress);

        if (ready) {
            changeItem(new Item(currentRecipe.endItem));
            checkItem();
        }

    }
}