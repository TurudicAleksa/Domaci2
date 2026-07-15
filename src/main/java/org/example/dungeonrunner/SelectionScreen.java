package org.example.dungeonrunner;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class SelectionScreen extends Group {

    private final List<String> options;
    private final Rectangle[] boxes;
    private final Text[] labels;
    private int selectedIndex = 0;

    private static final Color BOX_COLOR = Color.rgb(40, 40, 50);
    private static final Color BOX_SELECTED_COLOR = Color.rgb(120, 80, 200);
    private static final Color BOX_STROKE = Color.WHITE;

    public SelectionScreen(String title, List<String> options, double width, double height) {
        this.options = options;

        Rectangle background = new Rectangle(0, 0, width, height);
        background.setFill(Color.rgb(15, 15, 20));
        getChildren().add(background);

        Text titleText = new Text(title);
        titleText.setFill(Color.WHITE);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setX(width / 2 - titleText.getLayoutBounds().getWidth() / 2);
        titleText.setY(height * 0.25);
        getChildren().add(titleText);

        int count = options.size();
        double boxWidth = 160;
        double boxHeight = 160;
        double spacing = 40;
        double totalWidth = count * boxWidth + (count - 1) * spacing;
        double startX = width / 2 - totalWidth / 2;
        double boxY = height / 2 - boxHeight / 2;

        boxes = new Rectangle[count];
        labels = new Text[count];

        for (int i = 0; i < count; i++) {
            double x = startX + i * (boxWidth + spacing);

            Rectangle box = new Rectangle(x, boxY, boxWidth, boxHeight);
            box.setFill(BOX_COLOR);
            box.setStroke(BOX_STROKE);
            box.setStrokeWidth(2);
            boxes[i] = box;

            Text label = new Text(options.get(i));
            label.setFill(Color.WHITE);
            label.setX(x + boxWidth / 2 - label.getLayoutBounds().getWidth() / 2);
            label.setY(boxY + boxHeight / 2);
            labels[i] = label;

            getChildren().addAll(box, label);
        }

        Text hint = new Text("LEFT / RIGHT  to choose      ENTER to start");
        hint.setFill(Color.LIGHTGRAY);
        hint.setX(width / 2 - hint.getLayoutBounds().getWidth() / 2);
        hint.setY(boxY + boxHeight + 60);
        getChildren().add(hint);

        updateVisuals();
    }

    public void handleKey(KeyCode code) {
        switch (code) {
            case LEFT:
            case A:
                selectedIndex = (selectedIndex - 1 + options.size()) % options.size();
                updateVisuals();
                break;
            case RIGHT:
            case D:
                selectedIndex = (selectedIndex + 1) % options.size();
                updateVisuals();
                break;
            case DIGIT1: selectIfInRange(0); break;
            case DIGIT2: selectIfInRange(1); break;
            case DIGIT3: selectIfInRange(2); break;
            case DIGIT4: selectIfInRange(3); break;
            default:
                break;
        }
    }

    private void selectIfInRange(int index) {
        if (index < options.size()) {
            selectedIndex = index;
            updateVisuals();
        }
    }

    private void updateVisuals() {
        for (int i = 0; i < boxes.length; i++) {
            boolean selected = (i == selectedIndex);
            boxes[i].setFill(selected ? BOX_SELECTED_COLOR : BOX_COLOR);
            boxes[i].setStrokeWidth(selected ? 4 : 2);
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getSelectedOption() {
        return options.get(selectedIndex);
    }
}