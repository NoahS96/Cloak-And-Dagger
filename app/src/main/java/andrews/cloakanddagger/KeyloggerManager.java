package andrews.cloakanddagger;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Andrew Schaffer on 11/1/2017.
 */

public class KeyloggerManager {

    private WindowManager manager;
    private volatile Context parent;
    private int width, height;

    public KeyloggerManager(Context cxt, WindowManager manager, int width, int height) {
        parent = cxt;
        this.manager = manager;
        this.width = width;
        this.height = height;
    }

    private void makeOverlay() {


    }
}
