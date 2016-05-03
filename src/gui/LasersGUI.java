package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.LasersModel;

import java.util.Observable;
import java.util.Observer;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the model
 * and receives updates from it.
 *
 * @author Sean Strout @ RIT CS
 * @author Bryan Quinn
 * @author Chris Cassidy
 */
public class LasersGUI extends Application implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /** this can be removed - it is used to demonstrates the button toggle */
    private static boolean status = true;
    private BorderPane border;
    private Button[][] buttons;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        Parameters params = getParameters();
        String filename = params.getRaw().get(0);
        this.model = new LasersModel(filename);
        this.model.addObserver(this);
        border = new BorderPane();
        buttons = new Button[model.getRows()][model.getColumns()];
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * This is a private demo method that shows how to create a button
     * and attach a foreground image with a background image that
     * toggles from yellow to red each time it is pressed.
     *
     * @param stage the stage to add components into
     */
    /*private void buttonDemo(Stage stage) {
        // this demonstrates how to create a button and attach a foreground and
        // background image to it.
        Button button = new Button();
        Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
        ImageView laserIcon = new ImageView(laserImg);
        button.setGraphic(laserIcon);
        setButtonBackground(button, "yellow.png");
        button.setOnAction(e -> {
            // toggles background between yellow and red
            if (!status) {
                setButtonBackground(button, "yellow.png");
            } else {
                setButtonBackground(button, "red.png");
            }
            status = !status;
        });

        Scene scene = new Scene(button);
        stage.setScene(scene);
    }*/

    /**
     * The
     * @param stage the stage to add UI components into
     */
     private void init(Stage stage) {
        // TODO
        //buttonDemo(stage);  // this can be removed/altered
     }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO
        init(primaryStage);  // do all your UI initialization here

        GridPane grid = new GridPane();
        //grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5); grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);
        for (int row = 0; row < this.model.getRows() ; row++) {
            grid.addColumn(row);
            for (int col = 0; col < this.model.getColumns(); col++) {
                Button current = new Button();
                current.setPadding(new Insets(10,10,10,10));
                switch (model.getGrid(row,col)){
                    case '0':
                        setButtonBackground(current, "pillar0.png");
                        break;
                    case '1':
                        setButtonBackground(current, "pillar1.png");
                        break;
                    case '2':
                        setButtonBackground(current, "pillar2.png");
                        break;
                    case '3':
                        setButtonBackground(current, "pillar3.png");
                        break;
                    case '4':
                        setButtonBackground(current, "pillar4.png");
                        break;
                    case 'X':
                        setButtonBackground(current, "pillarX.png");
                        break;
                    default:
                        setButtonBackground(current, "white.png");
                        int finalCol = col;
                        int finalRow = row;
                        current.setOnAction(e -> setLaser(finalRow, finalCol));
                }
                buttons[row][col] = current;
                grid.add(current, row, col);
                //Rectangle rect = new Rectangle(50, 50, Color.GRAY);
                //int finalRow = model.getRemainingGuesses() - row;
                //int finalCol = col + 1;
                //rect.setOnMouseClicked(event -> model.choose(finalRow, finalCol));
                //grid.add(rect,col,row);
                //guesses[col][row] = rect;
            }
        }

        Button check = new Button("Check");
        Button hint = new Button("Hint");
        Button solve = new Button("Solve");
        Button restart = new Button("Restart");
        Button load = new Button("Load");


        check.setOnAction(e -> check());
        hint.setOnAction(e -> hint());
        solve.setOnAction(e -> solve());
        restart.setOnAction(e -> restart());
        load.setOnAction(e -> load());

        HBox bottom = new HBox();
        bottom.setSpacing(10);
        bottom.getChildren().addAll(check, hint, solve, restart, load);
        bottom.setPadding(new Insets(10,10,10,10));

        border.setTop(new Label("put something here"));
        border.setPadding(new Insets(10,10,10,10));
        border.setBottom(bottom);
        border.setCenter(grid);

        Scene scene = new Scene(border);
        primaryStage.setTitle("Lasers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Image schuyler = new Image(getClass().getResourceAsStream("resources/Schuyler.png"));
        primaryStage.getIcons().add((schuyler));
        primaryStage.show();
    }

    private void load() {
        //TODO
    }

    private void restart() {
        //TODO
    }

    private void solve() {
        //TODO
    }

    private void hint() {
        //TODO
    }

    private void check() {
        //TODO
    }

    private void setLaser(int col, int row) {
        System.out.println("test");
        //TODO
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO
    }
}
