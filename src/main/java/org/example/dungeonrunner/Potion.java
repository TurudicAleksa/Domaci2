package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Potion {
    private double positionX;
    private double positionZ;
    private double baseY;

    private double spinSpeed = 90;
    private double bobSpeed = 2.2;
    private double time = 0;

    private final Rotate spin = new Rotate(0, Rotate.Y_AXIS);
    private final Translate bob = new Translate(0, 0, 0);
    private final Group g = new Group();
    private boolean collected = false;

    private static final double SCALE = Constants.CELL_SIZE / 10.0;
    private static final double BOB_RANGE = Constants.CELL_SIZE * 0.06;

    public Potion(double positionX, double positionZ, double baseY) {
        this.positionX = positionX;
        this.positionZ = positionZ;
        this.baseY = baseY;

        PhongMaterial pot = new PhongMaterial(Color.rgb(140, 40, 180)); // pot purple
        pot.setSpecularColor(Color.MAGENTA);

        Cylinder bodyShape = new Cylinder(SCALE * 0.3, SCALE * 0.6);
        bodyShape.getTransforms().add(new Translate(0, SCALE * 0.1, 0));
        Cylinder neck = new Cylinder(SCALE * 0.12, SCALE * 0.3);
        neck.getTransforms().add(new Translate(0, -SCALE * 0.35, 0));

        for (Cylinder shape : new Cylinder[]{bodyShape, neck}) {
            shape.setMaterial(pot);
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