package io.howarth.move;

public class PieceCoordinates {

    private byte x;
    private byte y;

    public PieceCoordinates(byte x, byte y) {
        this.x = x;
        this.y = y;
    }

    public byte getX() {
        return x;
    }

    public byte getY() {
        return y;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public void setY(byte y) {
        this.y = y;
    }

}
