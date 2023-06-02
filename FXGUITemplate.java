import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.security.Key;

/**
 *App with Graphical User Interfaces.
 * 
 * @author Tristan Smith 
 */
public class FXGUITemplate extends Application {

    public static final int SQUARE_SIZE = 50;
    public static final int SIZE = 4; // modify these as you like should not cause issues

    public NumberSquare game = new NumberSquare(SIZE); // create game object
    public Button[] grid = new Button[game.size * game.size]; // make array of buttons at correct size

    public Integer highScore = null;
    public double bestTime = -1; // memory values

    public int currentSelection = -1; // for keeping track of which tile to swap

    public Button goButton;
    public Label scoreLabel;
    public Label highScoreLabel;
    public Label timeLabel;
    public Label bestTimeLabel;
    public Label winLabel; // labels & go button definition

    /**
     * This is where you create your components and the model and add event
     * handlers.
     *
     * @param stage The main stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        GridPane root = new GridPane();
        root.setHgap(5);
        root.setVgap(5);
        root.setPadding(new Insets(5, 5, 5, 5)); // padding
        Scene scene = new Scene(root, SQUARE_SIZE * game.size + 270, SQUARE_SIZE * game.size); // set the size here
        stage.setTitle("Number Square"); // set the window title here
        stage.setScene(scene);

        for (int i = 0; i < grid.length; i++) { // make grid buttons
            grid[i] = new Button("NULL");
            grid[i].setPrefSize(SQUARE_SIZE, SQUARE_SIZE); // set size
            int finalI = i; // make finalized version of i for lambda
            grid[i].setOnAction(event -> { // run on click
                if (game.isWin() == -1) { // dont allow moves if game is won
                    if (currentSelection < 0) { // if we have not selected a tile to swap with
                        currentSelection = finalI;
                        grid[currentSelection].setEffect(new DropShadow());
                        grid[currentSelection].toFront(); // select this tile and outline it
                    } else {
                        game.action(currentSelection, finalI);
                        grid[currentSelection].setEffect(null);
                        currentSelection = -1; // make swap and unoutline tiles
                    }
                }
            });
            int[] coords = game.getCoords(i); // get coordinates of this tiles for grid placement
            root.add(grid[i], coords[0], coords[1]); // add tile
        }
        goButton = new Button("GO");
        goButton.setOnAction(event -> {
            game.randomize();
        });
        root.add(goButton, grid.length, 2); // make go button & add start game to click
        scoreLabel = new Label("Score: NULL");
        root.add(scoreLabel, grid.length, 0);
        highScoreLabel = new Label("High Score: NULL");
        root.add(highScoreLabel, grid.length + 1, 0);
        timeLabel = new Label("Time: NULL");
        root.add(timeLabel, grid.length, 1);
        bestTimeLabel = new Label("Best Time: NULL");
        root.add(bestTimeLabel, grid.length + 1, 1);
        winLabel = new Label("");
        root.add(winLabel, grid.length, 3); // create and add labels

        Timeline timer = new Timeline(new KeyFrame(Duration.millis(10), event -> { // repeat every 10ms
            update(); // call update
            if (game.startTime != -1 && game.isWin() != -1) {
                highScore = Math.max(highScore != null ? highScore : 0, game.score);
                bestTime = Math.min(bestTime != -1 ? bestTime : Double.MAX_VALUE, game.isWin());
            }
        }));
        timer.setCycleCount(Animation.INDEFINITE); // repeat updater forever
        timer.play(); // start updater

        stage.show();
    }

    public void update() { // update labels and buttons
        for (int i = 0; i < grid.length; i++) {
            int number = game.grid[i];
            if (number >= 0) grid[i].setText(String.valueOf(number + 1));
            else grid[i].setText("");
        } // update number grid
        scoreLabel.setText(String.format("Score: %d", game.startTime != -1 ? game.score : 0));
        highScoreLabel.setText(String.format("High Score: %d", highScore != null ? highScore : 0));
        timeLabel.setText(String.format("Time: %.2fs", game.startTime != -1 ? (game.isWin() != -1 ? game.isWin() : ((double) System.currentTimeMillis() / 1000F) - game.startTime) : 0)); // if game is won, show winning time, otherwise show time since start
        bestTimeLabel.setText(String.format("Best Time: %.2fs", bestTime != -1 ? bestTime : 0));
        if (game.startTime != -1 && game.isWin() != -1) winLabel.setText("You Win!"); // if won show "You Win!"
        else winLabel.setText("");
    }

    /**
     * Make no changes here.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}
