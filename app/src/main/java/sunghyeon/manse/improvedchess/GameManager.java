package sunghyeon.manse.improvedchess;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import sunghyeon.manse.improvedchess.dialogmakers.DrawConfirmDialogMaker;
import sunghyeon.manse.improvedchess.dialogmakers.MessageDialogMaker;
import sunghyeon.manse.improvedchess.dialogmakers.PromotionDialogMaker;
import sunghyeon.manse.improvedchess.draws.InsufficientDraw;
import sunghyeon.manse.improvedchess.draws.RepetitionDraw;
import sunghyeon.manse.improvedchess.draws.TooManyMoveDraw;
import sunghyeon.manse.improvedchess.pieces.Bishop;
import sunghyeon.manse.improvedchess.pieces.King;
import sunghyeon.manse.improvedchess.pieces.Knight;
import sunghyeon.manse.improvedchess.pieces.Pawn;
import sunghyeon.manse.improvedchess.pieces.Piece;
import sunghyeon.manse.improvedchess.pieces.Queen;
import sunghyeon.manse.improvedchess.pieces.Rook;

public class GameManager {
    TextView[][] backSquares;
    TextView[][] imageSquares;
    Context context;
    DisplayMetrics displayMetrics;

    BoardInfo boardInfo;

    PromotionDialogMaker promotionDialogMaker;
    MessageDialogMaker messageDialogMaker;
    DrawConfirmDialogMaker drawConfirmDialogMaker;

    InsufficientDraw insufficientDraw;
    RepetitionDraw repetitionDraw;
    TooManyMoveDraw tooManyMoveDraw;

    private boolean turnColor;
    private boolean myColor;

    private boolean pieceClicked;

    private boolean queenSideCastling = false;
    private boolean kingSideCastling = false;

    private boolean isEnpassant = false;

    private boolean isCheckMate = false;

    private Position originalPos;

    private final HashSet<Position> movablePoses = new HashSet<>();
    private final HashSet<Position> catchablePoses = new HashSet<>();
    private final Position[] castlingPoses = new Position[2];
    private Position enpassantCatchable;
    private Position enpassantPos;

    public GameManager(TextView[][] backSquares, TextView[][] imageSquares, Context context, DisplayMetrics displayMetrics) {
        this.backSquares = backSquares;
        this.imageSquares = imageSquares;
        this.context = context;
        this.displayMetrics = displayMetrics;

        init();
    }

    public void init() {
        turnColor = Constants.WHITE;
        pieceClicked = false;

        boardInfo = BoardInfo.getInstance();

        promotionDialogMaker = new PromotionDialogMaker(context, displayMetrics);
        messageDialogMaker = new MessageDialogMaker(context, displayMetrics);
        drawConfirmDialogMaker = new DrawConfirmDialogMaker(context, displayMetrics);

        insufficientDraw = InsufficientDraw.getInstance();
        repetitionDraw = RepetitionDraw.getInstance();
        tooManyMoveDraw = TooManyMoveDraw.getInstance();
    }

    public void initBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = boardInfo.getPiece(new Position(i, j));
                if (piece != null) {
                    imageSquares[i][j].setBackground(piece.getImage(context));
                }
            }
        }
    }

    public void setTurnColor(boolean color) {
        turnColor = color;
    }

    public boolean getTurnColor() {
        return turnColor;
    }

    public void setMyColor(boolean color) {
        myColor=color;
    }

    public boolean onClick(Position pos) {
        Piece piece = boardInfo.getPiece(pos);

        int posX = pos.getX();
        int posY = pos.getY();


        if (!pieceClicked) {
            if (boardInfo.getPiece(pos) != null) {
                if (piece.getColor() == turnColor) {
                    pieceClicked = true;

                    originalPos = pos;

                    setPoses(piece, pos, turnColor, true);

                    for (Position nextPos : movablePoses) {
                        backSquares[nextPos.getX()][nextPos.getY()].setBackgroundColor(context.getResources().getColor(R.color.board_movable));
                    }
                    for (Position nextPos : catchablePoses) {
                        backSquares[nextPos.getX()][nextPos.getY()].setBackgroundColor(context.getResources().getColor(R.color.board_catchable));
                    }

                    backSquares[pos.getX()][pos.getY()].setBackgroundColor(context.getResources().getColor(R.color.board_movable));
                }
            }
        } else {
            pieceClicked = false;

            int originalX = originalPos.getX();
            int originalY = originalPos.getY();

            HashSet<Position> poses = new HashSet<>();
            poses.addAll(movablePoses);
            poses.addAll(catchablePoses);

            boolean isEqual = false;

            for (Position nextPos : poses) {
                if (pos.equals(nextPos)) {
                    confirmCastling(pos, turnColor);
                    confirmEnpassant(pos);

                    Piece originalPiece = boardInfo.getPiece(originalPos);
                    boardInfo.setPiece(null, originalPos, true);
                    boardInfo.setPiece(originalPiece, pos, true);
                    imageSquares[posX][posY].setBackground(originalPiece.getImage(context));
                    imageSquares[originalX][originalY].setBackground(null);

                    confirmPromotion(pos, turnColor);
                    confirmDraws(turnColor);
                    confirmMates(!turnColor);

                    turnColor = !turnColor;

                    isEqual = true;
                }

                if (pos.equals(originalPos)) {
                    isEqual = true;
                } else {
                    if (isEqual) {
                        if (isEnpassant) {
                            isEnpassant = false;
                            boardInfo.setEnpassant(turnColor, enpassantPos.getX(), Constants.NO_ENPASSANT);
                        }
                    }
                }

                int nextPosX = nextPos.getX();
                int nextPosY = nextPos.getY();
                if ((nextPosX + nextPosY) % 2 == 0) {
                    backSquares[nextPosX][nextPosY].setBackgroundColor(context.getResources().getColor(R.color.board_white));
                } else {
                    backSquares[nextPosX][nextPosY].setBackgroundColor(context.getResources().getColor(R.color.board_black));
                }
            }

            if (poses.isEmpty()) {
                isEqual = true;
            }

            if ((originalX + originalY) % 2 == 0) {
                backSquares[originalX][originalY].setBackgroundColor(context.getResources().getColor(R.color.board_white));
            } else {
                backSquares[originalX][originalY].setBackgroundColor(context.getResources().getColor(R.color.board_black));
            }

            return isEqual;
        }

        return true;
    }

    private void setPoses(Piece piece, Position pos, boolean color, boolean setFlag) {
        int backrank = (color == Constants.WHITE) ? 7 : 0;

        movablePoses.clear();
        catchablePoses.clear();

        ArrayList<HashSet<Position>> poses = piece.getPoses();
        HashSet<Position> tmpMovablePoses = poses.get(0);
        HashSet<Position> tmpCatchablePoses = poses.get(1);

        for (Position nextPos : tmpMovablePoses) {
            if (confirmMovable(pos, nextPos, color, false)) {
                movablePoses.add(nextPos);
            }
        }

        for (Position nextPos : tmpCatchablePoses) {
            if (confirmMovable(pos, nextPos, color, false)) {
                catchablePoses.add(nextPos);
            }
        }

        if (piece instanceof King) {
            boolean[] isPossible = ((King) piece).isPossibleCastling();
            if (isPossible[0]) {
                movablePoses.add(new Position(6, backrank));
                if (setFlag) {
                    castlingPoses[0] = new Position(6, backrank);
                    kingSideCastling = true;
                    android.util.Log.d("ImprovedChess", "king");
                }
            }
            if (isPossible[1]) {
                movablePoses.add(new Position(2, backrank));
                if (setFlag) {
                    castlingPoses[1] = new Position(2, backrank);
                    queenSideCastling = true;
                    android.util.Log.d("ImprovedChess", "queen");
                }
            }
        }

        if (piece instanceof Pawn) {
            int enpassantType = boardInfo.getEnpassant(color, pos.getX());
            Position tmpEnpassantCatchable = ((Pawn) piece).enpassantRelatedPosition(enpassantType, true);
            if (tmpEnpassantCatchable != null) {
                enpassantCatchable = tmpEnpassantCatchable;
                isEnpassant = true;
                enpassantPos = pos;
                if (confirmMovable(pos, enpassantCatchable, color, true)) {
                    catchablePoses.add(enpassantCatchable);
                }
            }
        }
    }

    private void confirmCastling(Position pos, boolean color) {
        int backRank = (color == Constants.WHITE) ? 7 : 0;

        if (queenSideCastling | kingSideCastling) {
            Position fromPos = null;
            Position toPos = null;
            Piece fromPiece = null;

            boolean actualCastling = false;

            if (kingSideCastling) {
                fromPos = new Position(7, backRank);
                toPos = new Position(5, backRank);
                fromPiece = boardInfo.getPiece(fromPos);

                if (pos.equals(castlingPoses[0])) actualCastling = true;
            }
            if (queenSideCastling) {
                fromPos = new Position(0, backRank);
                toPos = new Position(3, backRank);
                fromPiece = boardInfo.getPiece(fromPos);

                if (pos.equals(castlingPoses[1])) actualCastling = true;
            }

            if (actualCastling) {
                boardInfo.setPiece(fromPiece, toPos, true);
                boardInfo.setPiece(null, fromPos, true);
                imageSquares[toPos.getX()][toPos.getY()].setBackground(fromPiece.getImage(context));
                imageSquares[fromPos.getX()][fromPos.getY()].setBackground(null);
            }

            kingSideCastling = false;
            queenSideCastling = false;
        }
    }

    private void confirmEnpassant(Position pos) {
        if (isEnpassant) {
            if (pos.equals(enpassantCatchable)) {
                int enpassantType = boardInfo.getEnpassant(turnColor, enpassantPos.getX());
                Position toBeCaughtPos = ((Pawn) boardInfo.getPiece(enpassantPos)).enpassantRelatedPosition(enpassantType, false);
                boardInfo.setPiece(null, toBeCaughtPos, true);
                imageSquares[toBeCaughtPos.getX()][toBeCaughtPos.getY()].setBackground(null);
            }
        }
    }

    private void confirmPromotion(Position pos, boolean color) {
        HashSet<Position> promotionPos = boardInfo.getPromotionPos();
        if (!promotionPos.isEmpty()) {
            for (Position nextPos : promotionPos) {
                if (pos.equals(nextPos)) {
                    promotion(pos, color);
                    break;
                }
            }
            boardInfo.getPromotionPos().clear();
        }
    }

    private void promotion(Position pos, boolean color) {
        promotionDialogMaker.setPromotionDialogLayout();
        promotionDialogMaker.setRotation(color);
        promotionDialogMaker.makeDialog();

        ImageButton[] imageButtons = promotionDialogMaker.getImageButtons();
        for (int i = 0; i < imageButtons.length; i++) {
            final int index = i;
            imageButtons[index].setOnClickListener(view -> {
                insufficientDraw.pawnCountAdd(color, -1);

                switch (index) {
                    case 0:
                        boardInfo.setPiece(new Queen(pos, color), pos, false);
                        insufficientDraw.queenCountAdd(color, 1);
                        break;
                    case 1:
                        boardInfo.setPiece(new Rook(pos, color), pos, false);
                        insufficientDraw.rookCountAdd(color, 1);
                        break;
                    case 2:
                        boardInfo.setPiece(new Bishop(pos, color), pos, false);
                        boolean pieceColor = (pos.getX() % 2 == 0) ? Constants.WHITE : Constants.BLACK;
                        insufficientDraw.bishopCountAdd(color, pieceColor, 1);
                        break;
                    case 3:
                        boardInfo.setPiece(new Knight(pos, color), pos, false);
                        insufficientDraw.knightCountAdd(color, 1);
                        break;
                }
                imageSquares[pos.getX()][pos.getY()].setBackground(boardInfo.getPiece(pos).getImage(context));

                confirmMates(!color);
                promotionDialogMaker.getDialog().dismiss();
            });
        }
    }

    private boolean confirmMovable(Position fromPos, Position toPos, boolean color, boolean enpassantPiece) {
        Piece fromPiece = boardInfo.getPiece(fromPos);
        Piece toPiece = boardInfo.getPiece(toPos);

        boolean enpassant = false;
        Position toBeCaughtPos = null;
        Pawn toBeCaughtPiece = null;

        if (enpassantPiece) {
            int enpassantType = boardInfo.getEnpassant(color, enpassantPos.getX());
            toBeCaughtPiece = (Pawn) boardInfo.getPiece(enpassantPos);

            toBeCaughtPos = toBeCaughtPiece.enpassantRelatedPosition(enpassantType, false);
            boardInfo.setPiece(null, toBeCaughtPos, false);
            enpassant = true;
        }

        boardInfo.setPiece(null, fromPos, false);
        boardInfo.setPiece(fromPiece, toPos, false);

        boolean movable = boardInfo.isCheck(color);

        boardInfo.setPiece(fromPiece, fromPos, false);
        boardInfo.setPiece(toPiece, toPos, false);

        if (enpassant) {
            boardInfo.setPiece(toBeCaughtPiece, toBeCaughtPos, false);
        }

        return !movable;
    }

    private void confirmMates(boolean color) {
        boolean isCheck = boardInfo.isCheck(color);

        Position kingPos = boardInfo.getKingPos(color);
        Piece king = boardInfo.getPiece(kingPos);

        setPoses(king, kingPos, color, false);
        HashSet<Position> kingPoses = new HashSet<>();
        kingPoses.addAll(movablePoses);
        kingPoses.addAll(catchablePoses);

        if (kingPoses.isEmpty()) {
            if (isCheck) {
                confirmCheckMate(color);
            } else {
                confirmStaleMate(color);
            }
        }
    }

    private void confirmCheckMate(boolean color) {
        isCheckMate = true;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position pos = new Position(i, j);
                Piece piece = boardInfo.getPiece(pos);

                if (piece != null) {
                    if (piece.getColor() == color) {
                        setPoses(piece, pos, color, false);

                        if (!(movablePoses.isEmpty() && catchablePoses.isEmpty())) {
                            isCheckMate = false;
                        }
                    }
                }
            }
        }

        if (isCheckMate) {
            messageDialogMaker.makeDialog("체크메이트", context.getResources().getColor(R.color.background_checkmate));
        }
    }

    private void confirmStaleMate(boolean color) {
        boolean isStaleMate = true;

        checkEmpty:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position pos=new Position(i, j);
                Piece piece = boardInfo.getPiece(pos);
                if (piece != null) {
                    if (piece.getColor() == color) {
                        setPoses(piece, pos, color, false);
                        HashSet<Position> piecePoses = new HashSet<>();
                        piecePoses.addAll(movablePoses);
                        piecePoses.addAll(catchablePoses);

                        if (!piecePoses.isEmpty()) {
                            isStaleMate = false;
                            break checkEmpty;
                        }
                    }
                }
            }
        }

        if (isStaleMate) {
            messageDialogMaker.makeDialog("스테일메이트", context.getResources().getColor(R.color.background_stalemate));
        }
    }

    private void confirmDraws(boolean color) {
        if (insufficientDraw.judgeDraw() == Constants.DRAW) {
            messageDialogMaker.makeDialog("기물 부족\n무승부", context.getResources().getColor(R.color.background_stalemate));
        }

        int repetitionDrawType = repetitionDraw.judgeDraw();
        if (repetitionDrawType == Constants.NEED_CONFIRM_DRAW) {
            Handler repetitionDrawHandler = new Handler((Handler.Callback) msg -> {
                if (msg.what == 0) {
                    messageDialogMaker.makeDialog("3회 동형 반복\n무승부", context.getResources().getColor(R.color.background_stalemate));
                }
                return true;
            });

            Thread repetitionDrawThread = new Thread(() -> {
                repetitionDrawHandler.sendEmptyMessage(0);
            });

            drawConfirmDialogMaker.setRotation(color);
            drawConfirmDialogMaker.makeDialog(repetitionDrawThread);
        } else if (repetitionDrawType == Constants.DRAW) {
            messageDialogMaker.makeDialog("5회 동형 반복\n무승부", context.getResources().getColor(R.color.background_stalemate));
        }

        tooManyMoveDraw.addTurnCount();
        int tooManyMoveDrawType = tooManyMoveDraw.judgeDraw();
        if (tooManyMoveDrawType == Constants.NEED_CONFIRM_DRAW) {
            Handler tooManyMoveDrawHandler = new Handler((Handler.Callback) msg -> {
                if (msg.what == 0) {
                    messageDialogMaker.makeDialog("50수 무승부", context.getResources().getColor(R.color.background_stalemate));
                }
                return true;
            });

            Thread tooManyMoveDrawThread = new Thread(() -> {
                tooManyMoveDrawHandler.sendEmptyMessage(0);
            });

            drawConfirmDialogMaker.setRotation(color);
            drawConfirmDialogMaker.makeDialog(tooManyMoveDrawThread);
        } else if (tooManyMoveDrawType == Constants.DRAW) {
            if (!isCheckMate) {
                messageDialogMaker.makeDialog("75수 무승부", context.getResources().getColor(R.color.background_stalemate));
            }
        }
    }
}
