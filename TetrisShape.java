package tetris;

import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TetrisShape provides the basic methods for a shape -
 * it contains the move() method among others, and the
 * abstract rotate() method to rotate. Originally, all
 * the different types of shapes were defined and created
 * in this class, but it got too confusing, so I opted for
 * a simpler, delegation-oriented outline.
 *
 * Created by Yair on 11/23/2016.
 */
public abstract class TetrisShape {
    public List<Block> blocks;
    private boolean solidified;
    protected int rotations;

    TetrisShape() {
        blocks = new ArrayList<>();
        solidified = false;

        rotations = 0;
    }

    /**
     * Easy way to get new TetrisShapes
     *
     * @return
     */
    public static TetrisShape getNewShape() {
        Random random = new Random();

        switch(random.nextInt(7)) {
            case 0:
                return new Line();
            case 1:
                return new RightL();
            case 2:
                return new LeftL();
            case 3:
                return new RightZ();
            case 4:
                return new LeftZ();
            case 5:
                return new T();
            case 6:
                return new Square();
        }
        return null;
    }

    protected final void MightBeGameOver() {

        //This super complex for-loop thingy just checks to make sure the game isn't over
        //If a just created TetrisShape already contains part of another, Tetris.gameover is set to true
        //and Tetris.lose() is called.
        for(Block block : blocks) {
            for (TetrisShape ts : Tetris.pieces) {
                for (Block otherBlock : ts.blocks) {
                    if (block.getBoundsInParent().contains(otherBlock.getBoundsInParent())) {
                        if (!Tetris.gameover) {
                            Tetris.gameover = true;
                            Tetris.lose();
                            break;
                        }
                    }
                }
            }
        }
    }

    public final void solidify() { solidified = true; }
    public final void unsolidify() { solidified = false; }

    public final boolean isSolidified() { return solidified; }

    /**
     * Method for moving all the blocks in a TetrisShape. Method
     * will not work for a solidified TetrisShape.
     *
     * If the TetrisShape is touching the wall (meaning that it is
     * actually beyond - not just at -  the boundaries of the wall),
     * hit() is called and the method is concluded.
     *
     * The method operates by moving each Block in the direction
     * provided to it. It then checks to see if any block is touching
     * another Block or is outside the boundaries of the game.
     *
     * @param whichWay
     */
    public final void moveBlocks(String whichWay) {
        //First make sure the TetrisPiece is unsolidified
        if(solidified)
            return;

        //Next, see if it is touching the wall.
        //If it is, the TetrisPiece cannot move
        //so the method returns.
        //The reason that isTouchingWall is called at the beginning
        //of the method is so that after the move has been made and the
        //TetrisShape is touching the bottom, it can still be moved around
        //until move() is automatically called another time.
        for(int i = 0; i < blocks.size(); i++) {
            if(this.isTouchingWall()) { //Adding "this." seems to make it flow better...Why not?
                hit();
                return;
            }

            if(i == blocks.size() - 1 && isSolidified())
                return;
        }

        switch(whichWay) {
            case "Up":
                //Nothing yet. Hopefully something soon.
                break;
            case "Down":
                //For each direction, each block is moved in the proper
                //direction, and then is checked to make sure that that
                //move was legit.
                for (Block block : blocks) {
                    block.getTransforms().add(new Translate(0, 20));
                }
                break;
            case "Right":
                for (Block block : blocks) {
                    block.getTransforms().add(new Translate(20, 0));
                }
                break;
            case "Left":
                for (Block block : blocks) {
                    block.getTransforms().add(new Translate(-20, 0));
                }
                break;
            default:
                //It should never come to the default. I can't think of a scenario where it would.
        }
        if(isTouchingBlock(Tetris.pieces) || isTouchingWall()) {
            hit(whichWay);
            return;
        }
    }

    //This method is called before and after any move or rotation is done.
    protected final boolean isTouchingWall() {
        for(Block block : blocks) {
            if(block.getBoundsInParent().getMaxY() > 400 ||
                    block.getBoundsInParent().getMinX() < 0 ||
                    block.getBoundsInParent().getMaxX() > 200
                    ) return true;
        }
        return false;
    }

    //This method determines if the TetrisShape is touching another solidified TetrisShape.
    protected final boolean isTouchingBlock(List<TetrisShape> pieces) {
        //Caution: triple nested for loop. Watch your step.
        //For every Block in this TetrisShape, check that it's not touching
        //any other Block in any other TetrisShape.
        for(Block block : blocks) {
            for (TetrisShape ts : pieces) {
                for (Block otherBlock : ts.blocks) {
                    if (block.getBoundsInParent().contains(otherBlock.getBoundsInParent())
                            && ts.isSolidified()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Hit another block or a wall. First, the move must be undone,
     * then the TetrisShape is solidified.
     */
    protected final void hit() {
        hit("It was a rotate, dummy.");
        //Since it was only a rotate, it does not solidify
        unsolidify();
    }

    protected final void hit(String direction) {
        for (int i = 0; i <= 4; i++) {
            try {
                blocks.get(i).getTransforms().remove(blocks.get(i).getTransforms().size() - 1);
            } catch (IndexOutOfBoundsException exc) {
                //This shouldn't happen all that often. I'm not sure why it happened in the first place.
            }
        }

        if(direction.equals("Down"))
            solidify();
    }

    /**
     * Method for making a TetrisShape rotate. Currently,
     * two directional rotation are not allowed. If you
     * want to rotate, you get one direction. Forward.
     *
     * Each subclass defines the method a bit differently.
     */
    public abstract void rotate();

    /**
     * Special kind of rotation for when the TetrisShape
     * is on the wall. Naming creds go to Wikipedia.
     */
    public void wallKick() {
        if(blocks.get(0).getBoundsInParent().getMaxY() == 400)
            return;

        else if(blocks.get(0).getBoundsInParent().getMaxX() > 100) {
            for(Block block : blocks)
                block.getTransforms().add(new Translate(-10, 0));
        }

        else {
            for(Block block : blocks)
                block.getTransforms().add(new Translate(10, 0));
        }

        //The max number of times wallKick() will be called on a
        //TetrisShape is twice in a row. In those situations,
        //hit() must be called three time: twice for the wallKick()s
        //and once for the rotate(). The first 2 are called here. The
        //third (if needed will be called after the second wallKick() returns.
        if(isTouchingBlock(Tetris.pieces)) {
            hit();
            hit();
            //Subtract from the rotations since this rotate() failed. :(
            rotations--;
            return;
        }

        //If it's still touching the wall, then...
        if(isTouchingWall())
            wallKick();

        if(isTouchingBlock(Tetris.pieces))
            hit();
    }
}
