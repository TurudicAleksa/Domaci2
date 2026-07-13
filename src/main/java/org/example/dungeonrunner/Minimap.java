package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.awt.*;

public class Minimap {
    private double sizeX;
    private double sizeY;
    private double boxSizeX;
    private double boxSizeY;
    private Circle player;
    private Rectangle playerdir;
    private Rotate dir = new Rotate();
    private final Translate playerPos = new Translate();
    private Object [][] map;
    private Group g = new Group();
    public Minimap(int cols, int rows){
        map = new Object[cols][rows];
        sizeX=cols*20;
        sizeY=rows*20;

    }
    public void setPlayer(Player p){
        player = new Circle(8,Color.PINK);
        playerdir = new Rectangle(-1.5, -14, 3, 12);
        playerdir.setFill(Color.PINK);
        player.getTransforms().add(new Translate(p.getPositionX(),p.getPositionY()));

        player.getTransforms().add(playerPos);
        playerdir.getTransforms().addAll(playerPos, dir);

        g.getChildren().addAll(player, playerdir);
        updatePlayer(p);
    }
    public double getSizeX(){
        return this.sizeX;
    }
    public double getSizeY(){
        return this.sizeY;
    }

    public Group get(){
        return this.g;
    }
    public void add(int col, int row, int type){
        Rectangle a = new Rectangle(20*col,20*row,19,19);
        switch(type){
            case 0:
                a.setFill(Color.LIGHTGRAY);
                map[col][row]=a;
                g.getChildren().add(a);
                break;
            case 1:
                a.setFill(Color.GRAY);
                map[col][row]=a;
                g.getChildren().add(a);
                break;
            case 2:
                a.setFill(Color.GREEN);
                map[col][row]=a;
                g.getChildren().add(a);
                break;
        }
    }
    public void updatePlayer(Player player) {
        playerPos.setX(player.getPositionX() * 20);
        playerPos.setY(player.getPositionY() * 20);
        double angle = Math.toDegrees(Math.atan2(player.getDirectionX(), -player.getDirectionY()));
        dir.setAngle(angle);
    }
}
