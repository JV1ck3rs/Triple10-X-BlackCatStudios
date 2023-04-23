package com.mygdx.game.Core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.CameraFunctions;
import com.mygdx.game.GameScreen;

import java.util.LinkedList;
import java.util.List;

public class GameObject {

  public Renderable image;

  public Vector2 position;
  public Boolean destroyed = false;

  public float PhysicalWidth;

  public float PhysicalHeight;

  public Boolean isVisible = true;
  private Integer UID;
  List<Scriptable> Scripts = new LinkedList<>();


  public GameObject(Renderable renderable) {

    image = renderable;
    if (GameObjectManager.objManager != null) {
      GameObjectManager.objManager.SubmitGameObject(this);
    }
    position = new Vector2();
    PhysicalWidth = -1;
    PhysicalHeight = -1;

  }

  public void attachScript(Scriptable script) {
    Scripts.add(script);
    script.setGameObject(this);
    script.Start();
  }

  public Scriptable GetScript(int i){
   return Scripts.get(i);
  }

  public Integer getUID() {
    return UID;
  }

  public void setUID(int ID) {
    if (UID != null) {
      throw new IllegalArgumentException("You cannot change the UID");
    }

    UID = ID;
  }

  public void doUpdate(float dt) {
    for (Scriptable script : Scripts
    ) {
      script.Update(dt);
    }
  }

  public void doFixedUpdate(float dt) {
    for (Scriptable script : Scripts
    ) {
      script.FixedUpdate(dt);
    }
  }
  public void doOnRenderUpdate() {
    for (Scriptable script : Scripts
    ) {
      script.OnRender();
    }
  }

  public void render(SpriteBatch batch) {

    if(!isVisible || destroyed)
      return;
    if(image == null)
      return;



    doOnRenderUpdate();

    image.Render(batch, position.x, position.y);

  }


  public BlackSprite getSprite() {
    return ((BlackSprite) image);
  }

  public BlackTexture getBlackTexture() {
    return (BlackTexture) image;
  }

  public void setPosition(float x, float y) {
    position.set(x, y);
  }
  public boolean isClicked(){
    CameraFunctions camera1 = CameraFunctions.camera;
    Vector3 touchPos = new Vector3();
    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
      touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0);
      touchPos = camera1.unprojectCamera(touchPos);
      if (touchPos.x > position.x && touchPos.x < position.x + image.GetWidth()) {
        if (touchPos.y > position.y && touchPos.y < position.y + image.GetHeight()) {
          System.out.println("Clicked");
          return true;
        }
      }
    }
    return false;
  }

  public void setWidthAndHeight(float width, float height){
    PhysicalWidth = width;
    PhysicalHeight = height;
  }

  public float getWidth(){
    if(PhysicalWidth == -1)
      return image.GetWidth();
    return PhysicalWidth;
  }

  public float getHeight(){
    if(PhysicalHeight == -1)
      return image.GetHeight();
    return PhysicalHeight;
  }

  public void Destroy(){
    GameObjectManager.objManager.DestroyGameObject(this);
    image.Destroy();
  }
}
