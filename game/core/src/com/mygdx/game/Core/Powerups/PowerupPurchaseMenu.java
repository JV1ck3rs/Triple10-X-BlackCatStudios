package com.mygdx.game.Core.Powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Core.Chef.MasterChef;
import com.mygdx.game.Core.Customers.CustomerController;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Scriptable;
import com.mygdx.game.Items.ItemEnum;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This contains the code for the powerup purchase menu UI BlackCatStudio's Code
 *
 * @author Sam Toner
 * @date 02 /05/23
 */
public class PowerupPurchaseMenu extends Scriptable {

  /**
   * The Font.
   */
  FreeTypeFontGenerator.FreeTypeFontParameter font;
  /**
   * The Gen.
   */
  FreeTypeFontGenerator gen;
  /**
   * The Font bit.
   */
  BitmapFont fontBit;

  /**
   * The Background texture.
   */
  BlackTexture backgroundTexture = new BlackTexture("PowerupAssets/BackGround.png");
  /**
   * The Speed power up texture.
   */
  BlackTexture speedPowerUpTexture = new BlackTexture("PowerupAssets/SpeedPowerUp.png");
  /**
   * The Reputation power up texture.
   */
  BlackTexture reputationPowerUpTexture = new BlackTexture("PowerupAssets/ReputationPowerUp.png");
  /**
   * The Super food powerup texture.
   */
  BlackTexture superFoodPowerupTexture = new BlackTexture("PowerupAssets/SuperFoodPowerUp.png");
  /**
   * The Tetris super food powerup texture.
   */
  BlackTexture tetrisSuperFoodPowerupTexture = new BlackTexture(
      "PowerupAssets/TetrisSuperFoodPowerup.png");
  /**
   * The Frustration super food power up texture.
   */
  BlackTexture frustrationSuperFoodPowerUpTexture = new BlackTexture(
      "PowerupAssets/FrustrationPowerup.png");
  /**
   * The Close menu texture.
   */
  BlackTexture closeMenuTexture = new BlackTexture("Items/CloseButton.png");

  /**
   * The Background.
   */
// Create objects for the power-up screen
  public GameObject background = new GameObject(backgroundTexture);
  /**
   * The Speed power up button.
   */
  GameObject speedPowerUpButton = new GameObject(speedPowerUpTexture);
  /**
   * The Reputation power up button.
   */
  GameObject reputationPowerUpButton = new GameObject(reputationPowerUpTexture);
  /**
   * The Super food power up button.
   */
  GameObject superFoodPowerUpButton = new GameObject(superFoodPowerupTexture);
  /**
   * The Tetris super food power up button.
   */
  GameObject tetrisSuperFoodPowerUpButton = new GameObject(tetrisSuperFoodPowerupTexture);
  /**
   * The Stop frustration power up button.
   */
  GameObject stopFrustrationPowerUpButton = new GameObject(frustrationSuperFoodPowerUpTexture);
  /**
   * The Close menu button.
   */
  GameObject closeMenuButton = new GameObject(closeMenuTexture);


  /**
   * The Speed text texture.
   */
  BlackTexture speedTextText = new BlackTexture("PowerupAssets/SpeedText.png");
  /**
   * The Reputation text texture.
   */
  BlackTexture reputationTextText = new BlackTexture("PowerupAssets/ReputationText.png");
  /**
   * The Super food text texture.
   */
  BlackTexture SuperFoodTextText = new BlackTexture("PowerupAssets/SuperFoodText.png");
  /**
   * The Frustration text texture.
   */
  BlackTexture frustrationTextText = new BlackTexture("PowerupAssets/FrustrationText.png");
  /**
   * The Mega food text texture.
   */
  BlackTexture megaFoodTextText = new BlackTexture("PowerupAssets/MegaFoodText.png");

  /**
   * The Speed text.
   */
  GameObject speedText = new GameObject(speedTextText);
  /**
   * The Reputation text.
   */
  GameObject reputationText = new GameObject(reputationTextText);
  /**
   * The Super food text.
   */
  GameObject superFoodText = new GameObject(SuperFoodTextText);
  /**
   * The Frustration text.
   */
  GameObject frustrationText = new GameObject(frustrationTextText);
  /**
   * The Mega food text.
   */
  GameObject megaFoodText = new GameObject(megaFoodTextText);

  /**
   * The Cc.
   */
  CustomerController cc;
  /**
   * The Powerup.
   */
  Powerup powerup;

  /**
   * The Prices.
   */
  HashMap<String, Integer> prices = new HashMap<>();

  /**
   * The Mc.
   */
  MasterChef mc;
  /**
   * The Completed recipes.
   */
  LinkedList<ItemEnum> completedRecipes;


  /**
   * Instantiates a new Powerup purchase menu.
   *
   * @param cc      the cc
   * @param powerup the powerup
   * @param mc      the mc
   *
   * @author Sam Toner
   * @date 03/05/2023
   */
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

    speedPowerUpTexture.setSize(200, 35);
    reputationPowerUpTexture.setSize(200, 35);
    frustrationSuperFoodPowerUpTexture.setSize(200, 35);
    superFoodPowerupTexture.setSize(200, 35);
    tetrisSuperFoodPowerupTexture.setSize(200, 35);
    speedTextText.layer = 20;
    reputationTextText.layer = 20;
    frustrationTextText.layer = 20;
    SuperFoodTextText.layer = 20;
    megaFoodTextText.layer = 20;
  }

  /**
   * Initialise state.
   *
   * @author Sam Toner
   * @date 03/05/2023
   */
  public void initialiseState() {
    hidePowerMenu();
    backgroundTexture.layer = 19;
    background.setPosition(250, 50);
    speedPowerUpTexture.layer = 20;
    speedPowerUpButton.setPosition(400, 430);
    reputationPowerUpTexture.layer = 20;
    reputationPowerUpButton.setPosition(400, 360);
    superFoodPowerupTexture.layer = 20;
    superFoodPowerUpButton.setPosition(400, 290);
    tetrisSuperFoodPowerupTexture.layer = 20;
    tetrisSuperFoodPowerUpButton.setPosition(400, 150);
    frustrationSuperFoodPowerUpTexture.layer = 20;
    stopFrustrationPowerUpButton.setPosition(400, 220);
    closeMenuTexture.layer = 20;
    closeMenuButton.setPosition(700, 500);

    speedText.setPosition(300, 380);
    reputationText.setPosition(300, 310);
    superFoodText.setPosition(300, 240);
    frustrationText.setPosition(300, 170);
    megaFoodText.setPosition(275, -45);


  }


  /**
   * Show power up menu.
   *
   * @author Sam Toner
   * @date 03/05/2023
   */
  public void showPowerUpMenu() {
    background.isVisible = true;
    speedPowerUpButton.isVisible = true;
    reputationPowerUpButton.isVisible = true;
    superFoodPowerUpButton.isVisible = true;
    tetrisSuperFoodPowerUpButton.isVisible = true;
    stopFrustrationPowerUpButton.isVisible = true;
    closeMenuButton.isVisible = true;
    speedText.isVisible = true;
    reputationText.isVisible = true;
    frustrationText.isVisible = true;
    superFoodText.isVisible = true;
    megaFoodText.isVisible = true;
  }

  /**
   * Hide power menu.
   */
  public void hidePowerMenu() {
    background.isVisible = false;
    speedPowerUpButton.isVisible = false;
    reputationPowerUpButton.isVisible = false;
    superFoodPowerUpButton.isVisible = false;
    tetrisSuperFoodPowerUpButton.isVisible = false;
    stopFrustrationPowerUpButton.isVisible = false;
    closeMenuButton.isVisible = false;
    speedText.isVisible = false;
    reputationText.isVisible = false;
    frustrationText.isVisible = false;
    superFoodText.isVisible = false;
    megaFoodText.isVisible = false;
  }


  @Override
  public void update(float dt) {
    if (background.isVisible) {
      Integer money = cc.money;
      //System.out.println(money);
      if (speedPowerUpButton.isClicked() && money >= prices.get("Speed")) {
        cc.changeMoney(-Float.valueOf(prices.get("Speed")));
        powerup.doSpeedPowerup();
      } else if (reputationPowerUpButton.isClicked() && money >= prices.get("Reputation")) {
        cc.changeMoney(-Float.valueOf(prices.get("Reputation")));
        powerup.buyReputation();
      } else if (superFoodPowerUpButton.isClicked() && money >= prices.get("SuperFood")) {
        cc.changeMoney(-Float.valueOf(prices.get("SuperFood")));
        powerup.superFood();
      } else if (tetrisSuperFoodPowerUpButton.isClicked() && money >= prices.get(
          "TetrisSuperFood")) {

        if (mc.getCurrentChef().getTopItem() != null && completedRecipes.contains(
            mc.getCurrentChef().getTopItem().name)) {
          cc.changeMoney(-Float.valueOf(prices.get("TetrisSuperFood")));
          powerup.tetrisSuperFoodGive();
        } else {
//          System.out.println("Cant work");
        }
      } else if (stopFrustrationPowerUpButton.isClicked() && money >= prices.get("Frustration")) {
        cc.changeMoney(-Float.valueOf(prices.get("Frustration")));
        powerup.stopFrustration(60000);
      } else if (closeMenuButton.isClicked()) {
//        System.out.println("CLOSING");
        hidePowerMenu();
      } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
        hidePowerMenu();
      }
    }
  }
}
