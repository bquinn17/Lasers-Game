package gui;

import backtracking.Backtracker;
import backtracking.SafeConfig;
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
    /**
     * The UI's connection to the model
     */
    private LasersModel model;

    /**
     * this can be removed - it is used to demonstrates the button toggle
     */
    private static boolean status = true;
    private BorderPane border;
    private Button[][] buttons;
    private Label message;
    private File file;
    private char[][] winningConfig;


    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        Parameters params = getParameters();
        String filename = params.getRaw().get(0);
        this.file = new File(filename);
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
     * @param button    the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        Image laserImg = new Image(getClass().getResourceAsStream("resources/" + bgImgName));
        ImageView laserIcon = new ImageView(laserImg);
        button.setGraphic(laserIcon);
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }


    /**
     * Initializes variables
     *
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage) {
        //TODO add threading
         /*

         */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);  // do all your UI initialization here

        GridPane grid = makeButttons();
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
        bottom.setPadding(new Insets(10, 10, 10, 10));

        message = new Label(file.getName() + " was loaded");
        message.setTextAlignment(TextAlignment.CENTER);
        HBox top = new HBox();
        top.getChildren().add(message);
        top.setAlignment(Pos.CENTER);

        border.setTop(top);
        border.setPadding(new Insets(10, 10, 10, 10));
        border.setBottom(bottom);
        border.setCenter(grid);

        Scene scene = new Scene(border);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> grid.setPrefWidth((Double) newValue));

        scene.heightProperty().addListener((observable, oldValue, newValue) -> grid.setPrefHeight((Double) newValue));

        primaryStage.setTitle("Lasers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        Image schuyler = new Image(getClass().getResourceAsStream("resources/Schuyler.png"));
        primaryStage.getIcons().add((schuyler));
        primaryStage.show();
    }

    /**
     * creates grid that has buttons to be clicked on to add laser.
     *
     * @return the grid of the puzzle
     */
    private GridPane makeButttons() {
        GridPane grid = new GridPane();
        for (int row = 0; row < this.model.getRows(); row++) {
            for (int col = 0; col < this.model.getColumns(); col++) {
                Button current = new Button();
                current.setPadding(new Insets(10, 10, 10, 10));
                buttonToPic(current, model.getGridAtPos(col, row), false);
                int finalCol = col;
                int finalRow = row;
                current.setOnAction(e -> setLaser(finalCol, finalRow));
                buttons[col][row] = current;
                grid.add(current, col, row);
            }
        }
        return grid;
    }

    /**
     * Assign the laser or pillars with the correct background and picture.
     *
     * @param button the button that you want to modified.
     * @param ch     the picture you want to be there
     * @param isRed  whether or not that position is wrong
     */
    private void buttonToPic(Button button, char ch, boolean isRed) {
        if (isRed) {
            setButtonBackground(button, "red.png");
        } else {
            setButtonBackground(button, "white.png");
        }
        switch (ch) {
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
                if (!isRed) {
                    setButtonBackground(button, "white.png");
                }
                break;
            case 'L':
                if (isRed) setButtonBackground(button, "red.png");
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

    /**
     * Loads in a new safe that you want to solve.
     *
     * @param stage the gui that you are changing
     */
    private void load(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Safe File");
        File testfile = fileChooser.showOpenDialog(stage);
        if (testfile != null) {
            file = testfile;
            model.setFile(file);
            model.resetGrid();
            this.buttons = new Button[model.getColumns()][model.getRows()];
            this.border.setCenter(makeButttons());
            this.message.setText(testfile.getName() + " was loaded");
            this.refreshView();
        } else {
            this.message.setText("No file was loaded");
            this.refreshView();
        }
        //just do nothing if no file is selected

    }

    /**
     * will restart the laser entirely, so you can play again.
     */
    private void restart() {
        model.resetGrid();
        this.message.setText("The game was reset");
        this.refreshView();
    }

    /**
     * will solve the puzzle entirely for you. Uses backtracking
     */
    private void solve() {
        Backtracker solver = new Backtracker("tests/" + file.getName());
        SafeConfig solution = (SafeConfig) solver.getSolution();
        this.winningConfig = solution.getGrid();
        this.model.setGrid(winningConfig);
        this.refreshView();
    }

    /**
     * Gives you the next position of the laser that you should do only if your laser is correct.
     */
    private void hint() {
        if(winningConfig == null){
            Backtracker solver = new Backtracker("tests/" + file.getName());
            SafeConfig solution = (SafeConfig) solver.getSolution();
            this.winningConfig = solution.getGrid();
        }
        char[][] grid = this.model.getGrid();
        if (!model.verify()) {
            int row = model.getBadCoords().get(0);
            int col = model.getBadCoords().get(1);
            if (model.getGridAtPosFlipped(row, col) == 'L'){
                this.message.setText("Hint: no next step");
                return;
            }
        } else {
            message.setText("The safe is fully verified");
            return;
        }
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getColumns(); j++) {
                if (grid[i][j] == 'L' && winningConfig[i][j] != 'L') {
                    this.message.setText("Hint: no next step");
                    return;
                }
                if (winningConfig[i][j] == 'L') {
                    boolean add = true;
                    if (model.addLaser(i, j)) {
                        add = false;
                        this.message.setText("Hint: added laser to (" + i + "," + j + ")");
                    } else if (add) {
                        this.message.setText("Hint: no next step");
                        return;
                    }

                }
            }
        }
    }


    /**
     * makes sure that you solved the puzzle correctly.
     */
    private void check() {
        if (model.verify()) {
            message.setText("The safe is fully verified");
        } else {
            ArrayList<Integer> coords = model.getBadCoords();
            if (coords.get(0) >= 0 && coords.get(1) >= 0) {
                buttonToPic(buttons[coords.get(1)][coords.get(0)], model.getGridAtPos(coords.get(1), coords.get(0)), true);
            }
        }
    }

    /**
     * puts a laser at a certain position in the grid
     *
     * @param row row that you are placing the laser
     * @param col col that you are placing the laser
     */
    private void setLaser(int row, int col) {
        if (model.getGridAtPosFlipped(col, row) != 'L') {
            model.addLaser(col, row);
        } else {
            model.removeLaser(col, row);
        }
        refreshView();
    }

    /**
     * will refreshes the view and put everything from the model in the gui to the correct spot.
     */
    private void refreshView() {
        char[][] grid = model.getGrid();
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
