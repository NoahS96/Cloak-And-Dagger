package andrews.cloakanddagger;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by slothlord on 10/22/17.
 */

public class WindowSetup {

    public final int PHASE1 = 1;
    public final int PHASE2 = 2;
    public final int PHASE3 = 3;
    private int type =  WindowManager.LayoutParams.TYPE_TOAST;
    private int flags = 0;
    private static WindowManager manager;
    private int width;
    private int height;
    public volatile boolean result = false;
    volatile Context parent = null;

    public WindowSetup(Context context) {
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    public WindowManager getManager() {
        return manager;
    }

    public boolean updatePhase(Context context, int phase) {

        View overlaying_view1=null, overlaying_view2=null, overlaying_view3=null, touch_view;
        result = false;
        parent = context;

        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //This switch determines the decorative view at the top of the view
        switch (phase) {
            case PHASE1:
                overlaying_view1 = View.inflate(context, R.layout.activity_obscuring_toast, null);
                WindowManager.LayoutParams layoutParams_obs1 = new WindowManager.LayoutParams(width, 482, type, flags
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        PixelFormat.TRANSLUCENT);
                layoutParams_obs1.gravity = Gravity.TOP;
                manager.addView(overlaying_view1, layoutParams_obs1);
                break;

            case PHASE2:

                if (overlaying_view1 != null) {
                    manager.removeView(overlaying_view1);
                }

                overlaying_view2 = View.inflate(context, R.layout.obscuring_toast2, null);
                WindowManager.LayoutParams layoutParams_obs2 = new WindowManager.LayoutParams(width, 482, type, flags
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        PixelFormat.TRANSLUCENT);
                layoutParams_obs2.gravity = Gravity.TOP;
                manager.addView(overlaying_view2, layoutParams_obs2);
                break;

            case PHASE3:

                break;

            default:
                //this is default
        }

        //The touch view is responsible for listening for a touch which returns true when detected
        touch_view = View.inflate(context, R.layout.touch_view, null);
        WindowManager.LayoutParams layoutParams_touch = new WindowManager.LayoutParams(width, height - height/3 + 53, type, flags
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        touch_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!Touch Detected!!!!!!!!!!!!!!!!!!");
                    result = true;
                    return true;
                }
                return false;
            }
        });
        layoutParams_touch.gravity = Gravity.BOTTOM;
        manager.addView(touch_view, layoutParams_touch);

        return result;
    }
}
