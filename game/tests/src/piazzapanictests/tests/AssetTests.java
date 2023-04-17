package piazzapanictests.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests that the assets are present.
 */
@RunWith(GdxTestRunner.class)
public class AssetTests {

  /**
   * Tests that the chef assets are present.
   */
  @Test
  public void testChefAssetExists() {
    assertTrue("This test will only pass when the chef1.png asset exists.",
        Gdx.files.internal("Chefs/Chef1/chef1.png").exists());
  }

  /**
   * Tests that the skin json asset exists. This is used for the UI on the
   * scenarioModeConfigScreen.
   *
   * @author Jack Vickers
   */
  @Test
  public void testSkinJsonExists() {
    assertTrue("This test will only pass when the skin.json asset exists.",
        Gdx.files.internal("clean-crispy-ui.json").exists());
  }

  /**
   * Tests that the skin atlas asset exists. This is used for the UI on the
   * scenarioModeConfigScreen.
   *
   * @author Jack Vickers
   */
  @Test
  public void testSkinAtlasExists() {
    assertTrue("This test will only pass when the skin.atlas asset exists.",
        Gdx.files.internal("clean-crispy-ui.atlas").exists());
  }

  /**
   * Tests that the skin image asset exists. This is used for the UI on the
   * scenarioModeConfigScreen.
   *
   * @author Jack Vickers
   */
  @Test
  public void testSkinImageExists() {
    assertTrue("This test will only pass when the skin.png asset exists.",
        Gdx.files.internal("clean-crispy-ui.png").exists());
  }

  /**
   * Tests that the skin font asset exists. This is used for the UI on the
   * scenarioModeConfigScreen.
   *
   * @author Jack Vickers
   */
  @Test
  public void testSkinFontExists() {
    assertTrue("This test will only pass when the skin.fnt asset exists.",
        Gdx.files.internal("font-export.fnt").exists());
  }

  /**
   * Tests that the assets for the load game button are present. This button is displayed on the
   * title screen.
   *
   * @author Jack Vickers
   */
  @Test
  public void testLoadGameButtonImagesExist() {
    assertTrue("This test will only pass when the LoadUp.png asset exists.",
        Gdx.files.internal("LoadUp.png").exists());
    assertTrue("This test will only pass when the LoadDown.png asset exists.",
        Gdx.files.internal("LoadDown.png").exists());
  }

  /**
   * Tests that the atlas for the main menu exists. Also used for
   * the scenario mode config screen.
   *
   * @author Jack Vickers
   */
  @Test
  public void testMainMenuAtlasExists() {
    assertTrue("This test will only pass when the mainmenu.atlas asset exists.",
        Gdx.files.internal("mainMenu.atlas").exists());
  }

  /**
   * Tests that the image exists which is referenced by the main menu atlas.
   *
   * @author Jack Vickers
   */
  @Test
  public void testMainMenuImageExists() {
    assertTrue("This test will only pass when the mainMenu.png asset exists.",
        Gdx.files.internal("mainMenu.png").exists());
  }

  /**
   * Tests that the images for the play button on the main menu and scenario config screens
   * can be found using the texture atlas.
   *
   * @author Jack Vickers
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
   *
   * @author Jack Vickers
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
   *
   * @author Jack Vickers
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
   *
   * @author Jack Vickers
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
   *
   * @author Jack Vickers
   */
  @Test
  public void testPauseButtonImagesExist() {
    assertTrue("This test will only pass when the PauseUp.png asset exists.",
        Gdx.files.internal("PauseUp.png").exists());
    assertTrue("This test will only pass when the PauseDown.png asset exists.",
        Gdx.files.internal("PauseDown.png").exists());
  }

  //TODO: add tests for pause screen assets
}