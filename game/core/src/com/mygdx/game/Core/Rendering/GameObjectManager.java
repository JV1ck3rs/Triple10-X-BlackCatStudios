package com.mygdx.game.Core.Rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Core.Scriptable;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This manages references to every GameObject and loose scripts BlackCatStudio's Code
 *
 * @author Felix Seanor
 * @author Jack Hinton
 * @date 01 /04/23
 */
public class GameObjectManager {


  /**
   * The Game object hash.
   */
  public Hashtable<Integer, GameObject> gameObjectHash = new Hashtable<>();

  /**
   * The Loose scripts.
   */
  public List<Scriptable> looseScripts = new LinkedList<>();
  /**
   * The constant objManager.
   */
  public static GameObjectManager objManager;
  /**
   * The Rand.
   */
  Random rand;

  /**
   * Attaches a script to the world update sequence.
   *
   * @param scriptable the scriptable
   * @author Felix Seanor
   */
  public void appendLooseScript(Scriptable scriptable) {
    looseScripts.add(scriptable);
    scriptable.start();
  }

  /**
   * Returns objects with an interface. Limited to one superclass
   *
   * @param scriptType the script type
   * @return all valid objects
   * @author Felix Seanor
   * @author Jack Hinton
   */
  public List<Scriptable> returnObjectsWithInterface(Class<?> scriptType) {
    List<Scriptable> scripts = new LinkedList<>();
    for (GameObject obj : gameObjectHash.values()
    ) {
      for (Scriptable script : obj.scripts
      ) {

        Class<?>[] interfaces = script.getClass().getInterfaces();
        for (Class<?> scriptInterface : interfaces
        ) {
          if (scriptType == scriptInterface) {
            scripts.add(script);
            break;
          }
        }

        if (script.getClass().getSuperclass() != null) {
          Class<?>[] superInterfaces = script.getClass().getSuperclass().getInterfaces();

          for (Class<?> superClassInterface : superInterfaces
          ) {
            if (scriptType == superClassInterface) {
              scripts.add(script);
              break;
            }
          }

        }

      }
    }
    return scripts;
  }

  /**
   * Creates a singleton GameObject Manager
   *
   * @author Felix Seanor
   */
  public GameObjectManager() {
    if (objManager != null) {
      throw new IllegalArgumentException("This cannot be created more than once");
    }

    objManager = this;
    rand = new Random();


  }

  /**
   * Submits a new gameobject to this manager
   *
   * @param obj the obj
   * @author Felix Seanor
   */
  public void submitGameObject(GameObject obj) {

    int UID = createUID();

    gameObjectHash.put(UID, obj);

    obj.setUID(UID);

  }

  /**
   * run updates on objects
   *
   * @param dt the dt
   * @author Felix Seanor
   */
  public void doUpdate(float dt) {

    for (Scriptable scr : looseScripts) {
      scr.update(dt);

    }
    for (GameObject obj : gameObjectHash.values()
    ) {
      obj.doUpdate(dt);
    }
  }

  /**
   * Do fixed update.
   *
   * @param dt the dt
   */
  public void doFixedUpdate(float dt) {

    for (Scriptable scr : looseScripts) {
      scr.fixedUpdate(dt);

    }

    for (GameObject obj : gameObjectHash.values()
    ) {
      obj.doFixedUpdate(dt);
    }

  }


  /**
   * Render.
   *
   * @param batch the batch
   */
  public void render(SpriteBatch batch) {

    for (GameObject obj :
        gameObjectHash.values()) {
      obj.render(batch);
    }

  }

  /**
   * Destroy game object.
   *
   * @param obj the obj
   */
  public void destroyGameObject(GameObject obj) {
    obj.destroyed = true;

    gameObjectHash.remove(obj.getUID());
  }

  /**
   * Creates a unique ID for an object
   *
   * @return int
   * @author Felix Seanor
   */
  public int createUID() {
    int UID = 0;

    while (gameObjectHash.containsKey(UID)) {
      UID = rand.nextInt();
    }

    return UID;

  }

  /**
   * Reset.
   */
  public void reset() {
    for (GameObject obj : gameObjectHash.values()
    ) {
      obj.destroyed = true;
    }

    gameObjectHash.clear();
    looseScripts.clear();
  }

}
