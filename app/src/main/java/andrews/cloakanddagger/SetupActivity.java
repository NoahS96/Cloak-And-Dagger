package andrews.cloakanddagger;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class SetupActivity extends AppCompatActivity {

    private final int REQUEST_BIND_ACCESSIBILITY_SERVICE_PERMISSION = 100;
    private final int REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent myIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(myIntent);
        }

        WindowSetup setup = new WindowSetup(this);
        setup.updatePhase(this, setup.PHASE1);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.BIND_ACCESSIBILITY_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No permission Bind Service", Toast.LENGTH_LONG).show();
            //requestPermissions(new String[]{Manifest.permission.BIND_ACCESSIBILITY_SERVICE}, REQUEST_BIND_ACCESSIBILITY_SERVICE_PERMISSION);

            System.out.println("Should pop dialog");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BIND_ACCESSIBILITY_SERVICE}, REQUEST_BIND_ACCESSIBILITY_SERVICE_PERMISSION);
        } else {
            System.out.println("Permission Granted");
            Toast.makeText(this, "Permission Granted Bind Service", Toast.LENGTH_LONG).show();
        }*/

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No permission System Alert", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {

            } else {

                ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION);
            }
        } else {
            Toast.makeText(this, "Permission Granted System Alert", Toast.LENGTH_LONG).show();
        }*/

    }

}
