package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LeaderBoard {
    static String filepath = Gdx.files.internal("leadboard.json").path();
    public void LeaderBoard(){


    }

    public void createJSONFile(){
        File jsonFile = new File(filepath);
        try{
            if(jsonFile.createNewFile()){
            }else{
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJSONData() throws IOException {
        FileReader reader = new FileReader(filepath);
        String data = "";
        int j;
        while((j = reader.read()) != -1){
            data = data + (char) j; //Iterates through every character in the json file adding it to the string
        }
        reader.close();
        //HashMap<String, String> jsonMap = new Gson().

        System.out.println(data);




    }


}
