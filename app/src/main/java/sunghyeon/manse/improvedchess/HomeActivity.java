package sunghyeon.manse.improvedchess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {

    Button bOnePlayerGame;
    Button bTwoPlayerGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();
        setListeners();
    }

    private void setLayout() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        LinearLayout.LayoutParams fullParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout, fullParam);

        int buttonWidth = deviceWidth / 2;
        int buttonTextSize = (int) (deviceHeight * 0.05);
        LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(buttonWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        bOnePlayerGame = new Button(this);
        bOnePlayerGame.setText("오프라인\n게임");
        bOnePlayerGame.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);
        linearLayout.addView(bOnePlayerGame, buttonParam);

        bTwoPlayerGame = new Button(this);
        bTwoPlayerGame.setText("온라인\n게임");
        bTwoPlayerGame.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);
        buttonParam.setMargins(0, (int) (deviceHeight / 4), 0, 0);
        linearLayout.addView(bTwoPlayerGame, buttonParam);
    }

    private void setListeners() {
        bOnePlayerGame.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, OnePlayerGameActivity.class);
            startActivity(intent);
        });

        bTwoPlayerGame.setOnClickListener(view -> {
            Intent intent=new Intent(HomeActivity.this, TwoPlayerGameActivity.class);
            startActivity(intent);
        });
    }
}