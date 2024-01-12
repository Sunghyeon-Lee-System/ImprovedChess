package sunghyeon.manse.improvedchess.pieces;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.HashSet;

import sunghyeon.manse.improvedchess.BoardInfo;
import sunghyeon.manse.improvedchess.Constants;
import sunghyeon.manse.improvedchess.Position;
import sunghyeon.manse.improvedchess.R;
import sunghyeon.manse.improvedchess.draws.InsufficientDraw;
import sunghyeon.manse.improvedchess.draws.TooManyMoveDraw;

public class Pawn extends Piece {
    private int index;

    public Pawn(Position pos, boolean color) {
        super(pos, color);
    }

    @Override
    public void setPos(Position pos) {
        super.setPos(pos);

        boolean color = getColor();

        BoardInfo boardInfo = BoardInfo.getInstance();
        if (!boardInfo.isPawnMoved(color, index)) {
            boardInfo.setPawnMoved(color, index, true);
        }

        TooManyMoveDraw tooManyMoveDraw=TooManyMoveDraw.getInstance();
        tooManyMoveDraw.turnCountClear();
    }

    @Override
    public void caught() {
        super.caught();

        InsufficientDraw insufficientDraw = InsufficientDraw.getInstance();
        insufficientDraw.pawnCountAdd(getColor(), -1);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Drawable getImage(Context context) {
        if (super.getColor() == Constants.WHITE) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.wpawn, null);
        } else {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.bpawn, null);
        }
    }

    @Override
    public ArrayList<HashSet<Position>> getPoses() {
        HashSet<Position> movablePoses = new HashSet<>();
        HashSet<Position> catchablePoses = new HashSet<>();

        Position pos = super.getPos();
        int x = pos.getX();
        int y = pos.getY();
        boolean color = super.getColor();
        BoardInfo boardInfo = BoardInfo.getInstance();

        int direction = (color == Constants.WHITE) ? -1 : 1;

        int count = 1;

        if (!boardInfo.isPawnMoved(color, index)) {
            count++;
        }

        if (count == 2) {
            int[] enpassantXs = {x + 1, x - 1};
            int enpassantY = y + 2 * direction;

            boolean oppositionColor = (color == Constants.WHITE) ? Constants.BLACK : Constants.WHITE;
            int[] enpassantTypes = {Constants.LEFT_ENPASSANT, Constants.RIGHT_ENPASSANT};

            for (int i = 0; i < enpassantXs.length; i++) {
                int enpassantX = enpassantXs[i];
                int enpassantType = enpassantTypes[i];

                if (enpassantX >= 0 && enpassantX < 8) {
                    Position enpassantPos = new Position(enpassantX, enpassantY);
                    Piece enpassantPiece = boardInfo.getPiece(enpassantPos);
                    if (enpassantPiece instanceof Pawn) {
                        if (enpassantPiece.getColor() != color) {
                            boardInfo.setEnpassant(oppositionColor, enpassantX, enpassantType);
                        }
                    }
                }
            }
        }

        while (count != 0) {
            y += direction;
            if (y >= 0 && y < 8) {
                if (boardInfo.getPiece(new Position(x, y)) == null) {
                    movablePoses.add(new Position(x, y));
                }else{
                    break;
                }
            }
            count--;
        }

        ArrayList<ArrayList<HashSet<Position>>> totalCatchablePoses = new ArrayList<>();
        totalCatchablePoses.add(getToPoses(pos, -1, direction, false));
        totalCatchablePoses.add(getToPoses(pos, 1, direction, false));

        catchablePoses.addAll(totalCatchablePoses.get(0).get(1));
        catchablePoses.addAll(totalCatchablePoses.get(1).get(1));

        int oppositionBackRank = (color == Constants.WHITE) ? 0 : 7;

        for (Position nextPos : movablePoses) {
            if (nextPos.getY() == oppositionBackRank) {
                boardInfo.getPromotionPos().add(nextPos);
            }
        }

        for (Position nextPos : catchablePoses) {
            if (nextPos.getY() == oppositionBackRank) {
                boardInfo.getPromotionPos().add(nextPos);
            }
        }

        ArrayList<HashSet<Position>> poses = new ArrayList<>();
        poses.add(movablePoses);
        poses.add(catchablePoses);

        return poses;
    }

    public Position enpassantRelatedPosition(int enpassantType, boolean isPieceClicked) {
        Position currentPos = getPos();

        int[] enpassantRank = {3, 4};
        int colorIndex = (getColor() == Constants.WHITE) ? 0 : 1;

        if (enpassantRank[colorIndex] != currentPos.getY()) {
            return null;
        }

        int directionX = 0;
        if (enpassantType == Constants.NO_ENPASSANT) {
            return null;
        } else if (enpassantType == Constants.LEFT_ENPASSANT) {
            directionX = -1;
        } else if (enpassantType == Constants.RIGHT_ENPASSANT) {
            directionX = 1;
        }

        if (isPieceClicked) {
            int directionY = (getColor() == Constants.WHITE) ? -1 : 1;
            return new Position(currentPos.getX() + directionX, currentPos.getY() + directionY);
        } else {
            return new Position(currentPos.getX() + directionX, currentPos.getY());
        }
    }
}
