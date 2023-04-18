package com.mygdx.game.Core;

import com.badlogic.gdx.Game;

import java.util.HashMap;
import java.util.LinkedList;

public class PowerupPurchaseMenu {

    // Create objects for the power-up screen
    GameObject background = new GameObject(new BlackTexture(""));
    GameObject speedPowerUpButton = new GameObject(new BlackTexture(""));
    GameObject reputationPowerUpButton = new GameObject(new BlackTexture(""));
    GameObject staffPowerUpButton = new GameObject(new BlackTexture(""));
    GameObject cookSpeedPowerUpButton = new GameObject(new BlackTexture(""));
    GameObject closeMenuButton = new GameObject(new BlackTexture(""));


    public void showPowerUpMenu(){
        background.isVisible = true;


    }

    public void hidePowerMenu(){

    }
}
