package com.mygdx.game.Core.Interactions;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.Rendering.BlackTexture;
import com.mygdx.game.Core.Rendering.GameObject;
import com.mygdx.game.Core.Rendering.GameObjectManager;
import com.mygdx.game.Core.Scriptable;
import java.util.LinkedList;
import java.util.List;

/**
 * The interaction class that returns the closest applicable interactable script BlackCatStudio's
 * Code
 *
 * @author Felix Seanor
 * @author Jack Hinton
 * @author Jack Vickers
 * @date 1/05/23
 */
public class Interaction {

  public enum InteractionType {
    Fetch,
    Give,
    Interact
  }

  private static final boolean DEBUG = false;
  public static List<GameObject> debugVision = new LinkedList<>();

  /**
   * Find the closest acceptable interactable object. Will not return an object that cannot be
   * interacted given the type
   *
   * @param pos      position to scan from
   * @param type     type of position
   * @param maxRange max interaction range
   * @return Scriptable to interact with
   * @author Felix Seanor
   * @author Jack Hinton
   * @author Jack Vickers
   */
  public static Scriptable findClosetInteractable(Vector2 pos, InteractionType type,
      float maxRange) {

    if (DEBUG) {
      for (GameObject gameObject : debugVision
      ) {
        gameObject.destroy();
      }

      debugVision.clear();
    }
    BlackTexture tex;
    if (DEBUG) {
      tex = new BlackTexture("Black.png");
      tex.setSize(10, 10);

      GameObject co = new GameObject(tex);
      co.setPosition(pos.x, pos.y);

      debugVision.add(co);
    }

    //Gets every class with an interface implemented or a superclass with an interface
    List<Scriptable> interactables = GameObjectManager.objManager.returnObjectsWithInterface(
        Interactable.class);

    float distance = maxRange * maxRange * maxRange;
    Vector2 vector = new Vector2();
    Scriptable currentClosestScript = null;
    Vector2 temporary = Vector2.Zero;
    Vector2 ScriptPos;

//For all script check if it's valid for this type of interaction.
    // E.g. if your putting food down you cant put down on a full station
    // or a food station as it cannot receive
    for (Scriptable script : interactables
    ) {
      temporary.set(pos);
      ScriptPos = script.gameObject.position;

      if (type == InteractionType.Fetch) {
        if (!((Interactable) script).canRetrieve()) {
          continue;
        }
      } else if (type == InteractionType.Give) {
        if (!((Interactable) script).canGive()) {
          continue;
        }
      } else if (type == InteractionType.Interact) {
        if (!((Interactable) script).canInteract()) {
          continue;
        }
      }

      //2D separating Axis theorem with no rotation
      //Checks if the XY distance is smaller than the two sizes

      // case 1 X axis

      Vector2 L = new Vector2(1, 0);

      float bWidth = script.gameObject.getWidth() / 2f;
      float bHeight = script.gameObject.getHeight() / 2f;

      ScriptPos = new Vector2(ScriptPos);

      ScriptPos.add(bWidth, bHeight);

      vector.set(pos).sub(ScriptPos);

      //Case 2 Y axis

      float bound = Math.abs(L.dot(vector));
      float lower = maxRange + Math.abs(bWidth);

      float minVector;
      if (bound > lower) {
        //separating axis
        continue;
      } else {
        minVector = bound;
      }

      L.set(0, 1);
      bound = Math.abs(L.dot(vector));
      lower = maxRange + Math.abs(bHeight);

      if (bound > lower) {
        //seperating axis
        continue;
      } else {
        minVector = Math.min(bound, minVector);
      }

      if (DEBUG) {
        GameObject obj = new GameObject(tex);
        obj.setPosition(ScriptPos.x, ScriptPos.y);

        debugVision.add(obj);
      }
      //If the minimum vector is smaller than the current distance then accept this as the closest
      if (minVector < distance) {
        distance = minVector;

        currentClosestScript = script;
      }


    }

    return currentClosestScript;

  }
}
