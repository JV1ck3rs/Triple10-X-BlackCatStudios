package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * This extracts the camera functions and allows all methods access to the camera BlackCatStudio's
 * Code
 *
 * @author Sam Toner
 * @date 12 /04/23
 */
public class CameraFunctions {

  /**
   * The constant camera.
   */
  public static CameraFunctions camera;
  /**
   * The Game screen camera.
   */
  OrthographicCamera gameScreenCamera;

  /**
   * Instantiates a new Camera functions.
   */
  public CameraFunctions() {
    if (camera != null) {
      return;
    }
    camera = this;
  }

  /**
   * Update camera.
   *
   * @param camera the camera
   */
  public void updateCamera(OrthographicCamera camera) {
    gameScreenCamera = camera;
  }

  /**
   * Unproject camera vector 3.
   *
   * @param touchPos the touch pos
   * @return the vector 3
   */
  public Vector3 unprojectCamera(Vector3 touchPos) {
    return gameScreenCamera.unproject(touchPos);
  }

}