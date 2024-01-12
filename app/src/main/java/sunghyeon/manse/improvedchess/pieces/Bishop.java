package sunghyeon.manse.improvedchess.pieces;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.HashSet;

import sunghyeon.manse.improvedchess.Constants;
import sunghyeon.manse.improvedchess.Position;
import sunghyeon.manse.improvedchess.R;
import sunghyeon.manse.improvedchess.draws.InsufficientDraw;

public class Bishop extends Piece {
    public Bishop(Position pos, boolean color) {
        super(pos, color);
    }

    @Override
    public void caught() {
        super.caught();

        Position pos = getPos();
        boolean color = getColor();
        boolean pieceColor = ((pos.getX() + pos.getY()) % 2 == 0) ? Constants.WHITE : Constants.BLACK;

        InsufficientDraw insufficientDraw = InsufficientDraw.getInstance();
        insufficientDraw.bishopCountAdd(color, pieceColor, -1);
    }

    @Override
    public Drawable getImage(Context context) {
        if (super.getColor() == Constants.WHITE) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.wbishop, null);
        } else {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.bbishop, null);
        }
    }

    @Override
    public ArrayList<HashSet<Position>> getPoses() {
        Position pos = getPos();

        ArrayList<ArrayList<HashSet<Position>>> totalPoses = new ArrayList<>();
        totalPoses.add(getToPoses(pos, -1, -1, true));
        totalPoses.add(getToPoses(pos, -1, 1, true));
        totalPoses.add(getToPoses(pos, 1, -1, true));
        totalPoses.add(getToPoses(pos, 1, 1, true));

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
