package sunghyeon.manse.improvedchess;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sunghyeon.manse.improvedchess.pieces.Piece;

public class OnePlayerGameActivity extends AppCompatActivity {

    private int squareSize;

    private final TextView[][] backSquares = new TextView[8][8];
    private final TextView[][] imageSquares = new TextView[8][8];

    DisplayMetrics displayMetrics = new DisplayMetrics();

    private final BoardInfo boardInfo = BoardInfo.getInstance();
    GameManager gameManager;

    private final View.OnClickListener pieceClickedListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int x = view.getLeft() / squareSize;
            int y = view.getTop() / squareSize;
            Position clickedPos = new Position(x, y);

            if (!gameManager.onClick(clickedPos)) {
                gameManager.onClick(clickedPos);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameManager = new GameManager(backSquares, imageSquares, OnePlayerGameActivity.this, displayMetrics);
        setLayout();
        setListeners();
    }

    private void setLayout() {
        FrameLayout.LayoutParams fullParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        fullParams.gravity = Gravity.CENTER;
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setForegroundGravity(Gravity.CENTER);
        setContentView(frameLayout, fullParams);

        ImageView backgroundImage = new ImageView(this);
        backgroundImage.setImageResource(R.drawable.background);
        backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        frameLayout.addView(backgroundImage, fullParams);

        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        int realDeviceWidth = displayMetrics.widthPixels;
        int realDeviceHeight = displayMetrics.heightPixels;
        int shorterSide = Math.min(realDeviceWidth, realDeviceHeight);

        int boardSize = (int) (shorterSide * 0.9);
        boardSize = boardSize - boardSize % 8;
        squareSize = (boardSize / 8);

        FrameLayout.LayoutParams boardParam = new FrameLayout.LayoutParams(boardSize, boardSize);
        boardParam.gravity = Gravity.CENTER;
        GridLayout backBoardLayout = new GridLayout(OnePlayerGameActivity.this);
        GridLayout imageBoardLayout = new GridLayout(OnePlayerGameActivity.this);

        backBoardLayout.setRowCount(8);
        backBoardLayout.setColumnCount(8);
        backBoardLayout.setOrientation(GridLayout.VERTICAL);
        imageBoardLayout.setRowCount(8);
        imageBoardLayout.setColumnCount(8);
        imageBoardLayout.setOrientation(GridLayout.VERTICAL);

        frameLayout.addView(backBoardLayout, boardParam);
        frameLayout.addView(imageBoardLayout, boardParam);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                backSquares[i][j] = new TextView(OnePlayerGameActivity.this);
                imageSquares[i][j] = new TextView(OnePlayerGameActivity.this);

                GridLayout.LayoutParams squareParam = new GridLayout.LayoutParams();
                squareParam.width = squareSize;
                squareParam.height = squareSize;

                if ((i + j) % 2 == 0) {
                    backSquares[i][j].setBackgroundColor(getResources().getColor(R.color.board_white));
                } else {
                    backSquares[i][j].setBackgroundColor(getResources().getColor(R.color.board_black));
                }

                imageSquares[i][j].setClickable(true);

                backBoardLayout.addView(backSquares[i][j], squareParam);
                imageBoardLayout.addView(imageSquares[i][j], squareParam);
            }
        }

        gameManager.initBoard();
    }

    private void setListeners() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int xIndex;
                final int yIndex;

                xIndex = i;
                yIndex = j;

                imageSquares[xIndex][yIndex].setOnClickListener(pieceClickedListener);
            }
        }
    }
}