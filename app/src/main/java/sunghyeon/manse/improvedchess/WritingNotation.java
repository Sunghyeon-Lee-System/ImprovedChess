package sunghyeon.manse.improvedchess;

import java.util.ArrayList;

import sunghyeon.manse.improvedchess.pieces.Bishop;
import sunghyeon.manse.improvedchess.pieces.King;
import sunghyeon.manse.improvedchess.pieces.Knight;
import sunghyeon.manse.improvedchess.pieces.Pawn;
import sunghyeon.manse.improvedchess.pieces.Piece;
import sunghyeon.manse.improvedchess.pieces.Queen;
import sunghyeon.manse.improvedchess.pieces.Rook;

public class WritingNotation {
    boolean isCaught = false;

    private ArrayList<Move> notation = new ArrayList<>();

    private static final WritingNotation writingNotation = new WritingNotation();

    private WritingNotation() {
    }

    public static WritingNotation getInstance() {
        return writingNotation;
    }

    public void setCaught() {
        isCaught = true;
    }

    public void write(Position fromPos, Position toPos, int kindOfPiece, boolean isCaught) {

        notation.add(new Move(fromPos, toPos, kindOfPiece, isCaught));
    }

    public void printNotation() {
        Piece[][] board = new Piece[8][8];

        StringBuilder strNotation = new StringBuilder();

        String[] pileNames = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] rankNames = {"8", "7", "6", "5", "4", "3", "2", "1"};
        String[] pieceNames = {null, "R", "N", "B", "Q", "K"};

        int count = 1;
        boolean turnColor = Constants.WHITE;
        for (Move move : notation) {
            if (turnColor == Constants.WHITE) {
                if (count != 1) {
                    strNotation.append("\n");
                }
                strNotation.append(count);
                strNotation.append(". ");
            } else {
                strNotation.append(" ");
                count++;
            }

            Position toPos = move.toPos;
            if (toPos != null) {
                strNotation.append(pieceNames[move.kindOfPiece]);
                strNotation.append(pileNames[toPos.getX()]);
                strNotation.append(rankNames[toPos.getY()]);
            } else {
                int direction = move.kindOfPiece;
                if (direction == Constants.KINGSIDE) {
                    strNotation.append("O-O");
                } else {
                    strNotation.append("O-O-O");
                }
            }

            turnColor = !turnColor;
        }
    }

    private static class Move {
        private final Position fromPos;
        private final Position toPos;
        private final int kindOfPiece;
        private final boolean isCaught;

        public Move(Position fromPos, Position toPos, int kindOfPiece, boolean isCaught) {
            this.fromPos = fromPos;
            this.toPos = toPos;
            this.kindOfPiece = kindOfPiece;
            this.isCaught = isCaught;
        }
    }
}
