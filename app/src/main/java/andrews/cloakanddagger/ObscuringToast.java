package andrews.cloakanddagger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class ObscuringToast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obscuring_toast);

        Button button = (Button) findViewById(R.id.fake_button);
        button.setClickable(false);
    }
}
