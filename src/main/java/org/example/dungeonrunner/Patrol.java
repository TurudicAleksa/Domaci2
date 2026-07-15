package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class Patrol {
    private Sphere body = new Sphere();
    private double posX;
    private double posY;
    private double[] pathX;
    private double[] pathY;
    private int index=0;
    private double dirX;
    private double dirY;
    private Group p=new Group();
    private Translate move = new Translate();

    public Patrol(double posX, double posY){
        this.posX=posX;
        this.posY=posY;
        body = new Sphere(0.5);
        body.setMaterial(new PhongMaterial(Color.RED));
        //body.getTransforms().addAll(new Translate(posX,0.2,posY));
        p.getChildren().add(body);
        move.setX(posX);
        move.setZ(posY);
        p.getTransforms().add(move);
    }
    public void addPath(double[] x, double[] y){
        pathX = new double[x.length];
        pathY = new double[y.length];
        for (int i = 0; i < x.length; i++) {
            pathX[i] = x[i] * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0;
            pathY[i] = y[i] * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0;
        }
    }
    public void update(double dt){
           setDir();
            posX += dt * dirX;
            posY += dt * dirY;
            move.setX(posX);
            move.setZ(posY);
    }
    private void setDir(){
        double targetX = pathX[index];
        double targetY = pathY[index];
        double dx = targetX - posX;
        double dy = targetY - posY;
        double dist = Math.sqrt(dx*dx + dy*dy);

        if (dist < Constants.CELL_SIZE * 0.1) {
            index = (index + 1) % pathX.length;
        }
        if (dist > 0.0001) {
            dirX = dx / dist;
            dirY = dy / dist;
        }
    }
    public Group get(){
        return p;
    }

}
