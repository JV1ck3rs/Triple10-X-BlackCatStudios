package com.mygdx.game.Core;

import com.mygdx.game.Core.Customers.CustomerGroups;
import com.mygdx.game.Core.Customers.OrderType;
import com.mygdx.game.Customer;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.Stations.Station;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

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
        60000
    );
  }

  public void buyReputation() {
    cc.ModifyReputation(1);
  }


  public void superFood() {
    cc.superFoodUpgrade();
  }

  public void tetrisSuperFood() {
    Customer firstCustomer = cc.currentWaiting.MembersInLine.get(0);
    ItemEnum dish = firstCustomer.getDish();
    LinkedList<OrderType> orderTypes = cc.menu.getAllOrderTypes();
    List<ItemEnum> toClear = null;
    for (int i = 0; i < orderTypes.size(); i++) {
      if (orderTypes.get(i).orderables.contains(dish)) {
        toClear = orderTypes.get(i).orderables;
      }
    }
    for (int i = cc.currentWaiting.MembersInLine.size() - 1; i >= 0; i--) {
      Customer current = cc.currentWaiting.MembersInLine.get(i);
      if (toClear.contains(current.getDish())) {
        cc.currentWaiting.FeedSpecificCustomer(i);
      }
    }
  }


  public void stopFrustration() {
    cc.updateFrustration = false;
    new java.util.Timer().schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            cc.updateFrustration = true;
          }
        },
        60000
    );
  }
}
