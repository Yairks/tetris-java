package tetris;

import javafx.scene.transform.Translate;

/**
 * Created by Yair on 11/27/2016.
 */
public class LeftZ extends TetrisShape {
    LeftZ() {
        super();

        blocks.add(new Block());
        //Make it face to the Left
        blocks.add(new Block(120, 0, blocks.get(0).getFill()));
        blocks.add(new Block(120, 20, blocks.get(0).getFill()));
        blocks.add(new Block(140, 20, blocks.get(0).getFill()));

        MightBeGameOver();
    }

    @Override
    public void rotate() {
        if(isTouchingWall())
            return;

        if(isSolidified()) return;

        if(rotations % 2 == 0) {
            blocks.get(0).getTransforms().add(new Translate(20, 20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(20, -20));
            blocks.get(3).getTransforms().add(new Translate(0, -40));
        }
        else {
            blocks.get(0).getTransforms().add(new Translate(-20, -20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(-20, 20));
            blocks.get(3).getTransforms().add(new Translate(0, 40));
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
