package piazzapanictests.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.Key;

import static com.mygdx.game.Core.Inputs.*;
import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class InputKeysTests {

    @Test
    public void SpawnNewChefTest(){
        assertEquals("New chef spawned", SPAWN_NEW_CHEF, Keys.MINUS);
    }

    @Test
    public void MoveChefInputsTests(){
        assertEquals("Move chef up", MOVE_CHEF_UP, Keys.W);
        assertEquals("Move chef down", MOVE_CHEF_DOWN, Keys.S);
        assertEquals("Move chef left", MOVE_CHEF_LEFT, Keys.A);
        assertEquals("Move chef right", MOVE_CHEF_RIGHT, Keys.D);
    }

    public void CycleStackInputsTests(){
        assertEquals("Should be W key", CYCLE_STACK, Keys.W);

    }

    public void ChefInteractInputs(){
        assertEquals("Should be Q key", GIVE_ITEM, Keys.Q);
        assertEquals("Should be E key", FETCH_ITEM, Keys.E);
        assertEquals("Should be F key", DROP_ITEM, Keys.F);
        assertEquals("Should be spacebar", INTERACT, Keys.SPACE);
    }

}
