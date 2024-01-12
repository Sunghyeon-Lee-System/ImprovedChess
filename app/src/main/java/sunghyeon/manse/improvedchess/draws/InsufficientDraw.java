package sunghyeon.manse.improvedchess.draws;

import sunghyeon.manse.improvedchess.Constants;

public class InsufficientDraw extends Draw {
    private final int[] pawnCounts = {8, 8};
    private final int[] rookCounts = {2, 2};
    private final int[] knightCounts = {2, 2};
    private final int[][] bishopCounts = {{1, 1}, {1, 1}};
    private final int[] queenCounts = {1, 1};

    private static final InsufficientDraw insufficientDraw = new InsufficientDraw();

    private InsufficientDraw() {
    }

    public static InsufficientDraw getInstance() {
        return insufficientDraw;
    }

    private int getColorIndex(boolean color) {
        return (color == Constants.WHITE) ? 0 : 1;
    }

    public void pawnCountAdd(boolean color, int add) {
        int colorIndex = getColorIndex(color);
        pawnCounts[colorIndex] += add;
    }

    public void rookCountAdd(boolean color, int add) {
        int colorIndex = getColorIndex(color);
        rookCounts[colorIndex] += add;
    }

    public void knightCountAdd(boolean color, int add) {
        int colorIndex = getColorIndex(color);
        knightCounts[colorIndex] += add;
    }

    public void bishopCountAdd(boolean color, boolean pieceColor, int add) {
        int colorIndex = getColorIndex(color);
        int pieceColorIndex = getColorIndex(pieceColor);
        bishopCounts[colorIndex][pieceColorIndex] += add;
    }

    public void queenCountAdd(boolean color, int add) {
        int colorIndex = getColorIndex(color);
        queenCounts[colorIndex] += add;
    }

    @Override
    public int judgeDraw() {
        boolean noPawn = true;
        boolean noRook = true;
        boolean noQueen = true;

        boolean isDraw = false;

        for (int pawnCount : pawnCounts) {
            if (pawnCount != 0) {
                noPawn = false;
                break;
            }
        }
        for (int rookCount : rookCounts) {
            if (rookCount != 0) {
                noRook = false;
                break;
            }
        }
        for (int queenCount : queenCounts) {
            if (queenCount != 0) {
                noQueen = false;
                break;
            }
        }

        if (noPawn & noRook & noQueen) {
            int bishopKnightCount = 0;
            for (int i = 0; i < knightCounts.length; i++) {
                for (int j = 0; j < bishopCounts.length; j++) {
                    if (bishopCounts[i][j] != 0) {
                        bishopKnightCount++;
                    }
                }
                bishopKnightCount += knightCounts[i];
            }

            if(bishopKnightCount<=1) {
                return Constants.DRAW;
            }
        }

        return Constants.NOT_DRAW;
    }
}
