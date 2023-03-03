package com.mygdx.game.Core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.awt.font.FontRenderContext;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameObjectManager {

  public Hashtable<Integer, GameObject> GameObjects = new Hashtable<>();

  public List<Scriptable> LooseScripts = new LinkedList<>();
  public static GameObjectManager objManager;
  Random rand;


  public void AppendLooseScript(Scriptable scriptable){
    LooseScripts.add(scriptable);
    scriptable.Start();
  }

  public List<Scriptable> returnObjectsWithScript(Class<?> scriptType){
    List<Scriptable> scripts = new LinkedList<>();
    for (GameObject obj: GameObjects.values()
    ) {
      for (Scriptable script: obj.Scripts
      ) {
        if(scriptType == script.getClass())
          scripts.add(script);

      }
    }
    return scripts;
  }
  public GameObjectManager() {
    if (objManager != null) {
      throw new IllegalArgumentException("This cannot be created more than once");
    }

    objManager = this;
    rand = new Random();


  }


  public void SubmitGameObject(GameObject obj) {

    int UID = CreateUID();

    GameObjects.put(UID, obj);

    obj.setUID(UID);

  }


  public void doUpdate(float dt) {

    for (Scriptable scr : LooseScripts)
    {
      scr.Update(dt);

    }
    for (GameObject obj : GameObjects.values()
    ) {
      obj.doUpdate(dt);
    }
  }

  public void doFixedUpdate(float dt) {


    for (Scriptable scr : LooseScripts)
    {
      scr.FixedUpdate(dt);

    }

    for (GameObject obj : GameObjects.values()
    ) {
      obj.doFixedUpdate(dt);
    }

  }


  public void render(SpriteBatch batch) {

    for (GameObject obj :
        GameObjects.values()) {
      obj.render(batch);
    }

  }

  public void DestroyGameObject(GameObject obj) {
    obj.destroyed = true;

    GameObjects.remove(obj.getUID());
  }

  public int CreateUID() {
    int UID = 0;

    while (GameObjects.containsKey(UID)) {
      UID = rand.nextInt();
    }

    return UID;

  }


}
