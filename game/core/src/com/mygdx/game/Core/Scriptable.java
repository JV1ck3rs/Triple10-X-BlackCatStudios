package com.mygdx.game.Core;

import com.mygdx.game.Core.Rendering.GameObject;

/**
 * The methods gets called and helps to update scripts every frame BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 24/02/23
 */
public class Scriptable {


  public GameObject gameObject;


  public void setGameObject(GameObject obj) {
    gameObject = obj;
  }

  public void start() {
  }

  /**
   * runs every frame
   *
   * @param dt
   * @author Felix Seanor
   */
  public void update(float dt) {
  }

  /**
   * runs every fixed frame
   *
   * @param dt
   * @author Felix Seanor
   */
  public void fixedUpdate(float dt) {
  }

  /**
   * runs just before rendering
   *
   * @author Felix Seanor
   */
  public void onRender() {

  }

}
