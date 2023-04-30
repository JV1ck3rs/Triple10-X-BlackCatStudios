package com.mygdx.game.Stations;

import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.Item;


/**
 * This is a trash can to remove unwanted items
 * BlackCatStudio's Code
 * @author Jack Hinton
 */
public class TrashCan extends Scriptable implements Interactable {


    @Override
    public boolean GiveItem(Item item){
        return true;
    }


    @Override
    public Item RetrieveItem(){
        return null;
    }


    @Override
    public boolean CanRetrieve(){
        return false;
    }


    @Override
    public boolean CanGive(){
        return true;
    }


    @Override
    public boolean CanInteract() {
        return false;
    }


    @Override
    public float Interact() {
        return 0;
    }


}
