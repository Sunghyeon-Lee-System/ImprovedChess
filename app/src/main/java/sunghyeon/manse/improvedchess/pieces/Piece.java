package sunghyeon.manse.improvedchess.pieces;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

import sunghyeon.manse.improvedchess.BoardInfo;
import sunghyeon.manse.improvedchess.Position;
import sunghyeon.manse.improvedchess.WritingNotation;
import sunghyeon.manse.improvedchess.draws.RepetitionDraw;
import sunghyeon.manse.improvedchess.draws.TooManyMoveDraw;

public class Piece {
    private Position pos;
    private boolean color;

    //private Position primaryPos;

    public Piece(Position pos, boolean color) {
        this.pos = pos;
        this.color = color;

        //primaryPos = pos;
    }

    /*@Override
    public boolean equals(@Nullable Object obj) {
        Piece piece = (Piece) obj;
        if (piece != null) {
            return pos.equals(piece.pos) && color == piece.color;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int intColor = (color) ? 0 : 1;

        int result = 13;
        result = 37 * result + pos.hashCode();
        result = 37 * result + intColor;
        return result;
    }*/

    public void setPos(Position pos) {
        Position originalPos = this.pos;
        this.pos = pos;

        RepetitionDraw repetitionDraw = RepetitionDraw.getInstance();

        //int primaryPosY = primaryPos.getY();
        /*if (primaryPosY == 1 || primaryPosY == 6) {
            repetitionDraw.resetInfo();
        }else {
            repetitionDraw.setHasSteppedPos(color, primaryPos.getX(), pos);
        }*/
        if (!(this instanceof Pawn)) {
            repetitionDraw.addRepetitionCount();
        } else {
            repetitionDraw.resetInfo();
        }
    }

    public Position getPos() {
        return pos;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean getColor() {
        return color;
    }

    /*public void setPrimaryPos(Position primaryPos) {
        this.primaryPos = primaryPos;
    }

    public Position getPrimaryPos() {
        return primaryPos;
    }*/

    public void caught() {
        android.util.Log.d("ImprovedChess", "caught");

        RepetitionDraw repetitionDraw = RepetitionDraw.getInstance();

        /*int primaryPosY = primaryPos.getY();
        if (primaryPosY == 0 || primaryPosY == 7) {
        }*/

        if (!(this instanceof Pawn)) {
            repetitionDraw.resetInfo();
        }

        TooManyMoveDraw tooManyMoveDraw = TooManyMoveDraw.getInstance();
        tooManyMoveDraw.turnCountClear();
    }

    public ArrayList<HashSet<Position>> getToPoses(Position pos, int toX, int toY, boolean keepUp) {
        HashSet<Position> movablePoses = new HashSet<>();
        HashSet<Position> catchablePoses = new HashSet<>();

        int x = pos.getX();
        int y = pos.getY();
        BoardInfo boardInfo = BoardInfo.getInstance();
        x += toX;
        y += toY;
        if (x >= 0 && x < 8) {
            if (y >= 0 && y < 8) {
                Position newPos = new Position(x, y);
                Piece piece = boardInfo.getPiece(newPos);

                if (piece != null) {
                    if (piece.getColor() != color) {
                        catchablePoses.add(newPos);
                    }
                } else {
                    movablePoses.add(newPos);
                    if (keepUp) {
                        ArrayList<HashSet<Position>> nextPoses = getToPoses(newPos, toX, toY, keepUp);
                        movablePoses.addAll(nextPoses.get(0));
                        catchablePoses.addAll(nextPoses.get(1));
                    }
                }
            }
        }
        ArrayList<HashSet<Position>> poses = new ArrayList<>();
        poses.add(movablePoses);
        poses.add(catchablePoses);
        return poses;
    }

    public Drawable getImage(Context context) {
        return null;
    }

    public ArrayList<HashSet<Position>> getPoses() {
        return null;
    }
}
