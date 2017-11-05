package andrews.cloakanddagger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        /**Checks whether the BIND_ACCESSIBILITY_SERVICE permission is granted. If not call the startToast function from the WindowSetup class.**/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BIND_ACCESSIBILITY_SERVICE) != PackageManager.PERMISSION_GRANTED) {

            /**Opens the accessibility settings screen**/
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }

            WindowSetup setup = new WindowSetup(this);
            //setup.startToast(this);
        } else {
            Toast.makeText(this, "Permission Granted Bind Service", Toast.LENGTH_LONG).show();
        }

    }

}
