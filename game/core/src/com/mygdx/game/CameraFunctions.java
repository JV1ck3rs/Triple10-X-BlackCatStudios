package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraFunctions {
    public static CameraFunctions camera;
    OrthographicCamera gameScreenCamera;
    public CameraFunctions(){
        if(camera != null){
            return;
        }
        camera = this;
    }

    public void updateCamera(OrthographicCamera camera){
        gameScreenCamera = camera;
    }

    public Vector3 unprojectCamera(Vector3 touchPos){
        return gameScreenCamera.unproject(touchPos);
    }

}