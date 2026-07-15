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
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private ArrayList<Heart> hearts = new ArrayList<>();
    private ArrayList<Shield> shields = new ArrayList<>();
    private ArrayList<Potion> potions = new ArrayList<>();
    private DoorKey doorKey;
    private Sphere shieldSphere;
    public Minimap minimap;
    private Box exitBox;
    private Key key;
    private Stage stage;
    int invincible=0;
    int invul=0;
    double seconds = 0;
    double minutes = 0;
    double shieldTime=0;
    Group spawnables = new Group();
    Patrol p;

    private void buildDungeon (int[][] mapC ) {
        PhongMaterial wallMaterial = new PhongMaterial ( );
        Image bricks = new Image(getClass().getResourceAsStream("/img/bricks.jpg"));
        wallMaterial.setDiffuseColor ( Constants.WALL_DIFFUSE_COLOR );
        wallMaterial.setSpecularColor ( Constants.WALL_SPECULAR_COLOR );
        wallMaterial.setDiffuseMap(bricks);

        PhongMaterial floorMaterial = new PhongMaterial();
        floorMaterial.setDiffuseColor(Color.rgb(60, 40, 20));

        PhongMaterial ceilingMaterial = new PhongMaterial();
        ceilingMaterial.setDiffuseColor(Color.rgb(25, 25, 45));

        this.map = new DungeonMap ( mapC );

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
        double[] x = new double[]{0};
        double[] y = new double[]{0};
        double[] lx = new double[]{0};
        double[] ly = new double[]{0};
        int dx=0,dy=0;
        int sawx1=0, sawy2=0, sawx2=0, sawy1=0, spx1=0, spy1=0, kx1=0, ky1=0,px1=0,py1=0;
        if(mapC==Constants.MAP){
            sawx1=0;
            sawy1=3;
            sawx2=2;
            sawy2=3;
            spx1=1;
            spy1=2;
            kx1=7;
            ky1=2;
            px1=5;
            py1=2;
            dx=4;
            dy=6;
            lx= new double[]{4,5,6,7};
            ly= new double[]{6,6,6,6};
            y = new double[]{2, 3, 4, 4, 4, 3, 2,1,1,1};
            x = new double[]{5, 5, 5, 6, 7, 7, 7,7,6,5};
        }
        if(mapC==Constants.MAP2){
            sawx1=3;
            sawy1=5;
            sawx2=3;
            sawy2=3;
            spx1=6;
            spy1=5;
            kx1=3;
            ky1=1;
            px1=6;
            py1=4;
            dx=5;
            dy=6;
            lx= new double[]{1,2,3,4,5};
            ly= new double[]{6,6,6,6,6};
            y = new double[]{4, 5, 6, 5, 4};
            x = new double[]{6, 6, 6, 6, 6};
        }
        if(mapC==Constants.MAP3){
            sawx1=0;
            sawy1=5;
            sawx2=2;
            sawy2=5;
            spx1=3;
            spy1=6;
            kx1=7;
            ky1=2;
            px1=1;
            py1=6;
            dx=4;
            dy=2;
            lx= new double[]{4,5,5,6,7,7};
            ly= new double[]{2,2,1,1,1,2};
            y = new double[]{6, 5, 4, 3, 2,2,3,4,5,6};
            x = new double[]{1, 1, 1, 1, 1,1,1,1,1,1};
        }
        Saw saw1 = new Saw(sawx1 * Constants.CELL_SIZE + Constants.CELL_SIZE,
                sawy1 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0);
        saws.add(saw1);
        saw1.rotate(180);
        Saw saw2 = new Saw(sawx2 * Constants.CELL_SIZE,
                sawy2 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0);
        saws.add(saw2);
        if(mapC==Constants.MAP2){
            saw1.rotate(90);
            saw2.rotate(270);
        }

        this.world.getChildren().add(saw1.getSaws());
        this.world.getChildren().add(saw2.getSaws());

        Spikes sp1 = new Spikes(spx1 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                spy1 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0);
        spikes.add(sp1);
        this.world.getChildren().add(sp1.get());

        key = new Key(kx1 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                ky1 * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                0);
        this.world.getChildren().add(key.get());
        minimap.addKey(kx1,ky1);

        p = new Patrol(px1 * Constants.CELL_SIZE+ Constants.CELL_SIZE / 2.0,
                py1 * Constants.CELL_SIZE+ Constants.CELL_SIZE / 2.0);
        this.world.getChildren().add(p.get());

        p.addPath(x,y);

        this.world.getChildren().add(spawnables);
        double[] l = RandomPosition.findRandomLeverPos(map,lx,ly);
        doorKey = new DoorKey();
        doorKey.setDoorPosition(dx, dy);
        doorKey.setLeverPosition((int)l[0], (int)l[1]);
        minimap.addLever((int)l[0], (int)l[1],doorKey.getColor());
        minimap.addDoor(dx,dy,doorKey.getColor());
        this.world.getChildren().add(doorKey.get());

        player.setDoor(doorKey);


    }
    private void unlockExitVisual() {
        if (exitBox != null) {
            PhongMaterial exitMaterial = new PhongMaterial();
            exitMaterial.setDiffuseColor ( Constants.EXIT_DIFFUSE_COLOR );
            exitMaterial.setSpecularColor ( Constants.EXIT_SPECULAR_COLOR );
            exitBox.setMaterial(exitMaterial);
            minimap.unlock();
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
        double shieldRadius = Constants.CAMERA_NEAR_CLIP * 8;
        shieldSphere = new Sphere(shieldRadius);
        shieldSphere.setCullFace(CullFace.NONE);
        PhongMaterial shieldMat = new PhongMaterial(Color.rgb(60, 20, 255, 0.15));
        shieldMat.setSpecularColor(Color.rgb(0, 0, 255, 0.4));
        shieldSphere.setMaterial(shieldMat);
        shieldSphere.setMouseTransparent(true);
        shieldSphere.setVisible(false);
        cameraMount.getChildren().add(shieldSphere);


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
                    if(invincible>0){
                        invincible=0;
                        invul=2;
                        return false;
                    }
                    if(invul>0) return false;
                    return true;
                }
            }

            for (Spikes spike : spikes) {
                if (!spike.isDangerous()) continue;
                double dx = Math.abs(playerWorldX - spike.getX());
                double dz = Math.abs(playerWorldZ - spike.getZ());
                double hitDist = Constants.PLAYER_RADIUS * Constants.CELL_SIZE + 0.5;
                if (dx<hitDist && dz<hitDist) {
                    if(invincible>0){
                        invincible=0;
                        invul=2;
                        return false;
                    }
                    if(invul>0) return false;
                    return true;
                }
            }
            for(Potion i: potions){
                if (!i.isCollected()) {
                    double dx = playerWorldX - i.getX();
                    double dz = playerWorldZ - i.getZ();
                    double distSq = dx * dx + dz * dz;
                    double pickupDist = Constants.CELL_SIZE * 0.2;
                    if (distSq < pickupDist * pickupDist) {

                        i.collect();
                        player.poison();
                        spawnables.getChildren().remove(i);
                    }
                }
            }


        for(Heart i: hearts){
            if (!i.isCollected()) {
                double dx = playerWorldX - i.getX();
                double dz = playerWorldZ - i.getZ();
                double distSq = dx * dx + dz * dz;
                double pickupDist = Constants.CELL_SIZE * 0.2;
                if (distSq < pickupDist * pickupDist) {
                    i.collect();
                    player.addLife();
                    spawnables.getChildren().remove(i);

                }
            }
        }
        for(Shield i: shields){
            if (!i.isCollected()) {
                double dx = playerWorldX - i.getX();
                double dz = playerWorldZ - i.getZ();
                double distSq = dx * dx + dz * dz;
                double pickupDist = Constants.CELL_SIZE * 0.2;
                if (distSq < pickupDist * pickupDist) {
                    i.collect();
                    invincible=5;
                    spawnables.getChildren().remove(i);
                }
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
                minimap.removeKey();
            }
        }
        if(true){
            double playerRadius = Constants.PLAYER_RADIUS * Constants.CELL_SIZE;
            double dx = playerWorldX - p.getPosX();
            double dz = playerWorldZ - p.getPosY();
            double distSq = dx * dx + dz * dz;
            double hitDist = 0.5 + playerRadius;
            if(distSq < hitDist * hitDist){
                if(invincible>0){
                    invincible=0;
                    invul=2;
                    return false;
                }
                if(invul>0) return false;
                return true;
            }
        }
        if(doorKey.update(playerWorldX,playerWorldZ)){
            minimap.removeLever();

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

        this.torch.getTransforms ( ).setAll ( torchTranslate );
    }
    private long time=-1;

    private void showLevelSelect ( ) {
        List<String> strings = new ArrayList<>();
        strings.add("Level 1");
        strings.add("Level 2");
        strings.add("Level 3");
        SelectionScreen selection = new SelectionScreen (
                "Choose a Level",
                strings,
                Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT
        );

        Scene menuScene = new Scene ( selection, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT );

        menuScene.setOnKeyPressed ( e -> {
            if ( e.getCode ( ) == KeyCode.ENTER || e.getCode ( ) == KeyCode.SPACE ) {
                switch(selection.getSelectedIndex()){
                    case 0: startGame(Constants.MAP);
                    break;
                    case 1: startGame(Constants.MAP2);
                    break;
                    case 2: startGame(Constants.MAP3);
                }

            } else {
                selection.handleKey ( e.getCode ( ) );
            }
        } );

        stage.setScene ( menuScene );
    }



    private void startGame (int[][] mapC ) {

        saws.clear ( );
        spikes.clear ( );
        flicker = 0;
        seconds = 0;
        minutes = 0;
        time = -1;

        this.player = new Player ( Constants.PLAYER_START_X, Constants.PLAYER_START_Y );
        this.world = new Group ( );

        buildDungeon ( mapC);
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


        Group timerG = new Group();
        Text timec = new Text("00:00");
        timec.setScaleX(3);
        timec.setScaleY(3);
        timec.setFill(Color.WHITE);
        timerG.getChildren().addAll(timec);

        javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane ( subScene, minimapGroup,livesC, endScreen, timerG );
        javafx.scene.layout.StackPane.setAlignment ( minimapGroup, Pos.BOTTOM_RIGHT );
        javafx.scene.layout.StackPane.setAlignment ( endScreen, Pos.CENTER );
        javafx.scene.layout.StackPane.setAlignment ( livesC, Pos.TOP_LEFT );
        javafx.scene.layout.StackPane.setAlignment ( timerG, Pos.TOP_RIGHT );

        Scene scene = new Scene ( root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT );

        setupInput ( scene );

        this.timer = new AnimationTimer ( ) {
            @Override
            public void handle ( long now ) {
                lives.setText(String.valueOf(player.getLives()));
                Random seedGen = new Random();
                double seed=seedGen.nextDouble();

                if(time<0){
                    time=now;
                    return;
                }
                double sec = (now - time) / 1_000_000_000.0;
                seconds+=sec;
                shieldTime+=sec;
                shieldSphere.setVisible(invincible > 0);
                if(shieldTime>1.0){
                    shieldTime-=1.0;
                    if(invincible>0) {
                        invincible--;

                    }
                    if(invul>0) invul--;
                }

                if(seconds>=60.0){
                    minutes++;
                    seconds-=60;
                }
                String formattedSec = String.format("%02d", (int)seconds);
                String formattedMin = String.format("%02d", (int)minutes);
                timec.setText(formattedMin+":"+formattedSec);
                time=now;

                player.update ( map );
                p.update(sec);

                updateCameraMount ( );
                updateTorch ( );
                minimap.updatePlayer(player);

                if(seed>0.994){
                    double[] coords = RandomPosition.findRandomOpenPosition(map);
                    assert coords != null;
                    if(seed<0.996){
                        Heart a = new Heart(coords[0],coords[1],0.5);
                        hearts.add(a);
                        spawnables.getChildren().add(a.get());
                    } else if (seed<0.998) {
                        Shield a = new Shield(coords[0],coords[1],0.5);
                        shields.add(a);
                        spawnables.getChildren().add(a.get());
                    }
                    else{
                        Potion a = new Potion(coords[0],coords[1],0.5);
                        potions.add(a);
                        spawnables.getChildren().add(a.get());
                    }
                }


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

        stage.setScene ( scene );
    }

    @Override
    public void start ( Stage stage ) {
        this.stage = stage;
        stage.setTitle ( "Beg iz tamnice" );
        stage.setResizable ( false );

        showLevelSelect ( );

        stage.show ( );
    }

    public static void main ( String[] args ) {
        launch ( args );
    }
}