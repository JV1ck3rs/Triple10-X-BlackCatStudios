package piazzapanictests.tests;

import com.mygdx.game.Core.Rendering.GameObjectManager;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import org.junit.runner.RunWith;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests to bin items at the bin.
 *
 * Satisfies requirements for UR_REMOVE_ITEMS and UR_INTERACTIONS
 *
 * @author Azzam Amirul Bahri
 * @date 26/04/2023
 */

@RunWith(GdxTestRunner.class)
public class TrashCanTests extends MasterTestClass {

    /**
     * Test all functions of the trash can and verfies they are functional
     * @satisfies UR_REMOVE_ITEMS UR_INTERACTIONS
     * @author Azzam Bahri
     * @date 26/04/23
     *
     */
    @Test
    public void testBinItemAtTrashCan() {
        if (GameObjectManager.objManager == null) {
            // creates game object manager which makes sure that the game object manager is not null when it is needed
            new GameObjectManager();
        }

        instantiateWorldAndTrashCan();
        assertTrue("Item is now binned", trashCan.giveItem(new Item(ItemEnum.Buns)));

        assertNull("Cannot pick up item from trashcan", trashCan.retrieveItem());

        assertTrue("Can bin item", trashCan.canGive());

        assertFalse("Cannot take any item from trashcan", trashCan.canRetrieve());

        assertFalse("Cannot interact with trashcan", trashCan.canInteract());

        assertEquals("Cannot interact with trashcan", 0.0, trashCan.interact(), 0.1);
    }
}
