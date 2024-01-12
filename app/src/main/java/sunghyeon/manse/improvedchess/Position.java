package sunghyeon.manse.improvedchess;

import androidx.annotation.NonNull;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        Position pos = (Position) object;
        return (x == pos.x & y == pos.y);
    }

    @Override
    public int hashCode() {
        int result = 13;
        result = 37 * result + x;
        result = 37 * result + y;
        return result;
    }
}
