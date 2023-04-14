package piazzapanictests.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
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
}