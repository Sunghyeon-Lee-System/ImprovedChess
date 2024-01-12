package sunghyeon.manse.improvedchess.dialogmakers;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sunghyeon.manse.improvedchess.R;

public class MessageDialogMaker {
    private final Context context;
    private final DisplayMetrics displayMetrics;

    private final LinearLayout[] dialogViews = new LinearLayout[2];
    private final TextView[] titles = new TextView[2];
    private final Button[] buttons = new Button[2];

    public MessageDialogMaker(Context context, DisplayMetrics displayMetrics) {
        this.context = context;
        this.displayMetrics = displayMetrics;

        setDialogLayout();
    }

    public void setDialogLayout() {
        int displayX = displayMetrics.widthPixels;
        int displayY = displayMetrics.heightPixels;
        int shorterSide = Math.min(displayX, displayY);
        int longerSide = Math.max(displayX, displayY);

        int imageSide = (int) (shorterSide * 0.7);

        float[] rotations = {180f, 0f};

        for (int i = 0; i < rotations.length; i++) {

            dialogViews[i] = new LinearLayout(context);
            dialogViews[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            dialogViews[i].setOrientation(LinearLayout.VERTICAL);
            dialogViews[i].setGravity(Gravity.CENTER);
            dialogViews[i].setPadding(20, 20, 20, 20);
            dialogViews[i].setRotation(rotations[i]);

            LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            titles[i] = new TextView(context);
            titles[i].setLayoutParams(defaultParams);
            titles[i].setGravity(Gravity.CENTER);
            float titleSize = (float) (shorterSide * (0.1));
            titles[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            titles[i].setTextColor(context.getResources().getColor(R.color.black));
            dialogViews[i].addView(titles[i]);

            int[] imageIds = {R.drawable.sagiri0, R.drawable.sagiri1, R.drawable.sagiri1, R.drawable.sagiri2, R.drawable.sagiri3};

            int imageNum = (int) (Math.random() * imageIds.length);

            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(imageSide, imageSide));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imageIds[imageNum]);
            dialogViews[i].addView(imageView);

            buttons[i] = new Button(context);
            buttons[i].setLayoutParams(defaultParams);
            float buttonSize = titleSize / 2;
            buttons[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonSize);
            buttons[i].setText("확인");
            dialogViews[i].addView(buttons[i]);
        }
    }

    public void makeDialog(String message, int backgroundColor) {
        for (TextView title : titles) {
            if (title != null) {
                title.setText(message);
            }
        }

        int[] gravities = {Gravity.TOP, Gravity.BOTTOM};

        for (int i = 0; i < dialogViews.length; i++) {
            dialogViews[i].setBackgroundColor(backgroundColor);

            Dialog messageDialog = new Dialog(context);
            messageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            messageDialog.setContentView(dialogViews[i]);
            android.util.Log.d("ImprovedChess", "image is set");
            messageDialog.setCancelable(false);

            messageDialog.getWindow().setGravity(gravities[i]);
            messageDialog.show();

            buttons[i].setOnClickListener(view -> messageDialog.dismiss());
        }
    }
}
