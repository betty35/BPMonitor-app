package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;


import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.Measure;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.MeasureDao;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.DateUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {

    TextView from,to;
    Button search;
    RecyclerView myList;
    List<Measure> measureList;
    LinearLayoutManager llm;
    CommonAdapter<Measure> commonAdapter;
    Date fromD,toD;
    SimpleDateFormat myFmt;

    //db
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;
    MeasureDao mDAO;

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_msgs, container, false);
        fromD=DateUtil.getBeginningOfThisWeek();
        toD=DateUtil.getEndOfThisWeek();
        from= view.findViewById(R.id.his_list_from);
        to= view.findViewById(R.id.his_list_to);
        search= view.findViewById(R.id.his_list_search);
        myFmt=new SimpleDateFormat("yyyy-MM-dd");
        from.setText(myFmt.format(fromD));
        to.setText(myFmt.format(toD));
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPick(from);
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPick(to);
            }
        });

        myList= view.findViewById(R.id.his_list_recycle);
        llm=new LinearLayoutManager(getActivity());
        myList.setLayoutManager(llm);
        myList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        initGreenDao();
        measureList=mDAO.queryBuilder().where(MeasureDao.Properties.Time.between(fromD.getTime(),
                toD.getTime())).list();
        commonAdapter=new CommonAdapter<Measure>(getActivity(),R.layout.measure_item,measureList) {
            @Override
            protected void convert(ViewHolder holder, Measure measure, int position) {
                holder.setText(R.id.his_item_dia,measure.getDia()+"");
                holder.setText(R.id.his_item_sys,measure.getSys()+"");
                holder.setText(R.id.his_item_plu,measure.getPulse()+"");
                holder.setText(R.id.his_item_pos,getResources().getStringArray(R.array.select_pos)[measure.getPosition()]);
                holder.setText(R.id.his_item_mood,getResources().getStringArray(R.array.select_mood)[measure.getMood()]);
                SimpleDateFormat myFmt=new SimpleDateFormat("dd-MM-yyyy HH:mm");
                holder.setText(R.id.his_item_time,myFmt.format(new Date(measure.getTime())));
            }
        };
        myList.setAdapter(commonAdapter);
        myList.setHasFixedSize(true);
        myList.setItemAnimator(new DefaultItemAnimator());
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromD.getTime()!=toD.getTime())
                {
                    measureList.clear();
                    List<Measure> m2=mDAO.queryBuilder().where(MeasureDao.Properties.Time.between(fromD.getTime(),toD.getTime())).list();
                   // Toast.makeText(getActivity(),"list size:"+m2.size(),Toast.LENGTH_LONG).show();
                    measureList.addAll(m2);
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
            measureList.clear();
            measureList.addAll(mDAO.queryBuilder().where(MeasureDao.Properties.Time.between(fromD.getTime(),toD.getTime())).list());
            commonAdapter.notifyDataSetChanged();
    }


    private void showDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                timeText.setText(time.toString());
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                if(timeText.getId()==R.id.his_list_from) fromD.setTime(calendar.getTimeInMillis());
                else toD.setTime(calendar.getTimeInMillis());
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void initGreenDao()
    {
        helper = new DaoMaster.DevOpenHelper(getActivity(), "bp-monitor", null);
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
}
