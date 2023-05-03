package com.mygdx.game.Core.Rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstracts rendering behind a list of sorted GameObjects BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @date 01/04/23
 */
public class RenderManager {

  public static RenderManager renderer;

  /**
   * Creates a render manager singleton
   *
   * @author Felix Seanor
   */
  public RenderManager() {
    if (renderer != null) {
      throw new IllegalArgumentException("This cannot be created more than once");
    }

    renderer = this;

  }

  /**
   * Creates a render call for all gameobjects
   *
   * @param batch
   * @author Felix Seanor
   */
  public void onRender(SpriteBatch batch) {
    List<GameObject> layerOrderedRenderables = new LinkedList<>();

    for (GameObject obj : GameObjectManager.objManager.gameObjectHash.values()
    ) {
      if (obj.image != null) {
        layerOrderedRenderables.add(obj);
      }
    }

    Collections.sort(layerOrderedRenderables, new Comparator<GameObject>() {
      @Override
      public int compare(GameObject o1, GameObject o2) {

        return o1.image.compare(o1.image, o2.image);
      }
    });

    for (GameObject obj : layerOrderedRenderables
    ) {
      obj.render(batch);
    }
  }

}
