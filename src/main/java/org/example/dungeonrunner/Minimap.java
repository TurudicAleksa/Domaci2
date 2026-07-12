package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.paint.Color;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

import java.awt.*;

public class Minimap {
    private double sizeX;
    private double sizeY;
    private double boxSizeX;
    private double boxSizeY;
    private Object [][] map;
    private Group g = new Group();
    public Minimap(int cols, int rows){
        map = new Object[cols][rows];
        sizeX=cols*20;
        sizeY=rows*20;
    }
    public double getSizeX(){
        return this.sizeX;
    }
    public double getSizeY(){
        return this.sizeY;
    }
    public void setPos(double x, double y){
        System.out.println(x-sizeX);
        System.out.println(y-sizeY);
        g.getTransforms().add(new Translate(x-sizeX,y-sizeY));
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
                System.out.println(map[col][row]);
                break;
            case 1:
                a.setFill(Color.GRAY);
                map[col][row]=a;
                g.getChildren().add(a);
                System.out.println(map[col][row]);
                break;
            case 2:
                a.setFill(Color.GREEN);
                map[col][row]=a;
                g.getChildren().add(a);
                System.out.println(map[col][row]);
                break;
        }
    }
}
