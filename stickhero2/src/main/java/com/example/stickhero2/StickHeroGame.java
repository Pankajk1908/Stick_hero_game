package com.example.stickhero2;



import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.paint.*;
import javafx.geometry.Insets;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class StickHeroGame extends Application {
    private GameEngine gameEngine;
    private GameUI gameUi;
    private ScoreSystem scoreSystem;
    private Pane root;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
//        FXMLLoader fxm =new FXMLLoader(StickHeroGame.class.getResource("hello-view.fxm"));
        root = new Pane();
        scene = new Scene(root, 1550, 800);
        primaryStage.setTitle("Stick Hero Game");
        gameUi=new GameUI(scene);
        scoreSystem=new ScoreSystem();
        gameEngine = new GameEngine(gameUi,scoreSystem);gameUi.setGameEngine(gameEngine);gameEngine.getGameUI().setGameEngine(gameEngine);
        Double pl1=350.0;
        Double pl2=150.0;
        Double pl3=200.0;
        Double pl4=75.0;
        Double pl5=100.0;
        Double pl6=135.0;
        Double pl7=45.0;
        gameEngine.getPlatform().getPillars().add(pl1);
        gameEngine.getPlatform().getPillars().add(pl2);
        gameEngine.getPlatform().getPillars().add(pl3);
        gameEngine.getPlatform().getPillars().add(pl4);
        gameEngine.getPlatform().getPillars().add(pl5);
        gameEngine.getPlatform().getPillars().add(pl6);
        gameEngine.getPlatform().getPillars().add(pl7);
        gameEngine.getPlatform().getWidthList().add(150.0);
        gameEngine.getPlatform().getWidthList().add(280.0);
        gameEngine.getPlatform().getWidthList().add(220.0);
        gameEngine.getPlatform().getWidthList().add(170.0);
        gameEngine.getPlatform().getWidthList().add(145.0);
        gameEngine.getPlatform().getWidthList().add(100.0);
        gameEngine.getPlatform().getWidthList().add(160.0);
        gameEngine.getPlatform().getWidthList().add(390.0);
        gameEngine.getPlatform().getWidthList().add(410.0);
        gameEngine.getPlatform().getWidthList().add(125.0);
        gameEngine.getPlatform().getWidthList().add(350.0);
        gameEngine.getPlatform().getWidthList().add(200.0);
        gameEngine.getPlatform().getWidthList().add(250.0);

        Stop[] stops = new Stop[] { new Stop(0, Color.WHITESMOKE), new Stop(1, Color.DEEPSKYBLUE) };
        RadialGradient radialGradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);
        BackgroundFill backgroundFill = new BackgroundFill(radialGradient, CornerRadii.EMPTY, Insets.EMPTY);
        Background b = new Background(backgroundFill);
        root.setBackground(b);
        gameUi.setY(scene.getHeight()-300);
        gameUi.createPreliminaryUI(scene);
        root.getChildren().add(gameUi.getPane());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

class GameEngine implements Runnable  {
    private StickHeroCharacter character;
    private GameUI gameUI;
    private ScoreSystem scoreSystem;
    private Platform platform;
    private  boolean pressed;


    void handleStartButtonClick() {
        int ind1=(int)(Math.random()*7);
        int ind2=(int)(Math.random()*13);
        gameUI.gameLoop(ind1,ind2);


    }
    public GameEngine(GameUI gameUI,ScoreSystem s) {
        this.gameUI = gameUI;
        this.character = new StickHeroCharacter();
        this.scoreSystem=s;
        this.platform=new Platform();
        this.pressed=false;
    }
    public void handleKeyPress(KeyCode keyCode ,double len) {
        AtomicBoolean isover= new AtomicBoolean(false);
        gameUI.getScene().setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("SPACE")) {
                gameUI.startMovingLine();
            }
        });

        gameUI.getScene().setOnKeyReleased(event -> {
            if (event.getCode().toString().equals("SPACE")) {
                gameUI.stopMovingLine();
                double stickLen=gameUI.getVerticalLine().getStartY()-gameUI.getVerticalLine().getEndY();
                if ((gameUI.getVerticalLine().getStartX()+stickLen<len)||(gameUI.getVerticalLine().getStartX()+stickLen> len+getPlatform().getP2())){
                    gameUI.moveChar();
                    gameUI.isOverUi();

                }else {
                    ImageView i=character.getImage();
                    final int[] j={1,(int)(len+platform.getP2()-50-i.getLayoutX())};
                    Timeline t1 =new Timeline();
                    KeyFrame k1=new KeyFrame(Duration.millis(5),event1 ->{
                        i.setLayoutX(i.getLayoutX()+1);
                        j[0]=j[0]+1;
                        if (j[0]==j[1]){
                            scoreSystem.setCurrentScore(scoreSystem.getCurrentScore()+1);
                            pressed=true;
                            t1.stop();
                        }
                    });
                    t1.getKeyFrames().add(k1);
                    t1.setCycleCount(Timeline.INDEFINITE);
                    t1.play();
                }
            }
        });
    }
    public void onClickRestart(){
        scoreSystem.update();
        handleStartButtonClick();
    }
    public void onClickContinue(){
        gameUI.getPane().getChildren().clear();
        if (scoreSystem.getNoOfCherry()>=10){
            scoreSystem.setNoOfCherry(scoreSystem.getNoOfCherry()-10);
            handleStartButtonClick();
        }else {
            Rectangle r1=new Rectangle(700,500,Color.LIGHTGOLDENRODYELLOW);r1.setLayoutX(400);r1.setLayoutY(150);
            gameUI.getPane().getChildren().add(r1);

            Label title = new Label("Insufficient cherries");
            title.setLayoutX((500));
            title.setLayoutY((170));
            title.setStyle(
                    "-fx-font-size: 60px;" +      // Set font size
                            "-fx-font-family: 'Lobster';" + // Set font family
                            "-fx-font-weight: bold;" +    // Set font weight
                            "-fx-text-fill: #346688;"     // Set text color
            );
            gameUI.getPane().getChildren().add(title);
            Button restart = new Button("Restart");
            restart.setLayoutX(550);
            restart.setLayoutY(500);
            restart.setScaleX(3);
            restart.setScaleY(2.85);
            restart.setOnAction(event -> this.onClickRestart());
            gameUI.getPane().getChildren().add(restart);

            Button home = new Button("Home");
            home.setLayoutX(900);
            home.setLayoutY(500);
            home.setScaleX(3);
            home.setScaleY(2.85);
            home.setOnAction(event -> this.onClickHome());
            gameUI.getPane().getChildren().add(home);
        }
    }
    public void onClickHome(){
        gameUI.getPane().getChildren().clear();
        gameUI.createPreliminaryUI(gameUI.getScene());
    }
    public void onClickPause(){
        gameUI.scoresystemUi();
    }
    @Override
    public void run() {

    }

    public Platform getPlatform() {
        return platform;
    }
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
    public StickHeroCharacter getCharacter() {
        return character;
    }
    public void setCharacter(StickHeroCharacter character) {
        this.character = character;
    }
    public GameUI getGameUI() {
        return gameUI;
    }
    public void setGameUI(GameUI gameUI) {
        this.gameUI = gameUI;
    }
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
    public boolean isPressed() {
        return pressed;
    }
    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }
    public void setScoreSystem(ScoreSystem scoreSystem) {
        this.scoreSystem = scoreSystem;
    }
}

class GameUI implements Runnable{
    private Pane pane;
    private Scene scene;
    private GameEngine gameEngine;
    private double y;
    private Timeline timeline;
    private Line verticalLine;

    @Override
    public void run() {

    }
    public void createPreliminaryUI(Scene s) {
        gameEngine.getScoreSystem().update();
        Button startButton = new Button("Play");
        startButton.setLayoutX((s.getWidth()-startButton.getWidth())/2);
        startButton.setLayoutY((s.getHeight()-startButton.getHeight())/2 +100);
        startButton.setScaleX(3);
        startButton.setScaleY(2.85);
        startButton.setOnAction(event -> gameEngine.handleStartButtonClick());
        pane.getChildren().add(startButton);
        Button pause = new Button("Stats");
        pause.setLayoutX(scene.getWidth()-100);
        pause.setLayoutY(50);
        pause.setScaleX(3);
        pause.setScaleY(2.85);
        pause.setOnAction(event -> gameEngine.onClickPause());
        pane.getChildren().add(pause);

        Label title = new Label("Stick Hero Game");
        title.setFont(new Font(94));
        title.setLayoutX((s.getWidth()-startButton.getWidth())/2 -200);
        title.setLayoutY((s.getHeight()-startButton.getHeight())/2-200);
        title.setStyle(
                "-fx-font-size: 54px;" +
                        "-fx-font-family: 'Lobster';" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #346688;"
        );

        pane.getChildren().add(title);
        Rectangle st=new Rectangle(350,400,Color.BLACK);
        st.setLayoutY(y);
        st.setLayoutX(0);
        pane.getChildren().add(st);
        Image image = new Image("C:\\Users\\Pankaj Kumar\\OneDrive\\Documents\\stst.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(75);
        gameEngine.getCharacter().setImage(imageView);
        gameEngine.getCharacter().getImage().setLayoutX(300);
        gameEngine.getCharacter().getImage().setLayoutY(y-75);
        pane.getChildren().add(gameEngine.getCharacter().getImage());
        gameEngine.getPlatform().setP1(350);
        gameEngine.getPlatform().setY(y);

    }
    public GameUI(Scene root) {
        this.pane = new Pane();
        scene=root;
    }
    public void gameLoop(int ind1,int ind2) {
        pane.getChildren().clear();
        Label title1 = new Label("Score");
        title1.setLayoutX((scene.getWidth()/2 -100));
        title1.setLayoutY((scene.getHeight()/2 -250));
        title1.setStyle(
                "-fx-font-size: 54px;" +
                        "-fx-font-family: 'Lobster';" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #346688;"
        );
        this.getPane().getChildren().add(title1);
        Label title2 = new Label("Cherries");
        title2.setLayoutX((50));
        title2.setLayoutY((20));
        title2.setStyle(
                "-fx-font-size: 30px;" +      // Set font size
                        "-fx-font-family: 'Lobster';" + // Set font family
                        "-fx-font-weight: bold;" +    // Set font weight
                        "-fx-text-fill: #346688;"     // Set text color
        );
        this.getPane().getChildren().add(title2);
        Label title3 = new Label(gameEngine.getScoreSystem().getCurrentScore()+"");
        title3.setLayoutX((title1.getLayoutX()+65));
        title3.setLayoutY((title1.getLayoutY()+65));
        title3.setStyle(
                "-fx-font-size: 40px;" +      // Set font size
                        "-fx-font-family: 'Lobster';" + // Set font family
                        "-fx-font-weight: bold;" +    // Set font weight
                        "-fx-text-fill: #346688;"     // Set text color
        );
        this.getPane().getChildren().add(title3);
        Label title4 = new Label( ""+ gameEngine.getScoreSystem().getNoOfCherry());
        title4.setLayoutX((100));
        title4.setLayoutY((50));
        title4.setStyle(
                "-fx-font-size: 30px;" +      // Set font size
                        "-fx-font-family: 'Lobster';" + // Set font family
                        "-fx-font-weight: bold;" +    // Set font weight
                        "-fx-text-fill: #346688;"     // Set text color
        );
        this.getPane().getChildren().add(title4);
        Platform p=gameEngine.getPlatform();
        ImageView i=gameEngine.getCharacter().getImage();
        i.setLayoutX(p.getP1()-50);i.setLayoutY(y-75);
        this.getPane().getChildren().add(i);
        p.setP2(p.getPillars().get(ind1));
        double len=p.getP1()+p.getWidthList().get(ind2);
        Rectangle rec1=new Rectangle(p.getP1(),400,Color.BLACK);rec1.setLayoutX(0);rec1.setLayoutY(this.y);
        Rectangle rec2=new Rectangle(p.getP2(),400,Color.BLACK);rec2.setLayoutX(len);rec2.setLayoutY(this.y);
        this.getPane().getChildren().add(rec1);
        this.getPane().getChildren().add(rec2);
        double b= Math.random()*(p.getWidthList().get(ind2));
        Cherry cherry=new Cherry();
        Random r=new Random();
        int a=r.nextInt(0,2);
        System.out.println(a);
        if (a==1){
            cherry.getImage().setLayoutX(gameEngine.getCharacter().getImage().getLayoutX()+60+b);cherry.getImage().setFitWidth(20);
            cherry.getImage().setLayoutY(this.y-30);cherry.getImage().setFitHeight(20);
            gameEngine.getPlatform().setCherry(cherry);
            pane.getChildren().add(gameEngine.getPlatform().getCherry().getImage());
            gameEngine.getScoreSystem().setNoOfCherry(gameEngine.getScoreSystem().getNoOfCherry()+1);
        }
        timeline = new Timeline();
        verticalLine = new Line(gameEngine.getPlatform().getP1(), gameEngine.getPlatform().getY(), gameEngine.getPlatform().getP1(), gameEngine.getPlatform().getY());
        this.getPane().getChildren().add(verticalLine);
        gameEngine.handleKeyPress(KeyCode.SPACE, len);
        p.setP1(p.getP2());
        ind1=(int)(Math.random()*7);
        ind2=(int)(Math.random()*13);
        final int[] indices = new int[]{ind1, ind2};
        Timeline t2=new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), event -> {
            if (gameEngine.isPressed()){
                t2.stop();
                gameLoop(indices[0],indices[1]);
                gameEngine.setPressed(false);
            }
        });

        t2.getKeyFrames().add(keyFrame);
        t2.setCycleCount(Timeline.INDEFINITE);
        t2.play();
    }
    public void startMovingLine() {
        verticalLine.setStrokeWidth(5);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), event -> {
            verticalLine.setEndY(verticalLine.getEndY() - 1);
        });

        // Configure the timeline to repeat indefinitely
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Start the timeline
        timeline.play();
    }
    public void stopMovingLine() {
        // Stop the timeline
        timeline.stop();
        KeyFrame k1=new KeyFrame(Duration.millis(10),event1 ->{verticalLine.getTransforms().add(new Rotate(9,verticalLine.getStartX(), verticalLine.getStartY()));;});
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(k1);
        timeline.setCycleCount(10);
        timeline.play();
//
//            verticalLine.getTransforms().add(new Rotate(90,verticalLine.getStartX(), verticalLine.getStartY()));
    }
    public void moveChar(){
        int len=(int)(-verticalLine.getEndY()+verticalLine.getStartY()+50);
        ImageView i=gameEngine.getCharacter().getImage();
        KeyFrame k1=new KeyFrame(Duration.millis(5),event1 ->{i.setLayoutX(i.getLayoutX()+1);});
        Timeline t1 =new Timeline(k1);
        t1.setCycleCount(len-50);
        t1.play();
    }
    public void isOverUi(){
        Rectangle r1=new Rectangle(700,500,Color.LIGHTGOLDENRODYELLOW);r1.setLayoutX(400);r1.setLayoutY(150);
        pane.getChildren().add(r1);

        Label title = new Label("Game Over");
        title.setLayoutX((600));
        title.setLayoutY((170));
        title.setStyle(
                "-fx-font-size: 60px;" +      // Set font size
                        "-fx-font-family: 'Lobster';" + // Set font family
                        "-fx-font-weight: bold;" +    // Set font weight
                        "-fx-text-fill: #346688;"     // Set text color
        );
        this.getPane().getChildren().add(title);

        Button cont = new Button("Continue(10 cherries)");
        cont.setLayoutX(700);
        cont.setLayoutY(400);
        cont.setScaleX(3);
        cont.setScaleY(2.85);
        cont.setOnAction(event -> gameEngine.onClickContinue());
        pane.getChildren().add(cont);

        Button restart = new Button("Restart");
        restart.setLayoutX(550);
        restart.setLayoutY(500);
        restart.setScaleX(3);
        restart.setScaleY(2.85);
        restart.setOnAction(event -> gameEngine.onClickRestart());
        pane.getChildren().add(restart);

        Button home = new Button("Home");
        home.setLayoutX(900);
        home.setLayoutY(500);
        home.setScaleX(3);
        home.setScaleY(2.85);
        home.setOnAction(event -> gameEngine.onClickHome());
        pane.getChildren().add(home);
    }
    public void scoresystemUi(){
        pane.getChildren().clear();
        Rectangle r1=new Rectangle(700,500,Color.LIGHTGOLDENRODYELLOW);r1.setLayoutX(400);r1.setLayoutY(150);
        pane.getChildren().add(r1);

        Label title = new Label("Score Board");
        title.setLayoutX((600));
        title.setLayoutY((170));
        title.setStyle(
                "-fx-font-size: 60px;" +      // Set font size
                        "-fx-font-family: 'Lobster';" + // Set font family
                        "-fx-font-weight: bold;" +    // Set font weight
                        "-fx-text-fill: #346688;"     // Set text color
        );
        this.getPane().getChildren().add(title);

        Label title1 = new Label("  Highest Score  :     "+gameEngine.getScoreSystem().getBestScore());
        title1.setLayoutX((400));
        title1.setLayoutY((300));
        title1.setStyle(
                "-fx-font-size: 60px;" +      // Set font size
                        "-fx-font-family: 'Lobster';" + // Set font family
                        "-fx-font-weight: bold;" +    // Set font weight
                        "-fx-text-fill: #346688;"     // Set text color
        );
        this.getPane().getChildren().add(title1);

        Label title2 = new Label("  No Of cherries  :     "+gameEngine.getScoreSystem().getNoOfCherry());
        title2.setLayoutX((400));
        title2.setLayoutY((400));
        title2.setStyle(
                "-fx-font-size: 60px;" +      // Set font size
                        "-fx-font-family: 'Lobster';" + // Set font family
                        "-fx-font-weight: bold;" +    // Set font weight
                        "-fx-text-fill: #346688;"     // Set text color
        );
        this.getPane().getChildren().add(title2);

        Button home = new Button("Home");
        home.setLayoutX(700);
        home.setLayoutY(550);
        home.setScaleX(3);
        home.setScaleY(2.85);
        home.setOnAction(event -> gameEngine.onClickHome());
        pane.getChildren().add(home);
    }
    public Pane getPane() {
        return pane;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setPane(Pane pane) {
        this.pane = pane;
    }
    public GameEngine getGameEngine() {
        return gameEngine;
    }
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }
    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene rootPane) {
        this.scene = rootPane;
    }
    public Timeline getTimeline() {
        return timeline;
    }
    public Line getVerticalLine() {
        return verticalLine;
    }
}

class StickHeroCharacter {
    private int x,y;
    private boolean isInverted;
    private ImageView image;

    public StickHeroCharacter() {
        isInverted=false;
        x=0;
    }
    public ImageView getImage() {
        return image;
    }
    public void setImage(ImageView image) {
        this.image = image;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public boolean isInverted() {
        return isInverted;
    }
    public void setInverted(boolean inverted) {
        isInverted = inverted;
    }
}
class ScoreSystem {
    private int bestScore;
    private int currentScore;
    private int noOfCherry;
    private int initCherry;

    public int getBestScore() {
        return bestScore;
    }
    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }
    public int getCurrentScore() {
        return currentScore;
    }
    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }
    public int getNoOfCherry() {
        return noOfCherry;
    }
    public void setNoOfCherry(int noOfCherry) {
        this.noOfCherry = noOfCherry;
    }
    public int getInitCherry() {
        return initCherry;
    }
    public void setInitCherry(int initCherry) {
        this.initCherry = initCherry;
    }

    public ScoreSystem() {
        this.bestScore = 0;
        this.currentScore = 0;
        this.noOfCherry = 0;
        this.initCherry = 0;
    }
    public void update(){
        if (currentScore>bestScore){
            bestScore=currentScore;
            initCherry=noOfCherry;
        }
        currentScore=0;
    }
}
class Platform {
    private Cherry cherry;
    private double pillar1Width;
    private double pillar2Width;
    private ArrayList<Double> pillars = new ArrayList<>();
    private ArrayList<Double> widthList = new ArrayList<>();
    private double x, y;

    public Platform() {
    }
    public Cherry getCherry() {
        return cherry;
    }
    public void setCherry(Cherry cherry) {
        this.cherry = cherry;
    }
    public double getP1() {
        return pillar1Width;
    }
    public void setP1(double p1) {
        this.pillar1Width = p1;
    }
    public double getP2() {
        return pillar2Width;
    }
    public void setP2(double p2) {
        this.pillar2Width = p2;
    }
    public ArrayList<Double> getPillars() {
        return pillars;
    }
    public void setPillars(ArrayList<Double> pillars) {
        this.pillars = pillars;
    }
    public ArrayList<Double> getWidthList() {
        return widthList;
    }
    public void setWidthList(ArrayList<Double> widthList) {
        this.widthList = widthList;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
}

class Cherry extends Platform {
    private ImageView image;

    public Cherry() {
        Image i=new Image("C:\\Users\\Pankaj Kumar\\OneDrive\\Documents\\stst2.jpg");
        image=new ImageView(i);
    }

    public ImageView getImage() {
        return image;
    }


}