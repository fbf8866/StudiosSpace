package com.example.magic.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.HQ.CoinAdapter;
import com.example.magic.myapplication.activity.HQ.CoinInfo;
import com.example.magic.myapplication.activity.HQ.CommonViewHolder;
import com.example.magic.myapplication.activity.HQ.HRecyclerView;

import java.util.ArrayList;

public class HQActivityNew extends BaseActivity {
    private static final String TAG = "HQActivityNew";
    private ArrayList<CoinInfo> mDataModels;
    private HQActivityNew mThis = this;
    private HRecyclerView hRecyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hqnew);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
         hRecyclerView= (HRecyclerView) findViewById(R.id.id_hrecyclerview);
    }

    @Override
    public void initData() {
        mDataModels = new ArrayList<>();
        CoinInfo coinInfo1 = new CoinInfo();
        coinInfo1.name = "USDT1";
        coinInfo1.code = "600331";
        coinInfo1.type = View.GONE;
        coinInfo1.priceLast="21.0";
        coinInfo1.riseRate24="1.2";
        coinInfo1.vol24="11020";
        coinInfo1.close="21.2";
        coinInfo1.open="41.0";
        coinInfo1.bid="31.2";
        coinInfo1.ask="11.0";
        coinInfo1.amountPercent = "13.3%";
        mDataModels.add(coinInfo1);
        for(int i=0;i<10000;i++) {
            CoinInfo coinInfo = new CoinInfo();
            coinInfo.name = "USDT";
            coinInfo.code = "600330";
            coinInfo.type = View.VISIBLE;
            coinInfo.priceLast="20.0";
            coinInfo.riseRate24="0.2";
            coinInfo.vol24="10020";
            coinInfo.close="22.2";
            coinInfo.open="40.0";
            coinInfo.bid="33.2";
            coinInfo.ask="19.0";
            coinInfo.amountPercent = "33.3%";
            mDataModels.add(coinInfo);
        }
    }

    private void initEvent(){
        hRecyclerView.setHeaderListData(getResources().getStringArray(R.array.right_title_name));
        hRecyclerView.setOnTitleClickListener(new HRecyclerView.onTtitleClickListener() {
            @Override
            public void setOnTitleClickListener(String type,int tag) {
                Toast.makeText(mThis, "position--->"+type+"---"+tag, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "setOnTitleClickListener: "+type+"---"+tag);
            }
        });

        CoinAdapter adapter = new CoinAdapter(mThis, mDataModels, R.layout.item_layout, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Toast.makeText(mThis, "position--->"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });

        hRecyclerView.setAdapter(adapter);
    }
}
