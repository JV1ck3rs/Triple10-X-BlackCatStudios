package piazzapanictests.tests;

import com.mygdx.game.Core.GameObjectManager;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemEnum;
import com.mygdx.game.Stations.TrashCan;
import org.junit.runner.RunWith;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests to bin items at the bin.
 *
 * Satisfies requirements for UR_REMOVE_ITEMS and UR_INTERACTIONS
 *
 * @author Azzam Amirul Bahri
 */

@RunWith(GdxTestRunner.class)
public class TrashCanTests extends MasterTestClass {

    @Test
    public void testBinItemAtTrashCan() {
        if (GameObjectManager.objManager == null) {
            // creates game object manager which makes sure that the game object manager is not null when it is needed
            new GameObjectManager();
        }

        instantiateWorldAndTrashCan();
        assertTrue("Item is now binned", trashCan.GiveItem(new Item(ItemEnum.Buns)));

        assertNull("Cannot pick up item from trashcan", trashCan.RetrieveItem());

        assertTrue("Can bin item", trashCan.CanGive());

        assertFalse("Cannot take any item from trashcan", trashCan.CanRetrieve());

        assertFalse("Cannot interact with trashcan", trashCan.CanInteract());

        assertEquals("Cannot interact with trashcan", 0.0, trashCan.Interact(), 0.1);
    }
}
