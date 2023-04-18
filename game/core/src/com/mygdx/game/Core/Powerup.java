package com.mygdx.game.Core;

import com.mygdx.game.Stations.Station;

public class Powerup {
    MasterChef mc;
    CustomerController cc;
    Station s;
    public Powerup(MasterChef mc, CustomerController cc){
        this.mc = mc;
        this.cc = cc;
        //this.s = s;
    }

    public void doSpeedPowerup(){
        mc.upgradeSpeed();
    }

    public void buyReputation(){
        cc.ModifyReputation(1);
    }

    public void buyStaff(){
        if(mc.chefs.length<5){
            //Add New Chef
        }
    }

    //public void cookSpeedIncrease(){
    //    s.decreaseCookTime();
    //}
}
