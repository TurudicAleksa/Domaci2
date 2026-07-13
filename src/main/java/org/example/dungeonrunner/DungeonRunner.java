package org.example.dungeonrunner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;


import java.util.ArrayList;

public class DungeonRunner extends Application {

    private DungeonMap map;
    private Player player;
    private Group world;
    private PerspectiveCamera camera;
    private Group cameraMount;
    private PointLight torch;
    private AnimationTimer timer;
    private ArrayList<Saw> saws = new ArrayList<>();
    private ArrayList<Spikes> spikes = new ArrayList<>();
    public Minimap minimap;
    private Box exitBox;
    private Key key;

    private void buildDungeon ( ) {
        PhongMaterial wallMaterial = new PhongMaterial ( );
        Image bricks = new Image(getClass().getResourceAsStream("/img/bricks.jpg"));
        wallMaterial.setDiffuseColor ( Constants.WALL_DIFFUSE_COLOR );
        wallMaterial.setSpecularColor ( Constants.WALL_SPECULAR_COLOR );
        wallMaterial.setDiffuseMap(bricks);





        PhongMaterial floorMaterial = new PhongMaterial();
        floorMaterial.setDiffuseColor(Color.rgb(60, 40, 20));

        PhongMaterial ceilingMaterial = new PhongMaterial();
        ceilingMaterial.setDiffuseColor(Color.rgb(25, 25, 45));
        
        this.map = new DungeonMap ( Constants.MAP );

        int    rows       = this.map.getRows ( );
        int    columns    = this.map.getCols ( );
        double totalWidth = columns * Constants.CELL_SIZE;
        double totalDepth = rows * Constants.CELL_SIZE;

        Box floor = new Box ( totalWidth, Constants.SLAB_THICKNESS, totalDepth );
        Translate floorTranslate = new Translate (
                totalWidth / 2.0,
                Constants.WALL_HEIGHT / 2.0 + Constants.SLAB_THICKNESS / 2.0,
                totalDepth / 2.0
        );
        floor.getTransforms ( ).add ( floorTranslate );
        floor.setMaterial ( floorMaterial );

        Box ceiling = new Box ( totalWidth, Constants.SLAB_THICKNESS, totalDepth );
        Translate ceilingTranslate = new Translate (
                totalWidth / 2.0,
                -Constants.WALL_HEIGHT / 2.0 - Constants.SLAB_THICKNESS / 2.0,
                totalDepth / 2.0
        );
        ceiling.getTransforms ( ).add ( ceilingTranslate );
        ceiling.setMaterial ( ceilingMaterial );

        this.world.getChildren ( ).addAll ( floor, ceiling );
        minimap = new Minimap(columns,rows);

        for ( int row = 0; row < rows; row++ ) {
            for ( int column = 0; column < columns; column++ ) {
                int tile = this.map.get ( column, row );
                if ( tile == Constants.WALL || tile == Constants.EXIT ) {
                    Box wall = new Box ( Constants.CELL_SIZE, Constants.WALL_HEIGHT, Constants.CELL_SIZE );
                    Translate wallTranslate = new Translate (
                            column * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                            0,
                            row * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0
                    );
                    wall.getTransforms().add(wallTranslate);

                    if (tile == Constants.EXIT) {
                        minimap.add(column,row,2);
                        wall.setMaterial(wallMaterial);
                        exitBox = wall;
                    } else {
                        minimap.add(column,row,1);
                        wall.setMaterial(wallMaterial);
                    }

                    this.world.getChildren().add(wall);
                }
                else if(tile==Constants.OCTA){
                    minimap.add(column,row,1);
                    TriangleMesh mesh = new TriangleMesh();
                    float h = (float) (Constants.WALL_HEIGHT/2);
                    mesh.getPoints().addAll(
                            0,  -h,  0,
                            0,   h,  0,
                            h,   0,  0,
                            0,   0,  h,
                            -h,   0,  0,
                            0,   0, -h
                    );
                    mesh.getTexCoords().addAll(0.5f,0,0,1,1,1);
                    mesh.getFaces().addAll(
                            0, 0, 2, 1, 3, 2,
                            0, 0, 3, 1, 4, 2,
                            0, 0, 4, 1, 5, 2,
                            0, 0, 5, 1, 2, 2,
                            1, 0, 3, 1, 2, 2,
                            1, 0, 4, 1, 3, 2,
                            1, 0, 5, 1, 4, 2,
                            1, 0, 2, 1, 5, 2
                    );

                    MeshView octa = new MeshView(mesh);
                    octa.setCullFace(CullFace.NONE);
                    PhongMaterial p = new PhongMaterial();
                    p.setDiffuseMap(bricks);
                    octa.setMaterial(p);

                    Translate wallTranslate = new Translate (
                            column * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                            0,
                            row * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0
                    );
                    octa.getTransforms ().add (wallTranslate);
                    this.world.getChildren().add(octa);

                }
                else{
                    minimap.add(column,row,0);
                }
            }
        }
        Saw saw1 = new Saw(0 * Constants.CELL_SIZE + Constants.CELL_SIZE,
                3 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0);
        saws.add(saw1);
        saw1.rotate(180);
        Saw saw2 = new Saw(2 * Constants.CELL_SIZE,
                3 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0);
        saws.add(saw2);
        this.world.getChildren().add(saw1.getSaws());
        this.world.getChildren().add(saw2.getSaws());

        Spikes sp1 = new Spikes(1 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                2 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0);
        spikes.add(sp1);
        this.world.getChildren().add(sp1.get());

        key = new Key(2 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                6 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                0);
        this.world.getChildren().add(key.get());



    }
    private void unlockExitVisual() {
        if (exitBox != null) {
            PhongMaterial exitMaterial = new PhongMaterial();
            exitMaterial.setDiffuseColor ( Constants.EXIT_DIFFUSE_COLOR );
            exitMaterial.setSpecularColor ( Constants.EXIT_SPECULAR_COLOR );
            exitBox.setMaterial(exitMaterial); // green
        }
    }

    private void setupLighting ( ) {
        AmbientLight ambient = new AmbientLight ( Constants.AMBIENT_LIGHT_COLOR );

        this.torch = new PointLight ( Constants.POINT_LIGHT_COLOR );
        this.torch.setMaxRange ( Constants.CELL_SIZE * 6 );

        this.world.getChildren ( ).addAll ( ambient, torch );
    }

    private void setupCamera ( ) {
        this.camera = new PerspectiveCamera ( true );

        this.camera.setNearClip ( Constants.CAMERA_NEAR_CLIP );
        this.camera.setFarClip ( Constants.CAMERA_FAR_CLIP );
        this.camera.setFieldOfView ( Constants.CAMERA_FIELD_OF_VIEW );

        this.cameraMount = new Group ( this.camera );


        minimap.get().setTranslateZ(40);
        minimap.get().setTranslateX(5.0);
        minimap.get().setTranslateY(-3.5);


        this.cameraMount.getChildren().add(minimap.get());

        this.world.getChildren ( ).add ( cameraMount );

        updateCameraMount ( );


    }
    private boolean checkHazardCollisions() {
        double playerWorldX = player.getPositionX() * Constants.CELL_SIZE;
        double playerWorldZ = player.getPositionY() * Constants.CELL_SIZE;
        double playerHitRadius = Constants.PLAYER_RADIUS * Constants.CELL_SIZE;

        for (Saw saw : saws) {
            double dx = playerWorldX - saw.getX();
            double dz = playerWorldZ - saw.getZ();
            double distSq = dx * dx + dz * dz;
            double hitDist = Constants.CELL_SIZE * 0.2 + playerHitRadius;
            if (distSq < hitDist * hitDist) {
                return true;
            }
        }

        for (Spikes spike : spikes) {
            if (!spike.isDangerous()) continue;
            double dx = Math.abs(playerWorldX - spike.getX());
            double dz = Math.abs(playerWorldZ - spike.getZ());
            double hitDist = Constants.PLAYER_RADIUS * Constants.CELL_SIZE + 0.5;
            if (dx<hitDist && dz<hitDist) {
               return true;
            }
        }
        if (!key.isCollected()) {
            double dx = playerWorldX - key.getX();
            double dz = playerWorldZ - key.getZ();
            double distSq = dx * dx + dz * dz;
            double pickupDist = Constants.CELL_SIZE * 0.5;
            if (distSq < pickupDist * pickupDist) {
                key.collect();
                player.unlockExit();
                unlockExitVisual();
            }
        }

        return false;
    }
    private void setupMinimap(Player player){
        minimap.setPlayer(player);
    }

    private void setupInput ( Scene scene ) {
        scene.setOnKeyPressed ( event -> {
            switch ( event.getCode ( ) ) {
                case UP: {
                    this.player.setMoveForward ( true );
                    break;
                }
                case DOWN: {
                    this.player.setMoveBackward ( true );
                    break;
                }
                case LEFT: {
                    this.player.setRotateLeft ( true );
                    break;
                }
                case RIGHT: {
                    this.player.setRotateRight ( true );
                    break;
                }
            }
        } );

        scene.setOnKeyReleased ( event -> {
            switch ( event.getCode ( ) ) {
                case UP: {
                    this.player.setMoveForward ( false );
                    break;
                }
                case DOWN: {
                    this.player.setMoveBackward ( false );
                    break;
                }
                case LEFT: {
                    this.player.setRotateLeft ( false );
                    break;
                }
                case RIGHT: {
                    this.player.setRotateRight ( false );
                    break;
                }
            }
        } );
    }

    private void updateCameraMount ( ) {
        Translate camerMountTranslate = new Translate (
                this.player.getPositionX( ) * Constants.CELL_SIZE,
                0,
                this.player.getPositionY( ) * Constants.CELL_SIZE
        );

        Rotate camerMountRotate = new Rotate (
                Math.toDegrees ( Math.atan2 ( this.player.getDirectionX ( ), this.player.getDirectionY ( ) ) ),
                Rotate.Y_AXIS
        );

        this.cameraMount.getTransforms ( ).setAll (
                camerMountTranslate,
                camerMountRotate
        );
    }
    private int flicker=0;
    private void updateTorch() {
        Translate torchTranslate = new Translate (
                this.player.getPositionX( ) * Constants.CELL_SIZE,
                0,
                this.player.getPositionY( ) * Constants.CELL_SIZE
        );
        if(flicker>100){
            flicker=-5;
            this.torch.setConstantAttenuation(2);
        }
        if(flicker>0) this.torch.setConstantAttenuation(1.0);
        //else this.torch.setConstantAttenuation(1);

        this.torch.getTransforms ( ).setAll ( torchTranslate );
    }
    private long time=-1;
    @Override
    public void start ( Stage stage ) {
        this.player = new Player ( Constants.PLAYER_START_X, Constants.PLAYER_START_Y );
        this.world = new Group ( );

        buildDungeon ( );
        setupLighting ( );
        setupCamera ( );

        javafx.scene.SubScene subScene = new javafx.scene.SubScene (
                this.world,
                Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT,
                true,
                SceneAntialiasing.BALANCED
        );
        subScene.setCamera ( this.camera );
        subScene.setFill ( Color.BLACK );


        Group minimapGroup = minimap.get ( );
        minimapGroup.getTransforms().add(
                new javafx.scene.transform.Scale(-1, 1, minimap.getSizeX() / 2.0, 0)
        );
        minimapGroup.setTranslateX ( 10 );
        minimapGroup.setTranslateY ( 10 );
        minimapGroup.setOpacity(0.7);
        setupMinimap(player);

        Group livesC = new Group();
        Text lives = new Text();
        lives.setText(String.valueOf(player.getLives()));
        lives.setScaleX(3);
        lives.setScaleY(3);
        lives.setFill(Color.WHITE);
        livesC.getChildren().addAll(lives);
        lives.getTransforms().addAll(new Translate(-300,-300));




        Group endScreen = new Group();
        Rectangle bx = new Rectangle(400,300);
        bx.setFill(Color.GRAY);
        Text win = new Text("YOU WIN");
        win.setFill(Color.GREEN);
        win.getTransforms().add(new Translate(55,50));
        win.setScaleX(3);
        win.setScaleY(3);
        Text loss = new Text("GAME OVER");
        loss.setFill(Color.RED);
        loss.getTransforms().add(new Translate(55,50));
        loss.setScaleX(3);
        loss.setScaleY(3);
        endScreen.getChildren().addAll(bx,win,loss);
        endScreen.setOpacity(0);

        javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane ( subScene, minimapGroup,livesC, endScreen );
        javafx.scene.layout.StackPane.setAlignment ( minimapGroup, Pos.BOTTOM_RIGHT );
        javafx.scene.layout.StackPane.setAlignment ( endScreen, Pos.CENTER );
        javafx.scene.layout.StackPane.setAlignment ( livesC, Pos.TOP_LEFT );

        Scene scene = new Scene ( root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT );
        /*Scene scene = new Scene (
                this.world,
                Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT,
                true,
                SceneAntialiasing.BALANCED
        );*/
        //scene.setCamera ( this.camera );

        setupInput ( scene );

        this.timer = new AnimationTimer ( ) {
            @Override
            public void handle ( long now ) {
                if(time<0){
                    time=now;
                    return;
                }
                double sec = (now - time) / 1_000_000_000.0;
                time=now;

                player.update ( map );
                updateCameraMount ( );
                updateTorch ( );
                minimap.updatePlayer(player);

                flicker++;
                for(Saw i:saws){
                    i.update(sec);
                }
                for(Spikes i:spikes){
                    i.update(sec);
                }
                if ( player.isAtExit ( map ) ) {
                    endScreen.setOpacity(1);
                    loss.setOpacity(0);
                    timer.stop ( );
                }
                if(checkHazardCollisions()){

                    if(player.loselife()==0){
                        endScreen.setOpacity(1);
                        win.setOpacity(0);
                        timer.stop();
                    }
                    lives.setText(String.valueOf(player.getLives()));
                }
                key.update(sec);


            }
        };
        timer.start ( );

        stage.setTitle ( "Beg iz tamnice" );
        stage.setScene ( scene );
        stage.setResizable ( false );
        stage.show ( );
    }

    public static void main ( String[] args ) {
        launch ( args );
    }
}