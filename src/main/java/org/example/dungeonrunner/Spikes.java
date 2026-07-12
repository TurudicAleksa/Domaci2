package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Translate;

import java.util.ArrayList;

public class Spikes {
    private double positionX;
    private double positionZ;
    private double positionY;
    private double moveSpd=0.6;
    public double movBuff=0;
    private final Translate slide = new Translate(0, 0, 0);
    private Group s = new Group();
    private ArrayList<MeshView> models = new ArrayList<>();
    TriangleMesh sp = new TriangleMesh();
    float h = (float) (Constants.WALL_HEIGHT/2);
    public Spikes(double positionX, double positionZ){
        sp.getPoints().addAll(
            0, -h, 0
                ,0.2f,0,0.2f
                ,-0.2f,0,0.2f,
                -0.2f,0,-0.2f,
                0.2f,0,-0.2f
        );
        sp.getTexCoords().addAll(0, 0);
        sp.getFaces().addAll(
                0, 0, 1, 0, 2, 0,
                0, 0, 2, 0, 3, 0,
                0, 0, 3, 0, 4, 0,
                0, 0, 4, 0, 1, 0,
                1, 0, 3, 0, 2, 0,
                1, 0, 4, 0, 3, 0
        );
        for(int i=0;i<4;i++){
            MeshView a = new MeshView(sp);
            a.setCullFace(CullFace.NONE);
            PhongMaterial p = new PhongMaterial();
            p.setDiffuseColor(Color.BLACK);
            a.setMaterial(p);
            models.add(a);
        }
        models.get(0).getTransforms().add(new Translate(Constants.CELL_SIZE/4,0,Constants.CELL_SIZE/4));
        models.get(1).getTransforms().add(new Translate(-Constants.CELL_SIZE/4,0,Constants.CELL_SIZE/4));
        models.get(2).getTransforms().add(new Translate(-Constants.CELL_SIZE/4,0,-Constants.CELL_SIZE/4));
        models.get(3).getTransforms().add(new Translate(Constants.CELL_SIZE/4,0,-Constants.CELL_SIZE/4));
        for(int i=0;i<4;i++)
            s.getChildren().add(models.get(i));
        s.getTransforms().addAll(new Translate(positionX,1,positionZ),slide);
    }
    public Group get(){
        return s;
    }
    public void update(double dt){
        movBuff+=dt;
        double t = (Math.sin(movBuff*moveSpd) + 1) / 2.0;
        slide.setY(t * Constants.CELL_SIZE );
    }
    public double getX() { return positionX; }
    public double getZ() { return positionZ; }
    public boolean isDangerous() {

        return s.getBoundsInParent().getCenterY()<1.5;

    }
}
