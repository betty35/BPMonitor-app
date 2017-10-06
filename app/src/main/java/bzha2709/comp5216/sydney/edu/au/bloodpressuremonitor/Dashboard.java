package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {
    View view;
    Context mCon;
    Button addButton;

    public Dashboard(){    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        mCon=getActivity();
        addButton =(Button)view.findViewById(R.id.addRec_button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(mCon,newMeasure.class);
                startActivity(intent);
            }
        });
        // Initialize a linechart
        LineChart chart = (LineChart) view.findViewById(R.id.dashboard_linechart);
        List<Entry> entries=new ArrayList<Entry>();
        entries.add(new Entry(1, 3));
        entries.add(new Entry(2, 5));
        entries.add(new Entry(3, 2));
        entries.add(new Entry(5, 4));
        entries.add(new Entry(6, 6));
        LineDataSet lds=new LineDataSet(entries,"Test Data");
        lds.setColor(R.color.colorPrimaryDark);
        lds.setValueTextColor(R.color.colorAccent);
        LineData lineData = new LineData(lds);
        chart.setData(lineData);
        chart.invalidate(); // refresh
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
