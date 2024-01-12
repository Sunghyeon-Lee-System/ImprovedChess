package sunghyeon.manse.improvedchess;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TwoPlayerGameActivity extends AppCompatActivity {

    private boolean myColor;

    private final TextView[][] backSquares = new TextView[8][8];
    private final TextView[][] imageSquares = new TextView[8][8];

    private Button chattingButton;

    private final DisplayMetrics displayMetrics = new DisplayMetrics();

    private int squareSize;

    private final BoardInfo boardInfo = BoardInfo.getInstance();
    GameManager gameManager;


    private final View.OnClickListener pieceClickedListener = view -> {
        int x = view.getLeft() / squareSize;
        int y = view.getTop() / squareSize;
        Position clickedPos = new Position(x, y);

        if (gameManager.getTurnColor() == myColor) {
            if (!gameManager.onClick(clickedPos)) {
                gameManager.onClick(clickedPos);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference();
        DatabaseReference childRef=myRef.child("text");

        childRef.setValue("Hello, world!");

        super.onCreate(savedInstanceState);

        gameManager = new GameManager(backSquares, imageSquares, TwoPlayerGameActivity.this, displayMetrics);
        setLayout();
        setListeners();

        findOpposition();
    }

    private void setLayout() {
        ViewGroup.LayoutParams fullParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout fullLayout = new LinearLayout(TwoPlayerGameActivity.this);
        fullLayout.setGravity(Gravity.CENTER);
        setContentView(fullLayout, fullParams);

        ViewGroup.LayoutParams wrapParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setForegroundGravity(Gravity.CENTER);
        fullLayout.addView(frameLayout, wrapParams);

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
        GridLayout backBoardLayout = new GridLayout(TwoPlayerGameActivity.this);
        GridLayout imageBoardLayout = new GridLayout(TwoPlayerGameActivity.this);

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
                backSquares[i][j] = new TextView(TwoPlayerGameActivity.this);
                imageSquares[i][j] = new TextView(TwoPlayerGameActivity.this);

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

    private void findOpposition() {
        ProgressDialog waitDialog=new ProgressDialog(this);
        waitDialog.setMessage("상대를 찾고 있습니다...");
        waitDialog.show();
    }
}