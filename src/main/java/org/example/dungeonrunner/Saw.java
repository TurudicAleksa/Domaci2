package org.example.dungeonrunner;


import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Saw {
    private double rotateSpd=180;
    private double moveSpd=2.0;
    private double rot;
    private double positionX;
    private double positionZ;
    private double movBuff=0;
    private final Rotate spin = new Rotate(0, Rotate.Z_AXIS);
    private final Rotate orientation = new Rotate(0,Rotate.Y_AXIS);
    private final Translate slide = new Translate(0, 0, 0);
    private Group s = new Group();

    Box one = new Box (Constants.CELL_SIZE/2,Constants.CELL_SIZE/2,0.1);
    Box two = new Box (Constants.CELL_SIZE/2,Constants.CELL_SIZE/2,0.1);

    public Saw(double positionX, double positionZ) {
        this.positionX = positionX;
        this.positionZ = positionZ;
        Translate wallTranslate = new Translate (positionX, 0, positionZ);
        Rotate scnd = new Rotate(45);
        two.getTransforms().addAll(scnd);
        s.getChildren().add(one);
        s.getChildren().add(two);
        s.getTransforms().addAll(wallTranslate,orientation,slide,spin);
    }
    public Group getSaws(){
        return s;
    }
    public void update(double dt) {
        movBuff+=dt;
        spin.setAngle(spin.getAngle() + rotateSpd * dt);
        double t = (Math.sin(movBuff*moveSpd) + 1) / 2.0;
        slide.setX(t * Constants.CELL_SIZE / 2.0);
    }
    public void rotate(double rot){
        orientation.setAngle(rot);

    }
    public double getX(){
        Rotate temp = new Rotate(orientation.getAngle(), Rotate.Y_AXIS);
        Point3D dir = temp.transform(1, 0, 0);
        return positionX + slide.getX() * dir.getX();
    }
    public double getZ(){
        Rotate temp = new Rotate(orientation.getAngle(), Rotate.Y_AXIS);
        Point3D dir = temp.transform(1, 0, 0);
        return positionZ + slide.getX() * dir.getZ();
    }

}
