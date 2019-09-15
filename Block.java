package tetris;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Translate;

/**
 * This class represents one single block in the tetris
 * game. It is useful because the original shape of a tetris
 * block becomes meaningless when it hits the bottom.
 *
 * Created by Yair on 11/23/2016.
 */
public class Block extends Rectangle {
    private Random random;

    /**
     * A generic block with an unspecified color.
     */
    Block() {
        super(100, 0, 20, 20);
        random = new Random();
        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.BLUEVIOLET, Color.YELLOW};
        setFill(colors[random.nextInt(5)]);
        setStroke(Color.BLACK);
        setStrokeWidth(1);
        setStrokeType(StrokeType.INSIDE);
    }

    /**
     * A block with a specific color and location.
     * @param fill
     */
    Block(double x, double y, Paint fill) {
        super(x, y, 20, 20);
        random = new Random();
        setFill(fill);
        setStroke(Color.BLACK);
        setStrokeWidth(1);
        setStrokeType(StrokeType.INSIDE);
    }

    /**
     * Method for causing a Block to drop
     * after a row has been destroyed.
     */
    public void drop() {
        getTransforms().add(new Translate(0, 20));
    }

    /**
     * Called when the row is filled up.
     */
    public void selfDestruct() {
        for(TetrisShape ts : Tetris.pieces) {
            for(int i = 0; i < ts.blocks.size(); i++) {
                if(ts.blocks.get(i).equals(this)) {
                    ts.blocks.remove(i);
                }
            }
        }
    }
}
