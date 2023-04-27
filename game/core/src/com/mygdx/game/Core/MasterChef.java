package com.mygdx.game.Core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Chef;
import com.mygdx.game.Core.GameState.ChefParams;
import com.mygdx.game.Core.GameState.GameState;
import com.mygdx.game.Core.GameState.ItemState;
import com.mygdx.game.Core.Interactions.Interactable;
import com.mygdx.game.Core.Interactions.Interaction;
import com.mygdx.game.Core.Interactions.Interaction.InteractionType;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.soundFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class manages the chefs and their inputs. Letting the player control them.
 * This class is the players main access point into the game outside of UI elements.
 *   BlackCatStudio's Code and a few functions from team triple 10s
 * @author Felix Seanor
 * @author Jack Vickers
 * @author Jack Hinton
 * @author Sam Toner
 */
public class MasterChef extends Scriptable {

  public float maxRange = 25;
  public int currentControlledChef = 0;
  private static ArrayList<TextureAtlas> chefAtlasArray;
World world;
  private Camera camera;
  List<Chef> chefs;

  ChefParams chefParams;

  private Pathfinding pathfind;

  private GameObject SelectionArrow;

  public int returnChefCount() {
    return chefs.size();
  }

  public void upgradeSpeed(){
    for(int i = 0; i< chefs.size(); i++){
      chefs.get(i).changeSpeed();
    }
  }

  public void downgradeSpeed(){
    for(int i = 0; i< chefs.size(); i++){
      chefs.get(i).decreaseSpeed();
    }
  }

  public Chef getChef(int i) {
    return chefs.get(i);
  }

  public Chef getCurrentChef() {

    return chefs.get(currentControlledChef);
  }

  /**
   * Generates a chef array which can be used to get random chef sprites from the chef class.
   * Team Triple 10s code
   * @author Amy Cross
   */
  public void generateChefArray() {
    String filename;
    TextureAtlas chefAtlas;
    for (int i = 1; i < 6; i++) {
      filename = "Chefs/Chef" + i + "/chef" + i + ".txt";
      chefAtlas = new TextureAtlas(Gdx.files.internal(filename));
      chefAtlasArray.add(chefAtlas);
    }
  }

  /**
   * Returns the chef array that's been created
   *
   * @return ArrayList<TextureAtlas> chefAtlasArray;
   * @author Felix Seanor
   */
  public static ArrayList<TextureAtlas> getChefAtlasArray() {
    return chefAtlasArray;
  }

  /**
   * Creates a Chef controller class, handling inputs
   *
   * @param count
   * @param world
   * @param camera
   * @param pathfinding pathfinding module
   * @author Felix Seanor
   */
  public MasterChef(int count, World world, Camera camera, Pathfinding pathfinding, ChefParams params) {


    chefParams =  params;

    chefs = new LinkedList<>();
    chefAtlasArray = new ArrayList<TextureAtlas>();
    this.pathfind = pathfinding;
    generateChefArray();
    this.world = world;

    this.camera = camera;

  BlackTexture ArrowTex =   new BlackTexture("Chefs/SelectionArrow.png");
  ArrowTex.setSize(20,30);
    SelectionArrow = new GameObject(ArrowTex);

    for (int i = 0; i < count; i++) {
      Vector2 pos = new Vector2(0,0);
      pos.x = 750 + 32 * i;
      pos.y = 300;
        CreateNewChef(pos,i);
    }

  }

  /**
   * Create a new chef given a position and iD
   * @param position
   * @param i
   * @author Felix Seanor
   */
  void CreateNewChef(Vector2 position, int i){
    GameObject chefsGameObject = new GameObject(
        new BlackSprite());//passing in null since chef will define it later
    chefs.add(new Chef(world, i, chefAtlasArray.get(i)));
    chefsGameObject.attachScript(chefs.get(i));
    chefsGameObject.image.setSize(18, 40);
    chefsGameObject.position.set(position);
    chefs.get(chefs.size()-1).speed = chefParams.MoveSpeed;
    chefs.get(i).updateSpriteFromInput("idlesouth");
  }


  /**
   * Select a chef
   * @param i
   * @author Felix Seanor
   */
  void SelectChef(int i) {
    currentControlledChef = i;

  }

  void MoveArrow(){
    SelectionArrow.position.set(getCurrentChef().gameObject.position).add(new Vector2(0,45));
  }

  /**
   * The chef tries to put down  an item onto a nearby surface
   *
   * @author Felix Seanor, Jack Vickers, Jack Hinton
   */
  public void GiveItem() {

    if (!chefs.get(currentControlledChef).CanFetchItem()) {
      return;
    }

    Scriptable script = Interaction.FindClosetInteractable(
        chefs.get(currentControlledChef).gameObject.position, InteractionType.Give, maxRange);

    if (script == null) {
      return;
    }

    Optional<Item> itemToGive = chefs.get(currentControlledChef).FetchItem();

    if (!itemToGive.isPresent()) {
      return;
    }

    ((Interactable) script).GiveItem(itemToGive.get());
  }

  /**
   * The chef tries to pick up an item from a nearby surface.
   * @author Felix Seanor
   */
  public void FetchItem() {

    if (!chefs.get(currentControlledChef).CanGiveItem()) {
      return;
    }

    Scriptable script = Interaction.FindClosetInteractable(
        chefs.get(currentControlledChef).gameObject.position, InteractionType.Fetch, maxRange);

    if (script == null) {
      return;
    }

    Item itemToGive = ((Interactable) script).RetrieveItem();

    if (itemToGive == null) {
      return;
    }

    chefs.get(currentControlledChef).GiveItem(itemToGive);


  }

  void CycleItemStack(){
    getCurrentChef().CycleStack();
  }

  /**
   * The chef attempts to interact with a nearby surface
   *
   * @author Jack Hinton
   * @author Jack Vickers
   */
  public void Interact() {
    Scriptable script = Interaction.FindClosetInteractable(
        chefs.get(currentControlledChef).gameObject.position, InteractionType.Interact, maxRange);

    if (script == null) {
      return;
    }

    ((Interactable) script).Interact();
  }

  /**
   * Select a chef from the number keys
   * @author Felix Seanor
   */
  void selectChef() {
    for (int i = 0; i < chefs.size(); i++) {
      if (Gdx.input.isKeyPressed(Input.Keys.NUM_1
          + i)) // increments to next number for each chef 1,2,3 ect (dont go above 9) {
        SelectChef(i);
      for (Chef c : chefs
      ) {
       // c.stop();
      }
    }
  }


  boolean KeyPressedNow(int key){
    return Gdx.input.isKeyJustPressed(key);
  }

  /**
   * Update method for this class
   *
   * @param dt
   * @author Felix Seanor
   * @author Jack Hinton
   * @author Jack Vickers
   */
  @Override
  public void Update(float dt) {
    selectChef();

    chefs.get(currentControlledChef).updateSpriteFromInput(chefs.get(currentControlledChef).getMove());


    if(KeyPressedNow(Inputs.CYCLE_STACK)) {
      CycleItemStack();
    }
    if (Gdx.input.isKeyJustPressed(Inputs.GIVE_ITEM)) {
      GiveItem();
    }
    if (Gdx.input.isKeyJustPressed(Inputs.FETCH_ITEM)) {
      FetchItem();
    }


    if (Gdx.input.isKeyJustPressed(Inputs.INTERACT)) {
      Interact();
    }

    if (Gdx.input.isKeyJustPressed((Inputs.DROP_ITEM))) {
      chefs.get(currentControlledChef).DropItem();
    }

    if(Gdx.input.isKeyJustPressed(Inputs.SPAWN_NEW_CHEF))
      AddNewChefIn();

    if (Gdx.input.isButtonJustPressed(0)) {
      Vector3 touchpos = new Vector3();
      touchpos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      touchpos = camera.unproject(touchpos);
      if (!(touchpos.x >= 940 && touchpos.y >= 524)) {
        List<Vector2> path = pathfind.FindPath((int) getCurrentChef().gameObject.position.x,
            (int) getCurrentChef().gameObject.position.y, (int) touchpos.x, (int) touchpos.y,
            DistanceTest.Euclidean);
//      System.out.println(path);
        getCurrentChef().GivePath(path);
      }


    }

    MoveArrow();
    }






  /**
   * Adds in an new chef upto max
   * @author Felix Seanor
   */
  public void AddNewChefIn(){
    if(chefs.size()<5)
      CreateNewChef(new Vector2(750,300), chefs.size());
  }

  public void LoadState(GameState state){
    for (int i = 0; i < state.ChefPositions.length; i++) {
      if(i< chefs.size())
        chefs.get(i).gameObject.position = state.ChefPositions[i];
      else {
        CreateNewChef(state.ChefPositions[i],i);
      }
    }

    for (Chef chef: chefs
    ) {
      for (int i = 0; i < Chef.CarryCapacity; i++) {
        chef.FetchItem();
      }
    }

    int i =0;
    GiveBackFromState(state);
  }

  /**
   * Creates or modifies chefs from a save state.
   * @param state
   * @author Felix Seanor
   */
  void GiveBackFromState(GameState state){
    int i =0;
    for (Chef chef: chefs
    ) {

      for (int j = 0; j < Chef.CarryCapacity; j++) {

        ItemState itemState = state.ChefHoldingStacks[i * Chef.CarryCapacity + j];

        if(itemState == null || itemState.item == null)
          continue;

        Item item = new Item(itemState);
        chef.GiveItem(item);
      }

      i++;
    }
  }

  /**
   * Save the current state of the chefs into GameState
   * @param state
   * @author Felix Seanor
   */
  public void SaveState(GameState state){
    state.ChefPositions = new Vector2[chefs.size()];
    state.ChefHoldingStacks = new ItemState[chefs.size()*Chef.CarryCapacity];


    for (int i = 0; i < chefs.size(); i++) {
      state.ChefPositions[i] = chefs.get(i).gameObject.position;

      for (int j = Chef.CarryCapacity-1; j >=0; j--) {
        Optional<Item> item =chefs.get(i).FetchItem();

        if(!item.isPresent())
        state.ChefHoldingStacks[i*Chef.CarryCapacity+j] = null;
        else
          state.ChefHoldingStacks[i*Chef.CarryCapacity+j] = new ItemState(item.get());


      }
    }




    GiveBackFromState(state);//This exists to make quick saves look nicer

  }



}
