package piazzapanictests.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests that the assets are present.
 * @Satisfies Requirements for UR_MENU, UR_ENJOYABILITY, UR_SCENARIO_SET_CUSTOMER, UR_GAME_MAP, UR_HOW_TO_PLAY, UR_PAUSE_MENU, UR_CUSTOMER_ORDER
 * @author Jack Vickers
 * @author Azzam Amirul
 * @author Hubert Solecki
 * @date 02/05/2023
 */
@RunWith(GdxTestRunner.class)
public class AssetTests {

  /**
   * Tests that the chef assets are present.
   * @Satisfies UR_ENJOYABILITY
   * @author Jack Vickers
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testChefAssetExists() {
    assertTrue("This test will only pass when the chef1.png asset exists.",
        Gdx.files.internal("Chefs/Chef1/chef1.png").exists());
    assertTrue("This test will only pass when the chef2.png asset exists.",
            Gdx.files.internal("Chefs/Chef2/chef2.png").exists());
    assertTrue("This test will only pass when the chef3.png asset exists.",
            Gdx.files.internal("Chefs/Chef3/chef3.png").exists());
    assertTrue("This test will only pass when the chef4.png asset exists.",
            Gdx.files.internal("Chefs/Chef4/chef4.png").exists());
    assertTrue("This test will only pass when the chef5.png asset exists.",
            Gdx.files.internal("Chefs/Chef5/chef5.png").exists());
    assertTrue("This test will only pass when the SelectionArrow.png asset exists.",
            Gdx.files.internal("Chefs/SelectionArrow.png").exists());
  }

  /**
   * Tests that the skin json asset exists. This is used for the UI on the
   * scenarioModeConfigScreen.
   * @Satisfies UR_SCENARIO_SET_CUSTOMER
   * @author Jack Vickers
   * @date 07/04/2023
   */
  @Test
  public void testSkinJsonExists() {
    assertTrue("This test will only pass when the skin.json asset exists.",
        Gdx.files.internal("clean-crispy-ui.json").exists());
  }

  /**
   * Tests that the skin atlas asset exists. This is used for the UI on the
   * scenarioModeConfigScreen.
   * @Satisfies UR_SCENARIO_SET_CUSTOMER
   * @author Jack Vickers
   * @date 07/04/2023
   */
  @Test
  public void testSkinAtlasExists() {
    assertTrue("This test will only pass when the skin.atlas asset exists.",
        Gdx.files.internal("clean-crispy-ui.atlas").exists());
  }

  /**
   * Tests that the skin image asset exists. This is used for the UI on the
   * scenarioModeConfigScreen.
   * @Satisfies UR_SCENARIO_SET_CUSTOMER
   * @author Jack Vickers
   * @date 07/04/2023
   */
  @Test
  public void testSkinImageExists() {
    assertTrue("This test will only pass when the skin.png asset exists.",
        Gdx.files.internal("clean-crispy-ui.png").exists());
  }

  /**
   * Tests that the skin font asset exists. This is used for the UI on the
   * scenarioModeConfigScreen.
   * @Satisfies UR_SCENARIO_SET_CUSTOMER
   * @author Jack Vickers
   * @date 07/04/2023
   */
  @Test
  public void testSkinFontExists() {
    assertTrue("This test will only pass when the skin.fnt asset exists.",
        Gdx.files.internal("font-export.fnt").exists());
  }

  /**
   * Tests that the assets for the load game button are present. This button is displayed on the
   * title screen.
   * @Satisfies UR_MENU
   * @author Jack Vickers
   * @date 17/04/2023
   */
  @Test
  public void testLoadGameButtonImagesExist() {
    assertTrue("This test will only pass when the LoadUp.png asset exists.",
        Gdx.files.internal("LoadUp.png").exists());
    assertTrue("This test will only pass when the LoadDown.png asset exists.",
        Gdx.files.internal("LoadDown.png").exists());
  }

  /**
   * Tests that the assets for the save game button are present. This button is displayed on the
   * pause screen.
   * @Satisfies UR_PAUSE_MENU
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testSaveGameButtonImagesExist(){
    assertTrue("This test will only pass when the SaveUp.png asset exists.",
            Gdx.files.internal("SaveUp.png").exists());
    assertTrue("This test will only pass when the SaveDown.png asset exists.",
            Gdx.files.internal("SaveDown.png").exists());
  }

  /**
   * Tests that the atlas for the main menu exists. Also used for
   * the scenario mode config screen.
   * @Satisfies UR_MAIN_MENU
   * @author Jack Vickers
   * @date 17/04/2023
   */
  @Test
  public void testMainMenuAtlasExists() {
    assertTrue("This test will only pass when the mainmenu.atlas asset exists.",
        Gdx.files.internal("mainMenu.atlas").exists());
  }

  /**
   * Tests that the image exists which is referenced by the main menu atlas.
   * @Satisfies UR_MAIN_MENU
   * @author Jack Vickers
   * @date 17/04/2023
   */
  @Test
  public void testMainMenuImageExists() {
    assertTrue("This test will only pass when the mainMenu.png asset exists.",
        Gdx.files.internal("mainMenu.png").exists());
  }

  /**
   * Tests that the images for the play button on the main menu and scenario config screens
   * can be found using the texture atlas.
   * @Satisfies UR_MAIN_MENU
   * @author Jack Vickers
   * @date 17/04/2023
   */
  @Test
  public void testPlayButtonImagesExist() {
    TextureAtlas mainMenuAtlas = new TextureAtlas(Gdx.files.internal("mainMenu.atlas"));
    assertNotNull("A texture reqion for the playbutton up image should exist", mainMenuAtlas
        .findRegion("playButton"));
    assertNotNull("A texture reqion for the playbutton down image should exist", mainMenuAtlas
        .findRegion("playButtonDown"));
  }

  /**
   * Tests that the images for the scenario mode button on the main menu can be found using the
   * texture atlas.
   * @Satisfies UR_MAIN_MENU
   * @author Jack Vickers
   * @date 17/04/2023
   */
  @Test
  public void testScenarioModeButtonImagesExist() {
    TextureAtlas mainMenuAtlas = new TextureAtlas(Gdx.files.internal("mainMenu.atlas"));
    assertNotNull("A texture reqion for the scenario mode button up image should exist",
        mainMenuAtlas
            .findRegion("scenarioButton"));
    assertNotNull("A texture reqion for the scenarioModeButton down image should exist",
        mainMenuAtlas
            .findRegion("scenarioButtonDown"));
  }

  /**
   * Tests that the images for the exit button can be found using the texture atlas.
   * @Satisfies UR_MAIN_MENU
   * @author Jack Vickers
   * @date 17/04/2023
   */
  @Test
  public void testExitButtonImagesExist() {
    TextureAtlas mainMenuAtlas = new TextureAtlas(Gdx.files.internal("mainMenu.atlas"));
    assertNotNull("A texture reqion for the exit button up image should exist",
        mainMenuAtlas.findRegion("exitButton"));
    assertNotNull("A texture reqion for the exit button down image should exist",
        mainMenuAtlas.findRegion("exitButtonDown"));
  }

  /**
   * Tests that the image for the main menu background can be found using the texture atlas.
   * @Satisfies UR_MAIN_MENU
   * @author Jack Vickers
   * @date 17/04/2023
   */
  @Test
  public void testMainMenuBackgroundExists() {
    TextureAtlas mainMenuAtlas = new TextureAtlas(Gdx.files.internal("mainMenu.atlas"));
    assertNotNull("A texture reqion for the main menu background image should exist",
        mainMenuAtlas.findRegion("menuPP"));
  }

  /**
   * Tests that the assets for the pause button are present. This button is displayed on the game
   * screen.
   * @Satisfies UR_PAUSE_MENU
   * @author Jack Vickers
   * @date 17/04/2023
   */
  @Test
  public void testPauseButtonImagesExist() {
    assertTrue("This test will only pass when the PauseUp.png asset exists.",
        Gdx.files.internal("PauseUp.png").exists());
    assertTrue("This test will only pass when the PauseDown.png asset exists.",
        Gdx.files.internal("PauseDown.png").exists());
  }
  /**
   * Tests that the assets for the resume button are present. This button is displayed on the
   * pause screen.
   * @Satisfies UR_PAUSE_MENU
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testResumeButtonImagesExist() {
    assertTrue("This test will only pass when the ResumeUp.png asset exists.",
            Gdx.files.internal("ResumeUp.png").exists());
    assertTrue("This test will only pass when the ResumeDown.png asset exists.",
            Gdx.files.internal("ResumeDown.png").exists());
  }
  /**
   * Tests that the assets for the difficulty buttons are present. This button is displayed on the
   * main menu screen.
   * @Satisfies UR_MAIN_MENU
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testDifficultyButtonsImagesExist() {
    assertTrue("This test will only pass when the RelaxingUp.png asset exists.",
            Gdx.files.internal("RelaxingUp.png").exists());
    assertTrue("This test will only pass when the RelaxingDown.png asset exists.",
            Gdx.files.internal("RelaxingDown.png").exists());
    assertTrue("This test will only pass when the StressfulUp.png asset exists.",
            Gdx.files.internal("StressfulUp.png").exists());
    assertTrue("This test will only pass when the StressfulDown.png asset exists.",
            Gdx.files.internal("StressfulDown.png").exists());
    assertTrue("This test will only pass when the ExtremeUp.png asset exists.",
            Gdx.files.internal("ExtremeUp.png").exists());
    assertTrue("This test will only pass when the ExtremeDown.png asset exists.",
            Gdx.files.internal("ExtremeDown.png").exists());

  }

  /**
   * Tests that the assets for the high scores button are present. This button is displayed on the
   * main menu screen.
   * @Satisfies UR_MAIN_MENU
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testHighScoresButtonImagesExist(){
    assertTrue("This test will only pass when the HighScoresUp.png asset exists.",
            Gdx.files.internal("HighScoresUp.png").exists());
    assertTrue("This test will only pass when the HighScoresDown.png asset exists.",
            Gdx.files.internal("HighScoresDown.png").exists());
  }
  /**
   * Tests that the assets for the how to play button are present. This button is displayed on the
   * game screen.
   * @Satisfies UR_HOW_TO_PLAY
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testHowToPlayButtonImagesExist(){
    assertTrue("This test will only pass when the HowToPlayUp.png asset exists.",
            Gdx.files.internal("HowToPlayUp.png").exists());
    assertTrue("This test will only pass when the HowToPlayDown.png asset exists.",
            Gdx.files.internal("HowToPlayDown.png").exists());
  }
  /**
   * Tests that the assets for the save and exit button are present. This button is displayed on the
   * leaderboard screen.
   * @Satisfies UR_LEADERBOARD
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testSaveExitButtonImagesExist(){
    assertTrue("This test will only pass when the SaveExitUp.png asset exists.",
            Gdx.files.internal("SaveExitUp.png").exists());
    assertTrue("This test will only pass when the SaveExitDown.png asset exists.",
            Gdx.files.internal("SaveExitDown.png").exists());
  }
  /**
   * Tests that the assets for the piazza panic game map are present.
   * @Satisfies UR_GAME_MAP
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testPiazzaPanicMapExists(){
    assertTrue("This test will only pass when the PiazzaPanicMap.tmx asset exists.",
            Gdx.files.internal("PiazzaPanicMap.tmx").exists());
  }

  /**
   * Tests that all assets in the Items folder exist.
   * @Satisfies UR_ENJOYABILITY
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */
  @Test
  public void testItemsAssetsExist(){
    assertTrue("This test will only pass when the BakedPotato.png asset exists.",
            Gdx.files.internal("Items/BakedPotato.png").exists());
    assertTrue("This test will only pass when the Buns.png asset exists.",
            Gdx.files.internal("Items/Buns.png").exists());
    assertTrue("This test will only pass when the Burger.png asset exists.",
            Gdx.files.internal("Items/Burger.png").exists());
    assertTrue("This test will only pass when the Cheese.png asset exists.",
            Gdx.files.internal("Items/Cheese.png").exists());
    assertTrue("This test will only pass when the CheeseBake.png asset exists.",
            Gdx.files.internal("Items/CheeseBake.png").exists());
    assertTrue("This test will only pass when the CheeseBurger.png asset exists.",
            Gdx.files.internal("Items/CheeseBurger.png").exists());
    assertTrue("This test will only pass when the CheesePizza.png asset exists.",
            Gdx.files.internal("Items/CheesePizza.png").exists());
    assertTrue("This test will only pass when the CheesePizzaCooked.png asset exists.",
            Gdx.files.internal("Items/CheesePizzaCooked.png").exists());
    assertTrue("This test will only pass when the CheesePotato.png asset exists.",
            Gdx.files.internal("Items/CheesePotato.png").exists());
    assertTrue("This test will only pass when the Cinder.png asset exists.",
            Gdx.files.internal("Items/Cinder.png").exists());
    assertTrue("This test will only pass when the CloseButton.png asset exists.",
            Gdx.files.internal("Items/CloseButton.png").exists());
    assertTrue("This test will only pass when the CookedPatty.png asset exists.",
            Gdx.files.internal("Items/CookedPatty.png").exists());
    assertTrue("This test will only pass when the CutLettuce.png asset exists.",
            Gdx.files.internal("Items/CutLettuce.png").exists());
    assertTrue("This test will only pass when the CutOnion.png asset exists.",
            Gdx.files.internal("Items/CutOnion.png").exists());
    assertTrue("This test will only pass when the CutTomato.png asset exists.",
            Gdx.files.internal("Items/CutTomato.png").exists());
    assertTrue("This test will only pass when the Dough.png asset exists.",
            Gdx.files.internal("Items/Dough.png").exists());
    assertTrue("This test will only pass when the Lettuce.png asset exists.",
            Gdx.files.internal("Items/Lettuce.png").exists());
    assertTrue("This test will only pass when the LettuceOnionSalad.png asset exists.",
            Gdx.files.internal("Items/LettuceOnionSalad.png").exists());
    assertTrue("This test will only pass when the LettuceTomatoSalad.png asset exists.",
            Gdx.files.internal("Items/LettuceTomatoSalad.png").exists());
    assertTrue("This test will only pass when the MeatBake.png asset exists.",
            Gdx.files.internal("Items/MeatBake.png").exists());
    assertTrue("This test will only pass when the MeatPizza.png asset exists.",
            Gdx.files.internal("Items/MeatPizza.png").exists());
    assertTrue("This test will only pass when the MeatPizzaCooked.png asset exists.",
            Gdx.files.internal("Items/MeatPizzaCooked.png").exists());
    assertTrue("This test will only pass when the MeatPotato.png asset exists.",
            Gdx.files.internal("Items/MeatPotato.png").exists());
    assertTrue("This test will only pass when the Mince.png asset exists.",
            Gdx.files.internal("Items/Mince.png").exists());
    assertTrue("This test will only pass when the Onion.png asset exists.",
            Gdx.files.internal("Items/Onion.png").exists());
    assertTrue("This test will only pass when the OvenActive.png asset exists.",
            Gdx.files.internal("Items/OvenActive.png").exists());
    assertTrue("This test will only pass when the PizzaBase.png asset exists.",
            Gdx.files.internal("Items/PizzaBase.png").exists());
    assertTrue("This test will only pass when the Potato.png asset exists.",
            Gdx.files.internal("Items/Potato.png").exists());
    assertTrue("This test will only pass when the RawPatty.png asset exists.",
            Gdx.files.internal("Items/RawPatty.png").exists());
    assertTrue("This test will only pass when the RepairTool.png asset exists.",
            Gdx.files.internal("Items/RepairTool.png").exists());
    assertTrue("This test will only pass when the SuperBurger.png asset exists.",
            Gdx.files.internal("Items/SuperBurger.png").exists());
    assertTrue("This test will only pass when the SuperPizza.png asset exists.",
            Gdx.files.internal("Items/SuperPizza.png").exists());
    assertTrue("This test will only pass when the SuperPotato.png asset exists.",
            Gdx.files.internal("Items/SuperPotato.png").exists());
    assertTrue("This test will only pass when the SuperSalad.png asset exists.",
            Gdx.files.internal("Items/SuperSalad.png").exists());
    assertTrue("This test will only pass when the ToastedBuns.png asset exists.",
            Gdx.files.internal("Items/ToastedBuns.png").exists());
    assertTrue("This test will only pass when the ToasterActive.png asset exists.",
            Gdx.files.internal("Items/ToasterActive.png").exists());
    assertTrue("This test will only pass when the Tomato.png asset exists.",
            Gdx.files.internal("Items/Tomato.png").exists());
    assertTrue("This test will only pass when the TomatoOnionLettuceSalad.png asset exists.",
            Gdx.files.internal("Items/TomatoOnionLettuceSalad.png").exists());
    assertTrue("This test will only pass when the TomatoOnionSalad.png asset exists.",
            Gdx.files.internal("Items/TomatoOnionSalad.png").exists());
    assertTrue("This test will only pass when the TomPizza.png asset exists.",
            Gdx.files.internal("Items/TomPizza.png").exists());
    assertTrue("This test will only pass when the TomSauce.png asset exists.",
            Gdx.files.internal("Items/TomSauce.png").exists());
    assertTrue("This test will only pass when the VegPizza.png asset exists.",
            Gdx.files.internal("Items/VegPizza.png").exists());
    assertTrue("This test will only pass when the VegPizzaCooked.png asset exists.",
            Gdx.files.internal("Items/VegPizzaCooked.png").exists());
  }

  /**
   * Tests that all assets in the Recipes folder exists
   * @Satisfies UR_CUSTOMER_ORDER
   * @author Azzam Amirul Bahri
   * @date 01/05/2023
   */

  @Test
  public void testRecipesAssetsExist(){
    assertTrue("This test will only pass when the BakedPotatoRecipe.png asset exists.",
            Gdx.files.internal("Recipes/BakedPotatoRecipe.png").exists());
    assertTrue("This test will only pass when the BurgerRecipe.png asset exists.",
            Gdx.files.internal("Recipes/BurgerRecipe.png").exists());
    assertTrue("This test will only pass when the CheeseBakeRecipe.png asset exists.",
            Gdx.files.internal("Recipes/CheeseBakeRecipe.png").exists());
    assertTrue("This test will only pass when the CheeseBurgerRecipe.png asset exists.",
            Gdx.files.internal("Recipes/CheeseBurgerRecipe.png").exists());
    assertTrue("This test will only pass when the EmptyRecipe.png asset exists.",
            Gdx.files.internal("Recipes/EmptyRecipe.png").exists());
    assertTrue("This test will only pass when the LettuceOnionSaladRecipe.png asset exists.",
            Gdx.files.internal("Recipes/LettuceOnionSaladRecipe.png").exists());
    assertTrue("This test will only pass when the LettuceTomatoSaladRecipe.png asset exists.",
            Gdx.files.internal("Recipes/LettuceTomatoSaladRecipe.png").exists());
    assertTrue("This test will only pass when the MeatBakeRecipe.png asset exists.",
            Gdx.files.internal("Recipes/MeatBakeRecipe.png").exists());
    assertTrue("This test will only pass when the TomatoOnionLettuceSaladRecipe.png asset exists.",
            Gdx.files.internal("Recipes/TomatoOnionLettuceSaladRecipe.png").exists());
  }

  /**
   * Tests that the picture for the controls page exists.
   * @Satisfies UR_HOW_TO_PLAY
   * @author Jack Vickers
   * @date 01/05/2023
   */
  @Test
  public void testControlsImageExists() {
    assertTrue("This test will only pass when the Controls.png asset exists.",
            Gdx.files.internal("Controls.png").exists());
  }

  /**
   * Tests that the picture for the icons page exists.
   * @Satisfies UR_HOW_TO_PLAY
   * @author Jack Vickers
   * @date 01/05/2023
   */
  @Test
  public void testIconsImageExists() {
    assertTrue("This test will only pass when the Icons.png asset exists.",
            Gdx.files.internal("Icons.png").exists());
  }

  /**
   * Tests that the icons exist for the button that opens the icon page.
   * @Satisfies UR_HOW_TO_PLAY
   * @author Jack Vickers
   * @date 01/05/2023
   */
  @Test
  public void testIconButtonAssetsExist() {
    assertTrue("This test will only pass when IconsUp.png asset exists.",
            Gdx.files.internal("IconsUp.png").exists());
    assertTrue("This test will only pass when IconsDown.png asset exists.",
            Gdx.files.internal("IconsDown.png").exists());
  }

}