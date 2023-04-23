package com.mygdx.game.Stations;

import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.GameState.CookingParams;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Bakes potatoes and pizzas
 */
public class OvenStation extends Station {

    boolean interacted;
    boolean ready;
    public float maxProgress;
    public float progress;
    public static ArrayList<ItemEnum> ItemWhiteList;


    public OvenStation(CookingParams params) {
        super(params);
        ready = false;
        maxProgress = 10;
        if (ItemWhiteList == null) {
            ItemWhiteList = new ArrayList<>(Arrays.asList(ItemEnum.Potato, ItemEnum.CheesePotato, ItemEnum.MeatPotato,
                    ItemEnum.CheesePizza, ItemEnum.MeatPizza, ItemEnum.VegPizza, ItemEnum.CheesePizzaCooked,
                    ItemEnum.MeatPizzaCooked, ItemEnum.VegPizzaCooked, ItemEnum.BakedPotato, ItemEnum.CheeseBake,
                    ItemEnum.MeatBake));
        }
    }


    @Override
    public boolean GiveItem(Item item) {
        bubble.isVisible = true;
        changeItem(item);
        checkItem();
        return true;
    }


    @Override
    public Item RetrieveItem() {
        bubble.isVisible = false;
        returnItem = item;
        deleteItem();
        currentRecipe = null;
        return returnItem;
    }


    @Override
    public boolean CanRetrieve() {
        return item != null;
    }


    @Override
    public boolean CanGive() {
        return item == null;
    }


    @Override
    public boolean CanInteract(){
        return false;
    }


    @Override
    public boolean Interact() {
        return false;
    }


    public void checkItem() {
        if (ItemWhiteList.contains(item.name))
            currentRecipe = recipes.RecipeMap.get(item.name);
        else
            currentRecipe = null;
    }


    public void Cook(float dt) {
        ready = currentRecipe.RecipeSteps.get(item.step).timeStep(item, dt-stationTimeDecrease, interacted, maxProgress);
        if (ready) {
            changeItem(new Item(currentRecipe.endItem));
            checkItem();
            return;
        }
        progressBar();
    }


    public void progressBar(){
        bubble.image = new BlackTexture("Timer/0"+getProgress()+".png");
    }


    public int getProgress() {
        progress = item.progress / maxProgress;
        return (int) (progress/0.125) + 1;
    }


    @Override
    public void updatePictures() {
        return;
    }

    @Override
    public void Update(float dt) {
        if (currentRecipe != null) {
            Cook(dt);
        }
    }
}