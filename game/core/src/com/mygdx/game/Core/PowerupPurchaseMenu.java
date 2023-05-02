package com.mygdx.game.Core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Items.ItemEnum;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This contains the code for the powerup purchase menu UI
 * BlackCatStudio's Code
 * @author Sam Toner
 * @date 02/05/23
 */
public class PowerupPurchaseMenu extends Scriptable {
  FreeTypeFontGenerator.FreeTypeFontParameter font;
  FreeTypeFontGenerator gen;
  BitmapFont fontBit;

  BlackTexture backgroundTexture = new BlackTexture("PowerupAssets/BackGround.png");
  BlackTexture speedPowerUpTexture = new BlackTexture("PowerupAssets/SpeedPowerUp.png");
  BlackTexture reputationPowerUpTexture = new BlackTexture("PowerupAssets/ReputationPowerUp.png");
  BlackTexture superFoodPowerupTexture = new BlackTexture("PowerupAssets/SuperFoodPowerUp.png");
  BlackTexture tetrisSuperFoodPowerupTexture = new BlackTexture("PowerupAssets/TetrisSuperFoodPowerup.png");
  BlackTexture frustrationSuperFoodPowerUpTexture = new BlackTexture("PowerupAssets/FrustrationPowerup.png");
  BlackTexture closeMenuTexture = new BlackTexture("Items/CloseButton.png");

  // Create objects for the power-up screen
  public GameObject background = new GameObject(backgroundTexture);
  GameObject speedPowerUpButton = new GameObject(speedPowerUpTexture);
  GameObject reputationPowerUpButton = new GameObject(reputationPowerUpTexture);
  GameObject superFoodPowerUpButton = new GameObject(superFoodPowerupTexture);
  GameObject tetrisSuperFoodPowerUpButton = new GameObject(tetrisSuperFoodPowerupTexture);
  GameObject stopFrustrationPowerUpButton = new GameObject(frustrationSuperFoodPowerUpTexture);
  GameObject closeMenuButton = new GameObject(closeMenuTexture);

  CustomerController cc;
  Powerup powerup;

  HashMap<String, Integer> prices = new HashMap<>();
  SpriteBatch sb;
  MasterChef mc;
  LinkedList<ItemEnum> completedRecipes;


  public PowerupPurchaseMenu(CustomerController cc, Powerup powerup, MasterChef mc) {
    this.cc = cc;
    this.powerup = powerup;

    prices.put("Speed", 50);
    prices.put("Reputation", 60);
    prices.put("SuperFood", 75);
    prices.put("TetrisSuperFood", 100);
    prices.put("Frustration", 75);

    font = new FreeTypeFontGenerator.FreeTypeFontParameter();
    font.size = 20;
    Color colour = new Color(0x5584AC);
    font.color = colour;
    font.shadowColor = Color.WHITE;
    font.shadowOffsetX = 3;
    font.shadowOffsetY = 3;
    gen = new FreeTypeFontGenerator(Gdx.files.internal("ka1.ttf"));
    fontBit = gen.generateFont(font);
    Label.LabelStyle fontStyle = new Label.LabelStyle();
    fontStyle.font = fontBit;
    sb = new SpriteBatch();
    this.mc = mc;
    completedRecipes = new LinkedList<>();
    completedRecipes.add(ItemEnum.Burger);
    completedRecipes.add(ItemEnum.CheeseBurger);
    completedRecipes.add(ItemEnum.CheeseBake);
    completedRecipes.add(ItemEnum.MeatBake);
    completedRecipes.add(ItemEnum.BakedPotato);
    completedRecipes.add(ItemEnum.CheesePizzaCooked);
    completedRecipes.add(ItemEnum.LettuceOnionSalad);
    completedRecipes.add(ItemEnum.LettuceTomatoSalad);
    completedRecipes.add(ItemEnum.MeatPizzaCooked);
    completedRecipes.add(ItemEnum.TomatoOnionLettuceSalad);
    completedRecipes.add(ItemEnum.TomatoOnionSalad);
    completedRecipes.add(ItemEnum.VegPizzaCooked);

    speedPowerUpTexture.setSize(200,35);
    reputationPowerUpTexture.setSize(200, 35);
    frustrationSuperFoodPowerUpTexture.setSize(200,35);
    superFoodPowerupTexture.setSize(200,35);
    tetrisSuperFoodPowerupTexture.setSize(200,35);
  }

  public void initialiseState(){
    hidePowerMenu();
    backgroundTexture.layer = 19;
    background.setPosition(250,50);
    speedPowerUpTexture.layer = 20;
    speedPowerUpButton.setPosition(400,430);
    reputationPowerUpTexture.layer = 20;
    reputationPowerUpButton.setPosition(400, 360);
    superFoodPowerupTexture.layer = 20;
    superFoodPowerUpButton.setPosition(400, 290);
    tetrisSuperFoodPowerupTexture.layer = 20;
    tetrisSuperFoodPowerUpButton.setPosition(400, 220);
    frustrationSuperFoodPowerUpTexture.layer = 20;
    stopFrustrationPowerUpButton.setPosition(400, 150);
    closeMenuTexture.layer = 20;
    closeMenuButton.setPosition(700, 500);




  }


  public void showPowerUpMenu() {
    background.isVisible = true;
    speedPowerUpButton.isVisible = true;
    reputationPowerUpButton.isVisible = true;
    superFoodPowerUpButton.isVisible = true;
    tetrisSuperFoodPowerUpButton.isVisible = true;
    stopFrustrationPowerUpButton.isVisible = true;
    closeMenuButton.isVisible = true;
  }

  public void hidePowerMenu() {
    background.isVisible = false;
    speedPowerUpButton.isVisible = false;
    reputationPowerUpButton.isVisible = false;
    superFoodPowerUpButton.isVisible = false;
    tetrisSuperFoodPowerUpButton.isVisible = false;
    stopFrustrationPowerUpButton.isVisible = false;
    closeMenuButton.isVisible = false;
  }


  @Override
  public void Update(float dt) {
    if (background.isVisible) {
      Integer money = cc.Money;
      //System.out.println(money);
      if (speedPowerUpButton.isClicked() && money >= prices.get("Speed")) {
        cc.ChangeMoney(-Float.valueOf(prices.get("Speed")));
        powerup.doSpeedPowerup();
      } else if (reputationPowerUpButton.isClicked() && money >= prices.get("Reputation")) {
        cc.ChangeMoney(-Float.valueOf(prices.get("Reputation")));
        powerup.buyReputation();
      } else if (superFoodPowerUpButton.isClicked() && money >= prices.get("SuperFood")) {
        cc.ChangeMoney(-Float.valueOf(prices.get("SuperFood")));
        powerup.superFood();
      } else if (tetrisSuperFoodPowerUpButton.isClicked() && money >= prices.get(
          "TetrisSuperFood")) {

        if(mc.getCurrentChef().getTopItem() != null  && completedRecipes.contains(mc.getCurrentChef().getTopItem().name)){
          cc.ChangeMoney(-Float.valueOf(prices.get("TetrisSuperFood")));
          powerup.tetrisSuperFoodGive();
        }else{
          System.out.println("Cant work");
        }
      } else if (stopFrustrationPowerUpButton.isClicked() && money >= prices.get("Frustration")) {
        cc.ChangeMoney(-Float.valueOf(prices.get("Frustration")));
        powerup.stopFrustration(60000);
      } else if (closeMenuButton.isClicked()) {
        System.out.println("CLOSING");
        hidePowerMenu();
      } else if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
        hidePowerMenu();
      }
    }
  }
}
