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

public class Knight extends Piece {
    public Knight(Position pos, boolean color) {
        super(pos, color);
    }

    @Override
    public void caught() {
        super.caught();

        InsufficientDraw insufficientDraw = InsufficientDraw.getInstance();
        insufficientDraw.knightCountAdd(getColor(), -1);
    }

    @Override
    public Drawable getImage(Context context) {
        if (super.getColor() == Constants.WHITE) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.wknight, null);
        } else {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.bknight, null);
        }
    }

    @Override
    public ArrayList<HashSet<Position>> getPoses() {
        Position pos = getPos();

        ArrayList<ArrayList<HashSet<Position>>> totalPoses = new ArrayList<>();
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                for (int k = 1; k <= 2; k++) {
                    totalPoses.add(getToPoses(pos, i * k, j * (3 - k), false));
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
}
