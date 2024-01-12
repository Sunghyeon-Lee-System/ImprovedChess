package sunghyeon.manse.improvedchess.dialogmakers;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import sunghyeon.manse.improvedchess.Constants;
import sunghyeon.manse.improvedchess.draws.RepetitionDraw;

public class DrawConfirmDialogMaker extends DialogMaker {
    private Context context;
    private DisplayMetrics displayMetrics;

    private LinearLayout dialogView;
    private Button yesButton;
    private Button noButton;

    private Dialog dialog;

    public DrawConfirmDialogMaker(Context context, DisplayMetrics displayMetrics) {
        this.context = context;
        this.displayMetrics = displayMetrics;

        setDialogView();
    }

    private void setDialogView() {
        int displayX = displayMetrics.widthPixels;
        int displayY = displayMetrics.heightPixels;
        int shorterSide = Math.min(displayX, displayY);

        int dialogSide = (int) (shorterSide * 0.7);

        dialogView = new LinearLayout(context);
        dialogView.setLayoutParams(new LinearLayout.LayoutParams(dialogSide, dialogSide));
        dialogView.setOrientation(LinearLayout.VERTICAL);
        dialogView.setGravity(Gravity.CENTER);
        dialogView.setPadding(20, 20, 20, 20);

        LinearLayout.LayoutParams defaultParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView title = new TextView(context);
        title.setGravity(Gravity.CENTER);
        float textSize = (float) (dialogSide * 0.1);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        title.setText("무승부로\n하시겠습니까?");
        dialogView.addView(title, defaultParam);

        float buttonSize = textSize / 2;

        LinearLayout buttonLayout = new LinearLayout(context);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        dialogView.addView(buttonLayout);

        LinearLayout.LayoutParams wrapParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        yesButton = new Button(context);
        yesButton.setTextSize(buttonSize);
        yesButton.setText("예");
        buttonLayout.addView(yesButton, wrapParam);

        noButton = new Button(context);
        noButton.setTextSize(buttonSize);
        noButton.setText("아니오");
        buttonLayout.addView(noButton, wrapParam);
    }

    public void setRotation(boolean color) {
        float rotation = (color == Constants.WHITE) ? 0f : 180f;
        dialogView.setRotation(rotation);
    }

    public void makeDialog(Thread thread) {
        if (dialog == null) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogView);
            dialog.setCancelable(false);

            RepetitionDraw repetitionDraw = RepetitionDraw.getInstance();

            yesButton.setOnClickListener(view -> {
                android.util.Log.d("ImprovedChess", "yesButton");
                thread.start();
                dialog.dismiss();
            });
            noButton.setOnClickListener(view -> {
                repetitionDraw.setDrawType(Constants.NOT_DRAW);
                dialog.dismiss();
            });
        }

        dialog.show();
    }
}
