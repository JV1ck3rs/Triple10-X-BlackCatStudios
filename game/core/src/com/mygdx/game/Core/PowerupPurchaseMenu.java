package com.mygdx.game.Core;


import java.util.HashMap;

/**
 * This contains the code for the powerup purchase menu UI
 * BlackCatStudio's Code
 * @author Sam Toner
 */
public class PowerupPurchaseMenu extends Scriptable {

  // Create objects for the power-up screen
  GameObject background = new GameObject(new BlackTexture(""));
  GameObject speedPowerUpButton = new GameObject(new BlackTexture(""));
  GameObject reputationPowerUpButton = new GameObject(new BlackTexture(""));
  GameObject superFoodPowerUpButton = new GameObject(new BlackTexture(""));
  GameObject tetrisSuperFoodPowerUpButton = new GameObject(new BlackTexture(""));
  GameObject stopFrustrationPowerUpButton = new GameObject(new BlackTexture(""));
  GameObject closeMenuButton = new GameObject(new BlackTexture(""));
  CustomerController cc;
  Powerup powerup;

  HashMap<String, Integer> prices = new HashMap<>();

  public void PowerupPurchaseMenu(CustomerController cc, Powerup powerup) {
    this.cc = cc;
    this.powerup = powerup;

    prices.put("Speed", 50);
    prices.put("Reputation", 60);
    prices.put("SuperFood", 75);
    prices.put("TetrisSuperFood", 100);
    prices.put("Frustration", 75);
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
      if (speedPowerUpButton.isClicked() && money > prices.get("Speed")) {
        cc.Money -= prices.get("Speed");
        powerup.doSpeedPowerup();
      } else if (reputationPowerUpButton.isClicked() && money > prices.get("Reputation")) {
        cc.Money -= prices.get("Reputation");
        powerup.buyReputation();
      } else if (superFoodPowerUpButton.isClicked() && money > prices.get("SuperFood")) {
        cc.Money -= prices.get("SuperFood");
        powerup.superFood();
      } else if (tetrisSuperFoodPowerUpButton.isClicked() && money > prices.get(
          "TetrisSuperFood")) {
        cc.Money -= prices.get("TetrisSuperFood");
        powerup.tetrisSuperFood();
      } else if (stopFrustrationPowerUpButton.isClicked() && money > prices.get("Frustration")) {
        cc.Money -= prices.get("Frustration");
        powerup.stopFrustration();
      } else if (closeMenuButton.isClicked()) {
        hidePowerMenu();
      }
    }
  }
}
