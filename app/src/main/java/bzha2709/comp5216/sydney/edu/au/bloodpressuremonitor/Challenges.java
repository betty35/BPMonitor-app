package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.ChallengeItem;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.ImgUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class Challenges extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    CommonAdapter<ChallengeItem> commonAdapter;

    public Challenges() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_challenges, container, false);
        recyclerView= view.findViewById(R.id.challenge_list);
        llm=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        Resources resources=getResources();
        List<ChallengeItem> challengeItemList=new ArrayList<ChallengeItem>();
        challengeItemList.add(new ChallengeItem(ImgUtils.decodeSampledBitmapFromResource(resources,R.drawable.measure,100,100),
                "Know your condition. Measure blood pressure 3+ times/day. Set alarms."));
        challengeItemList.add(new ChallengeItem(ImgUtils.decodeSampledBitmapFromResource(resources,R.drawable.walking,100,100),
                "Try to be active. Do some exercise and keep fit."));
        challengeItemList.add(new ChallengeItem(ImgUtils.decodeSampledBitmapFromResource(resources,R.drawable.vegetable,100,100),
                "Less fat. More vegetables and fruit."));
        challengeItemList.add(new ChallengeItem(ImgUtils.decodeSampledBitmapFromResource(resources,R.drawable.salt,100,100),
                "Eat less than 5 grams of salt."));
        challengeItemList.add(new ChallengeItem(ImgUtils.decodeSampledBitmapFromResource(resources,R.drawable.alcohol,100,100),
                "No alcohol today!"));
        commonAdapter=new CommonAdapter<ChallengeItem>(getActivity(),R.layout.challenge_item,challengeItemList) {
            @Override
            protected void convert(ViewHolder holder, ChallengeItem challengeItem, int position) {
                holder.setImageBitmap(R.id.challenge_img,challengeItem.getImg());
                holder.setText(R.id.challeng_text,challengeItem.getDes());
            }
        };
        recyclerView.setAdapter(commonAdapter);
        return view;
    }

}
