package andrews.cloakanddagger;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by slothlord on 10/22/17.
 */

public class WindowSetup {

    final int PHASE1 = 1;
    final int PHASE2 = 2;
    final int PHASE3 = 3;
    private int type =  WindowManager.LayoutParams.TYPE_TOAST;
    private int flags = 0;
    private WindowManager manager;

    private WindowSetup(Context context) {

        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public WindowManager updatePhase(Context context, int phase) {

        switch (phase) {
            case PHASE1:
                View overlaying_view = View.inflate(context, R.layout.activity_obscuring_toast, null);
                WindowManager.LayoutParams layoutParams_obs = new WindowManager.LayoutParams(100, 500, type, flags
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT);
                overlaying_view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch (View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

                            return true;
                        }
                        return false;
                    }
                });
                layoutParams_obs.gravity = Gravity.TOP;
                manager.addView(overlaying_view, layoutParams_obs);

                View touch_view = View.inflate(context, R.layout.touch_view, null);
                WindowManager.LayoutParams layoutParams_touch = new WindowManager.LayoutParams(0, 0, type, flags
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        PixelFormat.TRANSLUCENT);
                layoutParams_touch.gravity = Gravity.BOTTOM;
                manager.addView(touch_view, layoutParams_touch);

                return manager;

            case PHASE2:

            case PHASE3:

            default:
        }

        return manager;
    }
}
