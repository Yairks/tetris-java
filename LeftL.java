package tetris;

import javafx.scene.transform.Translate;

/**
 * Created by Yair on 11/27/2016.
 */
public class LeftL extends TetrisShape {
    LeftL() {
        super();

        blocks.add(new Block());
        blocks.add(new Block(100, 20, blocks.get(0).getFill()));
        blocks.add(new Block(100, 40, blocks.get(0).getFill()));
        //This following Block makes it a left facing L
        blocks.add(new Block(80, 40, blocks.get(0).getFill()));

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
            blocks.get(2).getTransforms().add(new Translate(-20, -20));
            blocks.get(3).getTransforms().add(new Translate(0, -40));
        }
        else if(rotations % 4 == 1) {
            blocks.get(0).getTransforms().add(new Translate(-20, 20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(20, -20));
            blocks.get(3).getTransforms().add(new Translate(40, 0));
        }
        else if(rotations % 4 == 2) {
            blocks.get(0).getTransforms().add(new Translate(-20, -20));
            blocks.get(1).getTransforms().add(new Translate(0, 0));
            blocks.get(2).getTransforms().add(new Translate(20, 20));
            blocks.get(3).getTransforms().add(new Translate(0, 40));
        }
        else if(rotations % 4 == 3) {
            blocks.get(0).getTransforms().add(new Translate(20, -20));
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
