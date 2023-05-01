package com.mygdx.game.Core.Interactions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.mygdx.game.Core.BlackTexture;
import com.mygdx.game.Core.GameObject;
import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Core.Scriptable;
import java.util.LinkedList;
import java.util.List;

/**
 * The interaction class that returns the closest applicable interactable script
 * BlackCatStudio's Code
 * @author Felix Seanor
 * @author Jack Hinton
 * @author Jack Vickers
 */
public class Interaction {

  public enum InteractionType {
    Fetch,
    Give,
    Interact
  }

  private static final boolean Debug = false;
  public static List<GameObject> debugVision = new LinkedList<>();

  /**
   * Find the closest acceptable interactable object. Will not return an object that cannot be interacted given the type
   * @param pos position to scan from
   * @param type type of position
   * @param maxRange max interaction range
   * @return Scriptable to interact with
   * @author Felix Seanor
   * @author Jack Hinton
   * @author Jack Vickers
   */
  public static Scriptable FindClosetInteractable(Vector2 pos, InteractionType type,
      float maxRange) {

    if(Debug) {
      for (GameObject ob : debugVision
      ) {
        ob.Destroy();
      }

      debugVision.clear();
    }
    BlackTexture tex;
    if(Debug) {
      tex = new BlackTexture("Black.png");
      tex.setSize(10, 10);

      GameObject co = new GameObject(tex);
      co.setPosition(pos.x, pos.y);

      debugVision.add(co);
    }
    List<Scriptable> interactables = GameObjectManager.objManager.returnObjectsWithInterface(
        Interactable.class);

    float distance = maxRange * maxRange * maxRange;
    float dst2 = 0;
    Vector2 vct = new Vector2();
    Scriptable currentClosestScript = null;
    Vector2 temp = Vector2.Zero;
    Vector2 ScriptPos;



    for (Scriptable script : interactables
    ) {
      temp.set(pos);
      ScriptPos = script.gameObject.position;
      dst2 = (temp.sub(ScriptPos).dot(temp));

      if (type == InteractionType.Fetch) {
        if (!((Interactable) script).CanRetrieve()) {
          continue;
        }
      } else if (type == InteractionType.Give) {
        if (!((Interactable) script).CanGive()) {
          continue;
        }
      } else if (type == InteractionType.Interact) {
        if (!((Interactable) script).CanInteract()) {
          continue;
        }
      }


      dst2 = (float) Math.sqrt(dst2);

      // case 1


      Vector2 L  = new Vector2(1,0);

      float BW = script.gameObject.getWidth()/2f;
      float BH = script.gameObject.getHeight()/2f;

      ScriptPos = new Vector2(ScriptPos);

      ScriptPos.add(BW,BH);

      vct.set(pos).sub(ScriptPos);


      float bound = Math.abs(L.dot(vct));
      float lower = maxRange + Math.abs(BW);

      float minVct = 0;
      if(bound>lower){
        //seperating axis
        continue;
      } else {
        minVct = bound;
      }



      L.set(0,1);
      bound = Math.abs(L.dot(vct));
      lower = maxRange + Math.abs(BH);

      if(bound>lower){
        //seperating axis
        continue;
      } else {
        minVct = Math.min(bound,minVct);
      }


      if(Debug) {
        GameObject obj = new GameObject(tex);
        obj.setPosition(ScriptPos.x, ScriptPos.y);

        debugVision.add(obj);
      }

      if(minVct<distance){
        distance = minVct;

        currentClosestScript = script;
      }



/*
      if ((pos.x + maxRange) > ScriptPos.x
          && (pos.x - maxRange) < ScriptPos.x + script.gameObject.getWidth()) {
        if ((pos.y + maxRange) > ScriptPos.y
            && (pos.y - maxRange) < ScriptPos.y + script.gameObject.getHeight()) {
          if (dst2 < distance) {
            distance = dst2;
            currentClosestScript = script;
          }
        }
      }*/

    }

    return currentClosestScript;

  }
}
