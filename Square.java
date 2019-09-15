package tetris;

/**
 * Created by Yair on 11/27/2016.
 */
public class Square extends TetrisShape {
    Square() {
        super();

        blocks.add(new Block());
        blocks.add(new Block(120, 0, blocks.get(0).getFill()));
        blocks.add(new Block(100, 20, blocks.get(0).getFill()));
        blocks.add(new Block(120, 20, blocks.get(0).getFill()));
    }

    @Override
    public void rotate() {
        //Squares don't rotate, idiot.
    }
}
