package sunghyeon.manse.improvedchess;

import java.util.Arrays;
import java.util.HashSet;

import sunghyeon.manse.improvedchess.pieces.Bishop;
import sunghyeon.manse.improvedchess.pieces.King;
import sunghyeon.manse.improvedchess.pieces.Knight;
import sunghyeon.manse.improvedchess.pieces.Pawn;
import sunghyeon.manse.improvedchess.pieces.Piece;
import sunghyeon.manse.improvedchess.pieces.Queen;
import sunghyeon.manse.improvedchess.pieces.Rook;

public class BoardInfo {
    private final Piece[][] board = new Piece[8][8];
    private final Position[] kingPoses = new Position[2];
    private final boolean[][] pawnsMoved = new boolean[2][8];
    private final boolean[][] rooksMoved = new boolean[2][2];
    private final boolean[] kingsMoved = new boolean[2];

    private final HashSet<Position> promotionPos = new HashSet<>();
    private final int[][] enpassant = new int[2][8];

    private static final BoardInfo boardInfo = new BoardInfo();

    private BoardInfo() {
        init();
    }

    private void init() {
        boolean[] colors = {Constants.WHITE, Constants.BLACK};
        int[] pawnRank = {6, 1};
        int[] othersRank = {7, 0};
        for (int i = 0; i < 8; i++) {
            for (int index = 0; index < colors.length; index++) {
                Pawn pawn = new Pawn(new Position(i, pawnRank[index]), colors[index]);
                pawn.setColor(colors[index]);
                pawn.setIndex(i);
                board[i][pawnRank[index]] = pawn;
            }
        }
        for (int index = 0; index < colors.length; index++) {
            boolean color = colors[index];

            Rook lRook = new Rook(new Position(0, othersRank[index]), color);
            Rook rRook = new Rook(new Position(7, othersRank[index]), color);
            lRook.setColor(colors[index]);
            rRook.setColor(colors[index]);
            lRook.setDirection(Constants.QUEENSIDE);
            rRook.setDirection(Constants.KINGSIDE);
            board[0][othersRank[index]] = lRook;
            board[7][othersRank[index]] = rRook;

            Knight lKnight = new Knight(new Position(1, othersRank[index]), color);
            Knight rKnight = new Knight(new Position(6, othersRank[index]), color);
            lKnight.setColor(colors[index]);
            rKnight.setColor(colors[index]);
            board[1][othersRank[index]] = lKnight;
            board[6][othersRank[index]] = rKnight;

            Bishop lBishop = new Bishop(new Position(2, othersRank[index]), color);
            Bishop rBishop = new Bishop(new Position(5, othersRank[index]), color);
            lBishop.setColor(colors[index]);
            rBishop.setColor(colors[index]);
            board[2][othersRank[index]] = lBishop;
            board[5][othersRank[index]] = rBishop;

            Queen queen = new Queen(new Position(3, othersRank[index]), color);
            queen.setColor(colors[index]);
            board[3][othersRank[index]] = queen;

            Position kingPos = new Position(4, othersRank[index]);
            King king = new King(kingPos, color);
            king.setColor(colors[index]);
            board[4][othersRank[index]] = king;

            int colorIndex = (color == Constants.WHITE) ? 0 : 1;

            kingPoses[colorIndex] = kingPos;
        }

        for (int index = 0; index < 2; index++) {
            setKingPos(new Position(4, othersRank[index]), colors[index]);
        }

        for (int i = 0; i < pawnsMoved.length; i++) {
            for (int j = 0; j < pawnsMoved[0].length; j++) {
                pawnsMoved[i][j] = false;
            }
        }

        for (int i = 0; i < rooksMoved.length; i++) {
            for (int j = 0; j < rooksMoved[0].length; j++) {
                rooksMoved[i][j] = false;
            }
        }

        Arrays.fill(kingsMoved, false);
    }

    public static BoardInfo getInstance() {
        return boardInfo;
    }

    public void setPiece(Piece piece, Position pos, boolean alert) {
        WritingNotation writingNotation = WritingNotation.getInstance();

        Piece originalPiece = getPiece(pos);
        board[pos.getX()][pos.getY()] = piece;

        if (piece != null) {
            if (alert) {
                Position originalPos = piece.getPos();
                piece.setPos(pos);

                int kindOfPiece;

                if (piece instanceof Pawn) {
                    kindOfPiece = Constants.PAWN;
                } else if (piece instanceof Rook) {
                    kindOfPiece = Constants.ROOK;
                } else if (piece instanceof Knight) {
                    kindOfPiece = Constants.KNIGHT;
                } else if (piece instanceof Bishop) {
                    kindOfPiece = Constants.BISHOP;
                } else if (piece instanceof Queen) {
                    kindOfPiece = Constants.QUEEN;
                } else if (piece instanceof King) {
                    kindOfPiece = Constants.KING;
                } else {
                    kindOfPiece = 0;
                }

                if (originalPiece != null) {
                    originalPiece.caught();
                    writingNotation.write(originalPos, pos, kindOfPiece, true);
                } else {
                    writingNotation.write(originalPos, pos, kindOfPiece, false);
                }
            }

            if (piece instanceof King) {
                setKingPos(pos, piece.getColor());
            }
        }
    }

    public Piece getPiece(Position pos) {
        return board[pos.getX()][pos.getY()];
    }

    public void setKingPos(Position pos, boolean color) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        kingPoses[colorIndex] = pos;
    }

    public Position getKingPos(boolean color) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        return kingPoses[colorIndex];
    }

    public boolean isPawnMoved(boolean color, int index) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        return pawnsMoved[colorIndex][index];
    }

    public void setPawnMoved(boolean color, int index, boolean moved) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        pawnsMoved[colorIndex][index] = moved;
    }

    public boolean isRookMoved(boolean color, int direction) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        int directionIndex = (direction == Constants.KINGSIDE) ? 0 : 1;

        return rooksMoved[colorIndex][directionIndex];
    }

    public void setRookMoved(boolean color, int direction, boolean moved) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        int directionIndex = (direction == Constants.KINGSIDE) ? 0 : 1;

        rooksMoved[colorIndex][directionIndex] = moved;
    }

    public boolean isKingMoved(boolean color) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        return kingsMoved[colorIndex];
    }

    public void setKingMoved(boolean color, boolean moved) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;

        kingsMoved[colorIndex] = moved;
    }

    public boolean isRookAndKingMoved(boolean color, int direction) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        int directionIndex = (direction == Constants.KINGSIDE) ? 0 : 1;

        return rooksMoved[colorIndex][directionIndex] | kingsMoved[colorIndex];
    }

    public HashSet<Position> getPromotionPos() {
        return promotionPos;
    }

    public void doCastling(boolean color, int side) {
        WritingNotation writingNotation = WritingNotation.getInstance();

        int backrank = (color == Constants.WHITE) ? 7 : 0;

        if (side == Constants.KINGSIDE) {
            board[6][backrank] = board[4][backrank];
            board[4][backrank] = null;
            board[5][backrank] = board[7][backrank];
            board[7][backrank] = null;

            writingNotation.write(null, null, Constants.KINGSIDE, false);
        } else {
            board[1][backrank] = board[4][backrank];
            board[4][backrank] = null;
            board[3][backrank] = board[0][backrank];
            board[0][backrank] = null;

            writingNotation.write(null, null, Constants.QUEENSIDE, false);
        }
    }

    public int getEnpassant(boolean color, int index) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        return enpassant[colorIndex][index];
    }

    public void setEnpassant(boolean color, int index, int isEnpassant) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        enpassant[colorIndex][index] = isEnpassant;
    }

    public boolean isCheck(boolean color) {
        Position kingPos = getKingPos(color);

        Bishop diagonal = new Bishop(kingPos, color);
        Rook straight = new Rook(kingPos, color);
        Knight jump = new Knight(kingPos, color);
        Piece close = new Piece(kingPos, color);
        Piece opposition = new Piece(kingPos, color);

        HashSet<Position> dCatchable = diagonal.getPoses().get(1);
        HashSet<Position> sCatchable = straight.getPoses().get(1);
        HashSet<Position> jCatchable = jump.getPoses().get(1);

        HashSet<Position> cCatchable = new HashSet<>();
        int directionY = (color == Constants.WHITE) ? -1 : 1;
        cCatchable.addAll(close.getToPoses(kingPos, -1, directionY, false).get(1));
        cCatchable.addAll(close.getToPoses(kingPos, 1, directionY, false).get(1));

        HashSet<Position> oCatchable = new HashSet<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    oCatchable.addAll(opposition.getToPoses(kingPos, i, j, false).get(1));
                }
            }
        }

        for (Position nextPos : dCatchable) {
            Piece piece = board[nextPos.getX()][nextPos.getY()];
            if ((piece instanceof Bishop) || (piece instanceof Queen)) {
                return true;
            }
        }
        for (Position nextPos : sCatchable) {
            Piece piece = board[nextPos.getX()][nextPos.getY()];
            if ((piece instanceof Rook) || (piece instanceof Queen)) {
                return true;
            }
        }
        for (Position nextPos : jCatchable) {
            Piece piece = board[nextPos.getX()][nextPos.getY()];
            if (piece instanceof Knight) {
                return true;
            }
        }
        for (Position nextPos : cCatchable) {
            Piece piece = board[nextPos.getX()][nextPos.getY()];
            if (piece instanceof Pawn) {
                return true;
            }
        }
        for (Position nextPos : oCatchable) {
            Piece piece = board[nextPos.getX()][nextPos.getY()];
            if (piece instanceof King) {
                return true;
            }
        }
        return false;
    }
}
