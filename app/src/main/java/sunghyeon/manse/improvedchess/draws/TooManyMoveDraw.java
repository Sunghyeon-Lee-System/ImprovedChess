package sunghyeon.manse.improvedchess.draws;

import sunghyeon.manse.improvedchess.Constants;

public class TooManyMoveDraw extends Draw {
    int turnCount = 0;

    private static final TooManyMoveDraw tooManyMoveDraw = new TooManyMoveDraw();

    private TooManyMoveDraw() {
    }

    public static TooManyMoveDraw getInstance() {
        return tooManyMoveDraw;
    }

    public void addTurnCount() {
        turnCount++;
    }

    public void turnCountClear() {
        turnCount = -1;
    }

    @Override
    public int judgeDraw() {
        //android.util.Log.d("ImprovedChess", Integer.toString(turnCount));
        if (turnCount == 50) {
            return Constants.NEED_CONFIRM_DRAW;
        } else if (turnCount == 75) {
            return Constants.DRAW;
        } else {
            return Constants.NOT_DRAW;
        }
    }
}
