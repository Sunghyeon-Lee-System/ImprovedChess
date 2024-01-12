package sunghyeon.manse.improvedchess.dialogmakers;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sunghyeon.manse.improvedchess.Constants;
import sunghyeon.manse.improvedchess.R;

public class PromotionDialogMaker {

    private final Context context;
    private final DisplayMetrics displayMetrics;
    private LinearLayout dialogLayout;
    private final ImageButton[] imageButtons = new ImageButton[4];
    private Dialog dialog;

    public PromotionDialogMaker(Context context, DisplayMetrics displayMetrics) {
        this.context = context;
        this.displayMetrics = displayMetrics;

        setPromotionDialogLayout();
    }

    public ImageButton[] getImageButtons() {
        return imageButtons;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setPromotionDialogLayout() {
        int displayX = displayMetrics.widthPixels;
        int displayY = displayMetrics.heightPixels;
        int shorterSide = Math.min(displayX, displayY);

        int dialogX = (int) (shorterSide * 0.8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dialogX, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogLayout = new LinearLayout(context);
        dialogLayout.setLayoutParams(params);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(context);
        title.setGravity(Gravity.CENTER);
        title.setText("승진");
        float fontSize = (float) (dialogX * (0.15));
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        dialogLayout.addView(title);

        LinearLayout.LayoutParams buttonsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout buttonsLayout = new LinearLayout(context);
        dialogLayout.addView(buttonsLayout, buttonsLayoutParams);

        int buttonX = (int) (dialogX / 4);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(buttonX, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        for (int i = 0; i < imageButtons.length; i++) {
            imageButtons[i] = new ImageButton(context);
            imageButtons[i].setScaleType(ImageView.ScaleType.CENTER);
            buttonsLayout.addView(imageButtons[i], buttonParams);
        }
    }

    public void setRotation(boolean color) {
        float rotation = (color == Constants.WHITE) ? 0f : 180f;

        if (dialogLayout != null) {
            dialogLayout.setRotation(rotation);
        }

        int[] drawables;
        if (color == Constants.WHITE) {
            drawables = new int[]{R.drawable.wqueen, R.drawable.wrook, R.drawable.wbishop, R.drawable.wknight};
        } else {
            drawables = new int[]{R.drawable.bqueen, R.drawable.brook, R.drawable.bbishop, R.drawable.bknight};
        }

        for (int i = 0; i < imageButtons.length; i++) {
            if (imageButtons[i] != null) {
                imageButtons[i].setImageResource(drawables[i]);
                imageButtons[i].setRotation(rotation);
            }
        }
    }

    public void makeDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogLayout);
        dialog.setCancelable(false);
        dialog.show();
    }
}
