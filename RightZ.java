package tetris;

import javafx.scene.transform.Translate;

/**
 * Created by Yair on 11/27/2016.
 */
public class RightZ extends TetrisShape {
    RightZ() {
        super();

        blocks.add(new Block());
        //Make the shape a Right L
        blocks.add(new Block(80, 0, blocks.get(0).getFill()));
        blocks.add(new Block(80, 20, blocks.get(0).getFill()));
        blocks.add(new Block(60, 20, blocks.get(0).getFill()));

        MightBeGameOver();
    }

    @Override
    public void rotate() {
        if(isTouchingWall())
            return;

        if(isSolidified()) return;

        if(rotations % 2 == 0) {
            blocks.get(0).getTransforms().add(new Translate(-20, -20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(20, -20));
            blocks.get(3).getTransforms().add(new Translate(40, 0));
        }
        else {
            blocks.get(0).getTransforms().add(new Translate(20, 20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(-20, 20));
            blocks.get(3).getTransforms().add(new Translate(-40, 0));
        }

        if(isTouchingBlock(Tetris.pieces)) {
            hit();
            return;
        }

        if(isTouchingWall()) {
            wallKick();
        }

        rotations++;
    }
}
