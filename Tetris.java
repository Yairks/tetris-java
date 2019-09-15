package tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Yair on 11/23/2016.
 */
public class Tetris extends Application {
    static long startTime, currentTime, timeToAddPiece, destroyingTime;
    Rectangle border;
    public static List<TetrisShape> pieces;
    private Scene scene;
    public static Group rootnode;
    static private int destroyedRows, points;
    private Label instructions, stopWatch, pointsScored;
    public static boolean gameover;
    private static Random random;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        startTime = currentTime = destroyingTime = timeToAddPiece = destroyedRows = points = 0;
        gameover = false;

        rootnode = new Group();

        pieces = new ArrayList<TetrisShape>();

        addPiece();

        rootnode.prefWidth(100);
        rootnode.prefHeight(75);

        setUpLayout();

        scene = new Scene(rootnode, 400, 600, Color.CADETBLUE);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //If the key pressed is not an arrow key, exit the method now.
                if(event.getCode().isArrowKey())
                    for(TetrisShape ts : pieces)
                        ts.moveBlocks(event.getCode().getName());
                else if(event.getCode().getName() == "Space")
                    for(TetrisShape ts : pieces)
                        ts.rotate();
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(currentTime==0)
                    startTime = currentTime = now;

                if(!gameover) {
                    stopWatch.setText("Time: " + (int) ((now - startTime) / 1000000000) + " seconds");
                }

                //It will get steadily faster and faster...
                if((double) (now - currentTime)/1000000000 >= .75 - Math.pow(0.005 * (double) ((now - startTime)/1000000000 < 150 ?
                        (now - startTime)/1000000000 : 150) , 2)) {
                    for (TetrisShape ts : pieces)
                        ts.moveBlocks("Down");
                    currentTime = now;
                }

                if(timeToAddPiece())
                    addPiece();

                if(rowForDestruction() != -1 && destroyingTime == 0) {
                    System.out.println("Destroying row " + rowForDestruction());
                    destroyingTime = now;
                }

                if(destroyingTime != 0 && now - destroyingTime >= 1000000000) {
                    destroyBlocks(rowForDestruction());
                    destroyingTime = 0;
                }
            }
        };
        timer.start();

        stage.setScene(scene);
        stage.show();
    }

    private void createTetrisPiecesList() {
        List<TetrisShape> pieces = new ArrayList<TetrisShape>();
    }

    public static void addPiece() {

        TetrisShape ts = TetrisShape.getNewShape();
        pieces.add(ts);
        rootnode.getChildren().addAll(ts.blocks);
    }

    private double rowForDestruction() {
        int blocks = 0;

        for(double y = 20; y <= 400; y += 20) {
            for (TetrisShape ts : pieces) {
                for (Block block : ts.blocks) {
                    //First, check that the block is not moving.
                    if(!ts.isSolidified())
                        break;
                    //There needs to be 30 blocks for a row to be destroyed.
                    if(block.getBoundsInParent().getMaxY() == y) {
                        blocks++;
                    }

                    if(blocks == 10)
                        return y;
                }
            }
            blocks = 0;
        }
        //If no row is found...
        return -1;
    }

    private void destroyBlocks(double row) {
        if(row == -1)
            return;

        //There are 2 lists to be removed from.
        int blocks = 0;

        rootnode.getChildren().removeIf((Node node) -> {
            if(node.getBoundsInParent().getMaxY() == row &&
                    node.getBoundsInParent().getMinY() == row - 20)
                return true;
            return false;
        });

        for(TetrisShape ts : pieces) {
            ts.blocks.removeIf((Block block) -> (block.getBoundsInParent().getMaxY() == row));
        }

        System.out.println("\nBlocks destroyed: " + blocks);

        //Now, make all the rows on top move down 1 row.
        for(TetrisShape ts : pieces) {
            for(Block block : ts.blocks) {
                if(block.getBoundsInParent().getMaxY() < row)
                    block.drop();
            }
        }

        destroyedRows++;
        points += 1 + (currentTime - startTime)/5000000000L;
        if(!gameover) pointsScored.setText("Rows destroyed: " + destroyedRows + "\nPoints Scored: " + points);
    }

    private boolean timeToAddPiece() {
        for(TetrisShape ts : pieces) {
            if(!ts.isSolidified())
                return false;
        }
        if(timeToAddPiece == 0)
            timeToAddPiece = System.nanoTime();

        if(timeToAddPiece != 0 && System.nanoTime() - timeToAddPiece >= 1000000000) {
            timeToAddPiece = 0;
            return true;
        }

        return false;
    }

    private void setUpLayout() {
        stopWatch = new Label();
        stopWatch.setText("Time: " + (currentTime - startTime));
        stopWatch.relocate(220, 100);
        stopWatch.setFont(Font.font(14));

        pointsScored = new Label();
        pointsScored.setText("Points Scored: " + destroyedRows);
        pointsScored.relocate(220, 150);
        pointsScored.setFont(Font.font(14));

        instructions = new Label();
        instructions.setText("Use the arrow keys to move.\nPress space to rotate.");
        instructions.relocate(10, 400);
        instructions.setFont(Font.font("arial", FontWeight.EXTRA_BOLD, 14));

        border = new Rectangle(0, 0, 200, 400);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(2);

        rootnode.getChildren().addAll(stopWatch, pointsScored, instructions, border);

        random = new Random();
    }

    public static void lose() {
        rootnode.getScene().setFill(Color.CHOCOLATE);
        Label youLose = new Label("LOSER");
        youLose.relocate(200, 300);
        youLose.setFont(Font.font(18));

        Button playAgain = new Button("Click to play again");
        playAgain.relocate(200, 200);
        playAgain.setPrefSize(175, 40);

        rootnode.getChildren().addAll(youLose, playAgain);

        playAgain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Remove all Blocks from the rootnode
                rootnode.getChildren().removeIf((Node node) -> (node.getClass().equals(new Block().getClass())));
                //Remove all TetrisShapes from pieces.
                pieces.removeIf((TetrisShape ts) -> (true));
                //Turn screen back to normal.
                rootnode.getScene().setFill(Color.CADETBLUE);
                //Reset all the important numbers.
                startTime = currentTime = destroyingTime = timeToAddPiece = destroyedRows = points = 0;
                //Remove the button.
                rootnode.getChildren().remove(playAgain);
                rootnode.getChildren().remove(youLose);
                addPiece();
                gameover = false;
            }
        });
        gameover = true;
    }
}