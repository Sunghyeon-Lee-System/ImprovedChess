package sunghyeon.manse.improvedchess.draws;

import java.util.ArrayList;
import java.util.HashMap;

import sunghyeon.manse.improvedchess.BoardInfo;
import sunghyeon.manse.improvedchess.Constants;
import sunghyeon.manse.improvedchess.Position;
import sunghyeon.manse.improvedchess.pieces.Piece;

public class RepetitionDraw extends Draw {
    private char[][][][] hasSteppedPos = new char[2][6][8][8];
    private final HashMap<ArrayList<ArrayList<Piece>>, Integer> repetitionCount = new HashMap<>();

    private int drawType = Constants.NOT_DRAW;

    private static final RepetitionDraw repetitionDraw = new RepetitionDraw();

    private RepetitionDraw() {
        init();
    }

    private void init() {
        //resetInfo();
        addRepetitionCount();

        BoardInfo boardInfo = BoardInfo.getInstance();
        ArrayList<ArrayList<Piece>> board = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ArrayList<Piece> list = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                list.add(boardInfo.getPiece(new Position(i, j)));
            }
            board.add(list);
        }

        ArrayList<ArrayList<Piece>> board2 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ArrayList<Piece> list2 = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                list2.add(boardInfo.getPiece(new Position(i, j)));
            }
            board2.add(list2);
        }
        boolean isEqual = (board.equals(board2));
        android.util.Log.d("ImprovedChess", Boolean.toString(isEqual));
        //addRepetitionCount(board);
    }

    public static RepetitionDraw getInstance() {
        return repetitionDraw;
    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
    }

    public void resetInfo() {
        /*BoardInfo boardInfo = BoardInfo.getInstance();
        ArrayList<ArrayList<Piece>> boardList = new ArrayList<>();

        hasSteppedPos = new char[2][8][8][8];

        for (int i = 0; i < 8; i++) {
            ArrayList<Piece> list = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                Piece piece = boardInfo.getPiece(new Position(i, j));

                if (piece != null) {
                    int colorIndex = (piece.getColor() == Constants.WHITE) ? 0 : 1;
                    int primaryX = piece.getPrimaryPos().getX();
                    int primaryY = piece.getPrimaryPos().getY();
                    int index = (primaryX < 5) ? primaryX : (7 - primaryX);
                    Position pos = piece.getPos();

                    if (primaryY == 0 || primaryY == 7) {
                        hasSteppedPos[colorIndex][index][pos.getX()][pos.getY()]++;
                    }
                }

                list.add(piece);
            }
            boardList.add(list);
        }*/
        repetitionCount.clear();
        addRepetitionCount();
    }

    /*public void setHasSteppedPos(boolean color, int primaryIndex, Position pos) {
        int colorIndex = (color == Constants.WHITE) ? 0 : 1;
        int index = (primaryIndex < 5) ? primaryIndex : (7 - primaryIndex);

        if (hasSteppedPos[colorIndex][primaryIndex][pos.getX()][pos.getY()]!=0) {
            BoardInfo boardInfo = BoardInfo.getInstance();

            ArrayList<ArrayList<Piece>> board = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                ArrayList<Piece> list = new ArrayList<>();
                for (int j = 0; j < 8; j++) {
                    list.add(boardInfo.getPiece(new Position(i, j)));
                }
                board.add(list);
            }

            addRepetitionCount(board);
        } else {
            hasSteppedPos[colorIndex][index][pos.getX()][pos.getY()]++;
        }
    }*/

    public void addRepetitionCount() {
        ArrayList<ArrayList<Piece>> board=getBoardList();
        Integer count = repetitionCount.get(board);

        if (count == null) {
            repetitionCount.put(board, 1);
            android.util.Log.d("ImprovedChess", "1");
        } else {
            repetitionCount.remove(board);
            int newCount = count + 1;
            repetitionCount.put(board, newCount);
            android.util.Log.d("ImprovedChess", Integer.toString(newCount));

            if (newCount == 3) {
                drawType = Constants.NEED_CONFIRM_DRAW;
            } else if (newCount == 5) {
                drawType = Constants.DRAW;
            }
        }
    }

    /*private boolean confirmAll() {
        BoardInfo boardInfo=BoardInfo.getInstance();

        for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
                Piece piece=boardInfo.getPiece(new Position(i, j));
                if(piece!=null) {
                    boolean color=piece.getColor();
                    int primaryX=piece.getPrimaryPos().getX();
                    int primaryY=piece.getPrimaryPos().getY();

                    if(primaryY==0 || primaryY==7) {
                        int colorIndex=(color==Constants.WHITE)?0:1;
                        int index=(primaryX<5)?primaryX:(7-primaryX);

                        if(hasSteppedPos[colorIndex][index][i][j]==1) return false;
                    }
                }
            }
        }

        return true;
    }*/

    public void clearRepetitionCount() {
        repetitionCount.clear();
    }

    @Override
    public int judgeDraw() {
        return drawType;
    }

    private ArrayList<ArrayList<Piece>> getBoardList() {
        BoardInfo boardInfo = BoardInfo.getInstance();

        ArrayList<ArrayList<Piece>> boardList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ArrayList<Piece> list = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                list.add(boardInfo.getPiece(new Position(i, j)));
            }
            boardList.add(list);
        }

        return boardList;
    }
}