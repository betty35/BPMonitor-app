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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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
    Button addButton, alarmButton;
    MeasureDao mDAO;
    LineChart lineChart;
    PieChart pieChart;
    PieData pieData;
    LineDataSet sysLDS;
    LineDataSet diaLDS;
    LineDataSet pulseLDS;
    final short good_sys=120;
    final short good_dia=80;
    final short norm_sys=130;
    final short norm_dia=85;
    final short high_sys=140;
    final short high_dia=90;


    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;

    public Dashboard(){    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mCon=getActivity();
        initGreenDao();
        measures=mDAO.queryBuilder().orderAsc(MeasureDao.Properties.Time).list();
        //if(measures.size()>0)
        //Toast.makeText(mCon,"datasize:"+measures.size(),Toast.LENGTH_LONG).show();
    }

    public String bp_categorize(Measure m)
    {
        short sys=m.getSys();
        short dia=m.getDia();
        if(sys>high_sys||dia>high_dia) return "high";
        if(sys>norm_sys||dia>norm_dia) return "normal-high";
        if(sys>good_sys||dia>good_dia) return "normal";
        else return "good";
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
        alarmButton=view.findViewById(R.id.set_alarm_button);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCon,AlarmSetting.class);
                startActivity(intent);
            }
        });
        pieChart=view.findViewById(R.id.dashboard_piechart);
        lineChart = view.findViewById(R.id.dashboard_linechart);
        lineChart.zoom(4.0f,1.1f,new Date().getTime(),80.0f);
        lineChart.animateX(800);
        Description d2=new Description();
        d2.setText("BP & Pulse");
        lineChart.setDescription(d2);
        lineChart.setNoDataText("No data available yet. Please add a new set of blood pressure data.");
        lineChart.setNoDataTextColor(Color.DKGRAY);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        updateLineChartData(lineChart);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelRotationAngle(30);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private SimpleDateFormat mFormat = new SimpleDateFormat("dd-MM HH:mm");
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFormat.format(new Date((long)value));
            }
        });
        xAxis.setAxisMaximum(DateUtil.getEndOfThisWeek().getTime());
        xAxis.setAxisMinimum(DateUtil.getBeginningOfThisWeek().getTime());
        updatePieData();
        initPieChart(pieChart,pieData);
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
        if(null!= lineChart) updateLineChartData(lineChart);
        if(null!=pieChart)
        {
            updatePieData();
            pieChart.clear();
            pieChart.setData(pieData);
            pieChart.invalidate();
        }
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
            if(null!= lineChart) updateLineChartData(lineChart);
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
            sys.add(new Entry(m1.getTime(),m1.getSys()));
            dia.add(new Entry(m1.getTime(),m1.getDia()));
            pulse.add(new Entry(m1.getTime(),m1.getPulse()));
        }

        if(chart.getData()!=null) chart.clear();
        if(measures.size()>0)
        {
            sysLDS = new LineDataSet(sys, "SYS");
            diaLDS = new LineDataSet(dia, "DIA");
            pulseLDS = new LineDataSet(pulse, "PULSE");
            sysLDS.setLineWidth(1.5f);
            sysLDS.setCircleRadius(1.5f);
            sysLDS.setDrawCircleHole(false);
            diaLDS.setLineWidth(1.5f);
            diaLDS.setCircleRadius(1.5f);
            diaLDS.setDrawCircleHole(false);
            pulseLDS.setLineWidth(1.5f);
            pulseLDS.setCircleRadius(1.5f);
            pulseLDS.setDrawCircleHole(false);
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


    private void updatePieData()
    {
        ArrayList<PieEntry> entries =new ArrayList<PieEntry>();
        ArrayList<Integer> g=new ArrayList<Integer>();
        ArrayList<Integer> n=new ArrayList<Integer>();
        ArrayList<Integer> nh=new ArrayList<Integer>();
        ArrayList<Integer> h=new ArrayList<Integer>();
        for(int i=0;i<measures.size();i++)
        {
            Measure m=measures.get(i);
            String cate=bp_categorize(m);
            if(cate.equals("good")) g.add(i);
            else if(cate.equals("normal")) n.add(i);
            else if(cate.equals("normal-high")) nh.add(i);
            else h.add(i);
        }

        entries.add(new PieEntry(g.size()*1f/measures.size(),"healthy"));
        entries.add(new PieEntry(n.size()*1f/measures.size(),"normal"));
        entries.add(new PieEntry(nh.size()*1f/measures.size(),"normal-high"));
        entries.add(new PieEntry(h.size()*1f/measures.size(),"high"));
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS) colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        PieDataSet dataSet =new PieDataSet(entries,"");
        dataSet.setColors(colors);
        pieData=new PieData(dataSet);
    }

    private void initGreenDao()
    {
        helper = new DaoMaster.DevOpenHelper(mCon, "bp-monitor", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mDAO=daoSession.getMeasureDao();
    }

    @Override public void onDestroy()
    {
        daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
        super.onDestroy();
    }


    private void initPieChart(PieChart mChart,PieData pieData) {
        mChart.setUsePercentValues(true);
        Description d=new Description();
        d.setText("Distribution");
        mChart.setDescription(d);
        mChart.setExtraOffsets(-5,-5,-5,-5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setTransparentCircleRadius(61f);
        mChart.setTouchEnabled(false);
        mChart.setDrawEntryLabels(false);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setData(pieData);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }


}
