package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Key {
    private double positionX;
    private double positionZ;
    private double positionY;

    private double spinSpeed = 90;
    private double bobSpeed = 1;
    private double bobRange = 0.3;
    private double time = 0;

    private final Rotate spin = new Rotate(0, Rotate.Y_AXIS);
    private final Translate bob = new Translate(0, 0, 0);
    private final Group g = new Group();
    private boolean collected = false;

    public Key(double positionX, double positionZ, double positionY) {
        this.positionX = positionX;
        this.positionZ = positionZ;
        this.positionY = positionY;

        PhongMaterial gold = new PhongMaterial(Color.GOLDENROD);
        gold.setSpecularColor(Color.YELLOW);

        Box Top   = new Box(0.4, 0.1, 0.1);
        Box Left  = new Box(0.1, 0.5, 0.1);
        Box Right = new Box(0.1, 0.5, 0.1);
        Top.getTransforms().add(new Translate(0, -0.5, 0));
        Left.getTransforms().add(new Translate(-0.25, -0.2, 0));
        Right.getTransforms().add(new Translate(0.25, -0.2, 0));

        Box dole = new Box(0.1, 0.7, 0.1);
        dole.getTransforms().add(new Translate(0, 0.4, 0));

        Box zub = new Box(0.2, 0.1, 0.1);
        zub.getTransforms().add(new Translate(0.1, 0.5, 0));

        Box zub2 = new Box(0.25, 0.1, 0.1);
        zub2.getTransforms().add(new Translate(0.1, 0.7, 0));

        for (Box b : new Box[]{Top, Left, Right, dole, zub, zub2}) {
            b.setMaterial(gold);
            g.getChildren().add(b);
        }

        g.getTransforms().addAll(
                new Translate(positionX, positionY, positionZ),
                bob,
                spin
        );
    }

    public Group get() { return g; }

    public void update(double dt) {
        if (collected) return;
        time += dt;
        spin.setAngle(spin.getAngle() + spinSpeed * dt);
        bob.setY(Math.sin(time * bobSpeed) * bobRange);
    }

    public double getX() { return positionX; }
    public double getZ() { return positionZ; }

    public boolean isCollected() { return collected; }
    public void collect() {
        collected = true;
        g.setVisible(false);
    }
}
