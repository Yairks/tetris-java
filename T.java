package tetris;

import javafx.scene.transform.Translate;

/**
 * Created by Yair on 11/27/2016.
 */
public class T extends TetrisShape {
    T() {
        super();

        blocks.add(new Block());
        blocks.add(new Block(100, 20, blocks.get(0).getFill()));
        blocks.add(new Block(80, 20, blocks.get(0).getFill())); //Yes, I know its out of order. It's important for later.
        blocks.add(new Block(120, 20, blocks.get(0).getFill()));

        MightBeGameOver();
    }

    @Override
    public void rotate() {
        if(isTouchingWall())
            return;

        if(isSolidified()) return;

        if(rotations % 4 == 0) {
            blocks.get(0).getTransforms().add(new Translate(20, 20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(20, -20));
            blocks.get(3).getTransforms().add(new Translate(-20, 20));
        }
        else if(rotations % 4 == 1) {
            blocks.get(0).getTransforms().add(new Translate(-20, 20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(20, 20));
            blocks.get(3).getTransforms().add(new Translate(-20, -20));
        }
        else if(rotations % 4 == 2) {
            blocks.get(0).getTransforms().add(new Translate(-20, -20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(-20, 20));
            blocks.get(3).getTransforms().add(new Translate(20, -20));
        }
        else if(rotations % 4 == 3) {
            blocks.get(0).getTransforms().add(new Translate(20, -20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(-20, -20));
            blocks.get(3).getTransforms().add(new Translate(20, 20));
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
