package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Heart {
    private double positionX;
    private double positionZ;
    private double baseY;

    private double spinSpeed = 60;   // degrees per second
    private double bobSpeed = 2.5;
    private double time = 0;

    private final Rotate spin = new Rotate(0, Rotate.Y_AXIS);
    private final Translate bob = new Translate(0, 0, 0);
    private final Group g = new Group();
    private boolean collected = false;

    private static final double SCALE = Constants.CELL_SIZE / 10.0;
    private static final double BOB_RANGE = Constants.CELL_SIZE * 0.06;

    public Heart(double positionX, double positionZ, double baseY) {
        this.positionX = positionX;
        this.positionZ = positionZ;
        this.baseY = baseY;

        PhongMaterial red = new PhongMaterial(Color.CRIMSON);
        red.setSpecularColor(Color.PINK);

        Sphere lobeLeft = new Sphere(SCALE * 0.35);
        Sphere lobeRight = new Sphere(SCALE * 0.35);
        lobeLeft.getTransforms().add(new Translate(-SCALE * 0.3, -SCALE * 0.15, 0));
        lobeRight.getTransforms().add(new Translate(SCALE * 0.3, -SCALE * 0.15, 0));

        Box point = new Box(SCALE * 0.5, SCALE * 0.5, SCALE * 0.5);
        point.getTransforms().addAll(
                new Translate(0, SCALE * 0.25, 0),
                new Rotate(45, Rotate.Z_AXIS)
        );

        for (Shape3D shape : new Shape3D[]{lobeLeft, lobeRight, point}) {
            shape.setMaterial(red);
            g.getChildren().add(shape);
        }

        g.getTransforms().addAll(
                new Translate(positionX, baseY, positionZ),
                bob,
                spin
        );
    }

    public Group get() { return g; }

    public void update(double dt) {
        if (collected) return;
        time += dt;
        spin.setAngle(spin.getAngle() + spinSpeed * dt);
        bob.setY(Math.sin(time * bobSpeed) * BOB_RANGE);
    }

    public double getX() { return positionX; }
    public double getZ() { return positionZ; }

    public boolean isCollected() { return collected; }
    public void collect() {
        collected = true;
        g.setVisible(false);
    }
}