package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.listener;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.Challenges;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.Dashboard;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.MainActivity;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.Msgs;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.R;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.Records;


/**
 * Created by Bingqing ZHAO on 2017/10/5.
 */

public class MyBottomNaviListener implements BottomNavigationView.OnNavigationItemSelectedListener
{
    MainActivity m;
    Fragment currentFragment;
    Dashboard dashboard;
    Records records;
    Msgs msgs;
    Challenges challenges;
    int containerId;


    public MyBottomNaviListener(MainActivity c,int containerId)
    {
        m=c;
        this.containerId=containerId;
        initiateViews();
        changeFragment(R.id.navi_dashboard);
    }

    public void initiateViews()
    {
        dashboard=new Dashboard();
        records=new Records();
        msgs=new Msgs();
        challenges=new Challenges();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(m,item.getItemId(),Toast.LENGTH_LONG).show();
        changeFragment(item.getItemId());
        return true;
    }

    private void changeFragment(int id)
    {
        FragmentTransaction ft = m.getSupportFragmentManager().beginTransaction();
        if (null != currentFragment) ft.hide(currentFragment);
        Fragment f=m.getSupportFragmentManager().findFragmentById(id);
        if (null == f)
        {
            if(id==R.id.navi_dashboard) f=dashboard;
            else if(id==R.id.navi_challenge)f=challenges;
            else if(id==R.id.navi_record) f=records;
            else f=msgs;
        }
        currentFragment=f;
        if(!f.isAdded())
        {
            ft.add(containerId,f,f.getClass().getName());
        }
        else ft.show(f);
        ft.commit();
    }
}
