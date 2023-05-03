package com.mygdx.game.Core.Powerups;

import com.mygdx.game.Core.Chef.MasterChef;
import com.mygdx.game.Core.Customers.CustomerController;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;

/**
 * Class containing procedures to execute powerups BlackCatStudio's Code
 *
 * @author Sam Toner
 * @date 02/05/23
 */
public class Powerup {

  MasterChef mc;
  CustomerController cc;

  public Powerup(MasterChef mc, CustomerController cc) {
    this.mc = mc;
    this.cc = cc;
  }

  public void doSpeedPowerup() {
    mc.upgradeSpeed();
    new java.util.Timer().schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            mc.downgradeSpeed();
          }
        },
        20000
    );
  }

  public void buyReputation() {
    cc.modifyReputation(1);
  }


  public void superFood() {
    cc.superFoodUpgrade();
  }

  public void tetrisSuperFoodGive() {
    Item dish = mc.getCurrentChef().getTopItem();
    //ItemEnum dish = cc.currentWaiting.MembersInLine.get(0).getDish();
    mc.getCurrentChef().DropItem();
    Item superItem = cc.menu.getSuperFromDish(ItemEnum.valueOf(dish.name()));
    mc.getCurrentChef().GiveItem(superItem);
  }


  public void stopFrustration(Integer delayTime) {
    cc.updateFrustration = false;
    new java.util.Timer().schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            cc.updateFrustration = true;
          }
        },
        delayTime
    );
  }
}
