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

public class King extends Piece {

    public King(Position pos, boolean color) {
        super(pos, color);
    }

    @Override
    public void setPos(Position pos) {
        super.setPos(pos);

        boolean color = getColor();
        BoardInfo boardInfo = BoardInfo.getInstance();
        if (!boardInfo.isKingMoved(color)) boardInfo.setKingMoved(color, true);
    }

    @Override
    public Drawable getImage(Context context) {
        if (super.getColor() == Constants.WHITE) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.wking, null);
        } else {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.bking, null);
        }
    }

    @Override
    public ArrayList<HashSet<Position>> getPoses() {
        Position pos = getPos();
        boolean color = getColor();
        BoardInfo boardInfo = BoardInfo.getInstance();

        ArrayList<ArrayList<HashSet<Position>>> totalPoses = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    /*Position newPos = new Position(pos.getX() + i, pos.getY() + j);
                    Piece kingPiece = boardInfo.getPiece(pos);

                    boardInfo.setPiece(null, pos, false);
                    boardInfo.setKingPos(newPos, color);

                    if (!boardInfo.isCheck(color)) {

                    }

                    boardInfo.setPiece(kingPiece, pos, false);
                    boardInfo.setKingPos(pos, color);*/

                    totalPoses.add(getToPoses(pos, i, j, false));
                }
            }
        }

        ArrayList<HashSet<Position>> poses = new ArrayList<>();
        HashSet<Position> movablePoses = new HashSet<>();
        HashSet<Position> catchablePoses = new HashSet<>();

        for (ArrayList<HashSet<Position>> nextPoses : totalPoses) {
            movablePoses.addAll(nextPoses.get(0));
            catchablePoses.addAll(nextPoses.get(1));
        }

        poses.add(movablePoses);
        poses.add(catchablePoses);
        return poses;
    }

    public boolean[] isPossibleCastling() {
        boolean[] isPossible = new boolean[2];
        isPossible[0] = true;
        isPossible[1] = true;

        boolean color = getColor();
        BoardInfo boardInfo = BoardInfo.getInstance();

        boolean isCheck = boardInfo.isCheck(color);

        if (!isCheck) {
            int backrank = (color == Constants.WHITE) ? 7 : 0;

            int[] directions = {Constants.KINGSIDE, Constants.QUEENSIDE};
            int[][] startEnd = {{5, 6}, {1, 3}};

            for (int index = 0; index < directions.length; index++) {
                if (!boardInfo.isRookAndKingMoved(color, directions[index])) {
                    for (int i = startEnd[index][0]; i <= startEnd[index][1]; i++) {
                        Position pos = new Position(i, backrank);
                        if (boardInfo.getPiece(pos) == null) {
                            boardInfo.setKingPos(pos, color);
                            if (boardInfo.isCheck(color)) {
                                isPossible[index] = false;
                            }
                        } else {
                            isPossible[index] = false;
                        }
                    }

                    boardInfo.setKingPos(new Position(4, backrank), color);
                } else {
                    isPossible[index] = false;
                }
            }
        } else {
            isPossible[0] = false;
            isPossible[1] = false;
        }

        return isPossible;
    }
}
