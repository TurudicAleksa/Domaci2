package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

import java.util.Random;

public class DoorKey {
    private int doorCol;
    private int doorRow;
    private int leverCol;
    private int leverRow;

    private final Color color;
    private boolean open = false;

    private final Box door;
    private final Box lever;
    private final Group g = new Group();

    private static final double PRESS_DISTANCE = Constants.CELL_SIZE * 0.4;

    public DoorKey() {
        color = Color.hsb(new Random().nextDouble() * 360.0, 0.75, 0.9);
        PhongMaterial mat = new PhongMaterial(color);
        mat.setSpecularColor(Color.WHITE);
        door = new Box(Constants.CELL_SIZE, Constants.WALL_HEIGHT, Constants.CELL_SIZE);
        door.setMaterial(mat);
        lever = new Box(Constants.CELL_SIZE * 0.3, Constants.CELL_SIZE * 0.1, Constants.CELL_SIZE * 0.3);
        lever.setMaterial(mat);
        g.getChildren().addAll(door, lever);
    }


    public void setDoorPosition(int col, int row) {
        this.doorCol = col;
        this.doorRow = row;
        door.getTransforms().setAll(new Translate(
                col * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                0,
                row * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0
        ));
    }


    public void setLeverPosition(int col, int row) {
        this.leverCol = col;
        this.leverRow = row;
        lever.getTransforms().setAll(new Translate(
                col * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0,
                Constants.WALL_HEIGHT / 2.0 - Constants.CELL_SIZE * 0.05,
                row * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0
        ));
    }

    public Group get() { return g; }

    public Color getColor() { return color; }

    public int getDoorCol() { return doorCol; }
    public int getDoorRow() { return doorRow; }
    public int getLeverCol() { return leverCol; }
    public int getLeverRow() { return leverRow; }

    public double getDoorX() { return doorCol * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0; }
    public double getDoorZ() { return doorRow * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0; }
    public double getLeverX() { return leverCol * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0; }
    public double getLeverZ() { return leverRow * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0; }

    public boolean isOpen() { return open; }


    public boolean update(double playerWorldX, double playerWorldZ) {
        if (open) return false;

        double dx = playerWorldX - getLeverX();
        double dz = playerWorldZ - getLeverZ();
        double distSq = dx * dx + dz * dz;

        if (distSq < PRESS_DISTANCE * PRESS_DISTANCE) {
            open();
            return true;

        }
        return false;
    }

    private void open() {
        open = true;
        door.setVisible(false);
    }
}