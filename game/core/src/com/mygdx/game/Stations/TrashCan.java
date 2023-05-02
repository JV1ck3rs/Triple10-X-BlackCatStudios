package com.mygdx.game.Stations;

import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.Item;


/**
 * This is a trash can to remove unwanted items
 * BlackCatStudio's Code
 * @author Jack Hinton
 * @date 29/04/23
 */
public class TrashCan extends Scriptable implements Interactable {


    /**
     * Give an item to the trashcan
     * @param item Item you want to give
     * @return boolean
     * @Author Jack Hinton
     */
    @Override
    public boolean GiveItem(Item item){
        return true;
    }


    /**
     * Retrieve an item from the trashcan
     * @return Item
     * @Author Jack Hinton
     */
    @Override
    public Item RetrieveItem(){
        return null;
    }


    /**
     * Check if you can retrieve from the trash can
     * @return boolean
     * @Author Jack Hinton
     */
    @Override
    public boolean CanRetrieve(){
        return false;
    }


    /**
     * Check if you can give an item to the trash can
     * @return boolean
     * @Author Jack Hinton
     */
    @Override
    public boolean CanGive(){
        return true;
    }


    /**
     * Check if you can interact with the trash can
     * @return boolean
     * @Author Jack Hinton
     */
    @Override
    public boolean CanInteract() {
        return false;
    }


    /**
     * Interact with the trash can
     * @return float
     * @Author Jack Hinton
     */
    @Override
    public float Interact() {
        return 0;
    }


}
