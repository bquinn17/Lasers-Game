package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.LasersModel;

import java.io.File;
import java.util.ArrayList;
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
    private Label message;
    private File file;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        Parameters params = getParameters();
        String filename = params.getRaw().get(0);
        this.model = new LasersModel(filename);
        this.model.addObserver(this);
        border = new BorderPane();
        buttons = new Button[model.getColumns()][model.getRows()];
        status = true;
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        Image laserImg = new Image(getClass().getResourceAsStream("resources/"+ bgImgName));
        ImageView laserIcon = new ImageView(laserImg);
        button.setGraphic(laserIcon);
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
        //buttonDemo(stage);  // this can be removed/altered
     }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);  // do all your UI initialization here

        GridPane grid = makeButttons();
        //grid.setPadding(new Insets(10, 10, 10, 10));
        //grid.setHgap(0); grid.setVgap(0);
        grid.setAlignment(Pos.CENTER);

        Button check = new Button("Check");
        Button hint = new Button("Hint");
        Button solve = new Button("Solve");
        Button restart = new Button("Restart");
        Button load = new Button("Load");

        check.setOnAction(e -> check());
        hint.setOnAction(e -> hint());
        solve.setOnAction(e -> solve());
        restart.setOnAction(e -> restart());
        load.setOnAction(e -> load(primaryStage));

        HBox bottom = new HBox();
        bottom.setSpacing(10);
        bottom.getChildren().addAll(check, hint, solve, restart, load);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(10,10,10,10));

        message = new Label("");
        message.setTextAlignment(TextAlignment.CENTER);
        HBox top = new HBox();
        top.getChildren().add(message);
        top.setAlignment(Pos.CENTER);

        border.setTop(top);
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

    private GridPane makeButttons(){
        GridPane grid = new GridPane();
        for (int row = 0; row < this.model.getRows() ; row++) {
            //grid.addColumn(row);
            for (int col = 0; col < this.model.getColumns(); col++) {
                Button current = new Button();
                current.setPadding(new Insets(10,10,10,10));
                buttonToPic(current, model.getGridAtPos(col,row), false);
                int finalCol = col;
                int finalRow = row;
                current.setOnAction(e -> setLaser(finalCol, finalRow));
                buttons[col][row] = current;
                grid.add(current, col, row);
            }
        }
        return grid;
    }

    private void buttonToPic(Button button, char ch, boolean isRed){
        if (isRed){
            setButtonBackground(button, "red.png");
            System.out.println("schuylerhjhyyhjuu");
        } else {
            setButtonBackground(button, "white.png");
        }
        switch (ch){
            case '0':
                Image zeroImg = new Image(getClass().getResourceAsStream("resources/pillar0.png"));
                ImageView zeroIcon = new ImageView(zeroImg);
                button.setGraphic(zeroIcon);
                //setButtonBackground(button, "pillar0.png");
                break;
            case '1':
                Image oneImg = new Image(getClass().getResourceAsStream("resources/pillar1.png"));
                ImageView oneIcon = new ImageView(oneImg);
                button.setGraphic(oneIcon);
                //setButtonBackground(button, "pillar1.png");
                break;
            case '2':
                Image twoImg = new Image(getClass().getResourceAsStream("resources/pillar2.png"));
                ImageView twoIcon = new ImageView(twoImg);
                button.setGraphic(twoIcon);
                //setButtonBackground(button, "pillar2.png");
                break;
            case '3':
                Image threeImg = new Image(getClass().getResourceAsStream("resources/pillar3.png"));
                ImageView threeIcon = new ImageView(threeImg);
                button.setGraphic(threeIcon);
                //setButtonBackground(button, "pillar3.png");
                break;
            case '4':
                Image fourImg = new Image(getClass().getResourceAsStream("resources/pillar4.png"));
                ImageView fourIcon = new ImageView(fourImg);
                button.setGraphic(fourIcon);
                //setButtonBackground(button, "pillar4.png");
                break;
            case 'X':
                Image xImg = new Image(getClass().getResourceAsStream("resources/pillarX.png"));
                ImageView xIcon = new ImageView(xImg);
                button.setGraphic(xIcon);
                //setButtonBackground(button, "pillarX.png");
                break;
            case '.':
                if(!isRed) {
                    setButtonBackground(button, "white.png");
                }
                break;
            case 'L':
                if(isRed) setButtonBackground(button, "red.png");
                else setButtonBackground(button, "yellow.png");

                Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
                ImageView laserIcon = new ImageView(laserImg);
                button.setGraphic(laserIcon);
                break;
            case '*':
                setButtonBackground(button, "beam.png");
                break;
            default:
                break;
        }
    }

    private void load(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Safe File");
        File testfile = fileChooser.showOpenDialog(stage);
        if (testfile != null) {
            file = testfile;
            model.setFile(file);
            model.resetGrid();
            this.buttons = new Button[model.getRows()][model.getColumns()];
            this.border.setCenter(makeButttons());
            this.message.setText("");
            this.refreshView();
            //TODO resize window.
            //http://stackoverflow.com/questions/3391373/dynamic-instant-resize-in-javafx
        }
        //just do nothing if no file is selected

    }

    private void restart() {
        model.resetGrid();
        this.message.setText("The game was reset");
        this.refreshView();
    }

    private void solve() {
        //TODO
    }

    private void hint() {
        //TODO
    }

    private void check() {
        if(model.verify()) {
            message.setText("The safe is fully verified");
        }else {
            ArrayList<Integer> coords = model.getBadCoords();
            System.out.println(coords);
            if (coords.get(0) >= 0 && coords.get(1) >= 0) {
                //setButtonBackground(buttons[coords.get(0)][coords.get(1)], "red.png");
                buttonToPic(buttons[coords.get(1)][coords.get(0)], model.getGridAtPos(coords.get(1), coords.get(0)), true);
            }
        }
    }

    private void setLaser(int row, int col) {
        if(model.getGridAtPos1(col, row) != 'L') {
            model.addLaser(col, row);
        }
        else {
            model.removeLaser(col, row);
        }
        refreshView();
    }

    private void refreshView() {
        char [][] grid = model.getGrid();
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getColumns(); j++) {
                buttonToPic(buttons[j][i], grid[i][j], false);
            }

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.message.setText(model.getMessage());
        this.refreshView();
    }

}
