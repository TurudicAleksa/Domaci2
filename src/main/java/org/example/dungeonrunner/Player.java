package org.example.dungeonrunner;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class Player {
    private double positionX;
    private double startX;
    private double startY;
    private double positionY;
    private double directionX;
    private double directionY;
    private boolean moveForward;
    private boolean moveBackward;
    private boolean rotateLeft;
    private boolean rotateRight;
    private int lives=3;
    private int poisoned=0;
    private DoorKey door;

    public Player ( double startX, double startY ) {
        this.positionX = startX;
        this.positionY = startY;
        this.startX=startX;
        this.startY=startY;
        this.directionX =  1.0;
        this.directionY =  0.0;
    }

    public void setDoor(DoorKey door) {
        this.door = door;
    }

    private boolean isClosed(int x, int y) {
        return door != null
                && door.getDoorCol() == x
                && door.getDoorRow() == y
                && !door.isOpen();
    }

    public double getPositionX ( ) { return this.positionX; }
    public double getPositionY ( ) { return this.positionY; }
    public double getDirectionX ( ) { return this.directionX; }
    public double getDirectionY ( ) { return this.directionY; }

    public void setMoveForward  ( boolean newValue ) { this.moveForward  = newValue; }
    public void setMoveBackward ( boolean newValue ) { this.moveBackward = newValue; }
    public void setRotateLeft   ( boolean newValue ) { this.rotateLeft   = newValue; }
    public void setRotateRight  ( boolean newValue ) { this.rotateRight  = newValue; }

    public void update ( DungeonMap map ) {
        if(poisoned>0) poisoned--;
        if ( this.moveForward ) {
            double newX,newY;
            if(poisoned>0){
                newX = this.positionX - this.directionX * Constants.PLAYER_MOVE_SPEED;
                newY = this.positionY - this.directionY * Constants.PLAYER_MOVE_SPEED;
            }
            else{
                newX = this.positionX + this.directionX * Constants.PLAYER_MOVE_SPEED;
                newY = this.positionY + this.directionY * Constants.PLAYER_MOVE_SPEED;
            }



            if ( canMoveTo ( newX, positionY, map ) ) {
                this.positionX = newX;
            }

            if ( canMoveTo ( positionX, newY, map ) ) {
                this.positionY = newY;
            }
        }

        if ( this.moveBackward ) {
            double newX,newY;
            if(poisoned==0){
                newX = this.positionX - this.directionX * Constants.PLAYER_MOVE_SPEED;
                newY = this.positionY - this.directionY * Constants.PLAYER_MOVE_SPEED;
            }
            else{
                newX = this.positionX + this.directionX * Constants.PLAYER_MOVE_SPEED;
                newY = this.positionY + this.directionY * Constants.PLAYER_MOVE_SPEED;
            }


            if ( canMoveTo ( newX, positionY, map ) ) {
                this.positionX = newX;
            }

            if ( canMoveTo ( positionX, newY, map ) ) {
                this.positionY = newY;
            }
        }

        if ( this.rotateLeft ) {
            if(poisoned>0){
                rotate ( -Constants.PLAYER_ROTATION_SPEED );
            }
            else
                rotate ( Constants.PLAYER_ROTATION_SPEED );
        }
        if ( this.rotateRight ) {
            if(poisoned>0)
                rotate ( Constants.PLAYER_ROTATION_SPEED );
            else
                rotate ( -Constants.PLAYER_ROTATION_SPEED );
        }
    }

    public boolean isAtExit ( DungeonMap map ) {
        return map.get ( ( int ) this.positionX, ( int ) this.positionY ) == Constants.EXIT;
    }
    private boolean canMoveTo ( double x, double y, DungeonMap map ) {
        return isFree ( ( int ) ( x + Constants.PLAYER_RADIUS ), ( int ) ( y + Constants.PLAYER_RADIUS ), map )
            && isFree ( ( int ) ( x + Constants.PLAYER_RADIUS ), ( int ) ( y - Constants.PLAYER_RADIUS ), map )
            && isFree ( ( int ) ( x - Constants.PLAYER_RADIUS ), ( int ) ( y + Constants.PLAYER_RADIUS ), map )
            && isFree ( ( int ) ( x - Constants.PLAYER_RADIUS ), ( int ) ( y - Constants.PLAYER_RADIUS ), map );
    }

    private boolean exitUnlocked = false;
    public void unlockExit() { this.exitUnlocked = true; }

    private boolean isFree(int x, int y, DungeonMap map) {
        int tile = map.get(x, y);
        if (tile == Constants.EXIT ) {
            return exitUnlocked;
        }
        if (tile == Constants.EMPTY && isClosed(x,y)) {
            return false;
        }
        return tile == Constants.EMPTY;
    }

    private void rotate ( double angle ) {
        Point2D newDirection = new Rotate ( Math.toDegrees ( angle ) ).transform ( this.directionX, this.directionY );
        this.directionX = newDirection.getX ( );
        this.directionY = newDirection.getY ( );
    }
    public int loselife()
    {
        lives--;
        this.positionX=startX;
        this.positionY=startY;
        return lives;
    }
    public int getLives(){
        return lives;
    }
    public void addLife(){
        lives++;
    }
    public void poison(){
        this.poisoned=250;
    }
}