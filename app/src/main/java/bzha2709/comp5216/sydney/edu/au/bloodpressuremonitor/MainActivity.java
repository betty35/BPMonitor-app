package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.listener.MyBottomNaviListener;

public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        mOnNavigationItemSelectedListener = new MyBottomNaviListener(this,R.id.content);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
