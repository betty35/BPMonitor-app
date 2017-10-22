package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.R;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.Measure;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Measure> measures;
    Context con;

    public MyAdapter(Context context,List<Measure> m)
    {
        measures=m;
        con=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(con).inflate(R.layout.measure_item,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder h, int position) {
        Measure m=measures.get(position);
        h.dia.setText(m.getDia());
        h.sys.setText(m.getSys());
        h.pulse.setText(m.getPulse());
        h.time.setText(m.getTime().toString());
    }

    @Override
    public int getItemCount() {
        return measures.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sys;
        TextView dia;
        TextView pulse;
        TextView time;
        MyViewHolder(View view){
            super(view);
            dia = view.findViewById(R.id.his_item_dia);
            sys = view.findViewById(R.id.his_item_sys);
            pulse= view.findViewById(R.id.his_item_plu);
            time= view.findViewById(R.id.his_item_time);
        }
    }
}
