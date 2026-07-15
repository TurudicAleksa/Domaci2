package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Shield {
    private double positionX;
    private double positionZ;
    private double baseY;

    private double spinSpeed = 150;  // faster spin than Key/Heart — reads as a spinning coin
    private double bobSpeed = 2.0;
    private double time = 0;

    private final Rotate spin = new Rotate(0, Rotate.Y_AXIS);
    private final Translate bob = new Translate(0, 0, 0);
    private final Group g = new Group();
    private boolean collected = false;

    private static final double SCALE = Constants.CELL_SIZE / 10.0;
    private static final double BOB_RANGE = Constants.CELL_SIZE * 0.06;

    public Shield(double positionX, double positionZ, double baseY) {
        this.positionX = positionX;
        this.positionZ = positionZ;
        this.baseY = baseY;

        PhongMaterial blue = new PhongMaterial(Color.DEEPSKYBLUE);
        blue.setSpecularColor(Color.WHITE);

        // thin flat cylinder to read as a coin
        Cylinder coin = new Cylinder(SCALE * 0.5, SCALE * 0.15);
        coin.setMaterial(blue);
        // Cylinder's default axis is vertical (Y) already, so no extra rotation needed
        // for it to appear "flat like a coin" when spun around Y — matches Key's approach

        g.getChildren().add(coin);

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