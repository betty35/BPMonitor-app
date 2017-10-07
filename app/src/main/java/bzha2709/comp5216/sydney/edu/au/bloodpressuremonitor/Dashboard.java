package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.Measure;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.MeasureDao;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.DateUtil;

public class Dashboard extends Fragment {
    View view;
    List<Measure> measures;
    Context mCon;
    Button addButton;
    MeasureDao mDAO;
    LineChart chart;
    LineDataSet sysLDS;
    LineDataSet diaLDS;
    LineDataSet pulseLDS;

    public Dashboard(){    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mCon=getActivity();
        initGreenDao();
        measures=mDAO.queryBuilder().orderAsc(MeasureDao.Properties.Time).list();
        if(measures.size()>0)
        Toast.makeText(mCon,"datasize:"+measures.size(),Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        addButton = view.findViewById(R.id.addRec_button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(mCon,newMeasure.class);
                startActivity(intent);
            }
        });


        chart = view.findViewById(R.id.dashboard_linechart);
        chart.setNoDataText("No data available yet. Please add a new set of blood pressure data.");
        chart.setNoDataTextColor(Color.DKGRAY);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        updateLineChartData(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelRotationAngle(90);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFormat.format(new Date((long)value));
            }
        });
        xAxis.setAxisMaximum(DateUtil.getTodayEvening().getTime());
        xAxis.setAxisMinimum(DateUtil.getThisMorning().getTime());
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        measures.clear();
        measures.addAll(mDAO.queryBuilder().orderAsc(MeasureDao.Properties.Time).list());
        if(null!=chart) updateLineChartData(chart);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden)
        {
            measures.clear();
        }
        else
        {//update data
            measures.clear();
            measures.addAll(mDAO.queryBuilder().orderAsc(MeasureDao.Properties.Time).list());
            if(null!=chart) updateLineChartData(chart);
        }
    }

    private void updateLineChartData(LineChart chart)
    {
        List<Entry> sys=new ArrayList<Entry>();
        List<Entry> dia=new ArrayList<Entry>();
        List<Entry> pulse=new ArrayList<Entry>();
        for(int i=0;i<measures.size();i++)
        {
            Measure m1=measures.get(i);
            sys.add(new Entry(m1.getTime().getTime(),m1.getSys()));
            dia.add(new Entry(m1.getTime().getTime(),m1.getDia()));
            pulse.add(new Entry(m1.getTime().getTime(),m1.getPulse()));
        }

        if(chart.getData()!=null) chart.clear();
        if(measures.size()>0)
        {
            sysLDS = new LineDataSet(sys, "SYS");
            diaLDS = new LineDataSet(dia, "DIA");
            pulseLDS = new LineDataSet(pulse, "PULSE");
            sysLDS.setLineWidth(1f);
            sysLDS.setCircleRadius(3f);
            sysLDS.setDrawCircleHole(false);
            sysLDS.setColor(ContextCompat.getColor(mCon, R.color.color_sys));
            diaLDS.setColor(ContextCompat.getColor(mCon, R.color.color_dia));
            pulseLDS.setColor(ContextCompat.getColor(mCon, R.color.color_pulse));
            ArrayList<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
            dataSets.add(sysLDS);
            dataSets.add(diaLDS);
            dataSets.add(pulseLDS);
            LineData data=new LineData(dataSets);
            chart.setData(data);
            chart.invalidate();
        }

    }

    private void initGreenDao()
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mCon, "bp-monitor", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mDAO=daoSession.getMeasureDao();
    }

}
