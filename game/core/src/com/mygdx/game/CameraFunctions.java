package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * This extracts the camera functions and allows all methods access to the camera BlackCatStudio's
 * Code
 *
 * @author Sam Toner
 * @date 12/04/23
 */
public class CameraFunctions {

  public static CameraFunctions camera;
  OrthographicCamera gameScreenCamera;

  public CameraFunctions() {
    if (camera != null) {
      return;
    }
    camera = this;
  }

  public void updateCamera(OrthographicCamera camera) {
    gameScreenCamera = camera;
  }

  public Vector3 unprojectCamera(Vector3 touchPos) {
    return gameScreenCamera.unproject(touchPos);
  }

}