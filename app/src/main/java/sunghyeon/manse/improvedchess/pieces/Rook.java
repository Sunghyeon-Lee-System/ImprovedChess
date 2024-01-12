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

public class Rook extends Piece {

    private int direction;

    public Rook(Position pos, boolean color) {
        super(pos, color);
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void setPos(Position pos) {
        super.setPos(pos);

        boolean color = getColor();

        BoardInfo boardInfo = BoardInfo.getInstance();
        if (!boardInfo.isRookMoved(color, direction)) {
            boardInfo.setRookMoved(color, direction, true);
        }
    }

    @Override
    public void caught() {
        super.caught();

        boolean color=getColor();

        BoardInfo boardInfo=BoardInfo.getInstance();
        if(!boardInfo.isRookMoved(color, direction)) {
            boardInfo.setRookMoved(color, direction, true);
        }

        InsufficientDraw insufficientDraw = InsufficientDraw.getInstance();
        insufficientDraw.rookCountAdd(getColor(), -1);
    }

    @Override
    public Drawable getImage(Context context) {
        if (super.getColor() == Constants.WHITE) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.wrook, null);
        } else {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.brook, null);
        }
    }

    @Override
    public ArrayList<HashSet<Position>> getPoses() {
        Position pos = getPos();

        ArrayList<ArrayList<HashSet<Position>>> totalPoses = new ArrayList<>();
        totalPoses.add(getToPoses(pos, -1, 0, true));
        totalPoses.add(getToPoses(pos, 1, 0, true));
        totalPoses.add(getToPoses(pos, 0, -1, true));
        totalPoses.add(getToPoses(pos, 0, 1, true));

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
}
