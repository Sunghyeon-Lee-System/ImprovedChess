package sunghyeon.manse.improvedchess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();

        Handler handler = new Handler();

        handler.postDelayed(() -> {
            Intent intent = new Intent(LogoActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }

    private void setLayout() {
        FrameLayout.LayoutParams fullParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        FrameLayout frameLayout = new FrameLayout(this);
        setContentView(frameLayout, fullParam);

        ImageView backImage = new ImageView(this);
        backImage.setImageResource(R.color.white);
    }
}