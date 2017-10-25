package com.example.magic.myapplication.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.magic.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class CustomSearchView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "CustomSearchView";
    private View mView;
    private Context mContext;
    private EditText custom_search_child_edittext;
    private LinearLayout custom_search_child_container;
    private ListView custom_search_child_listview;
    private Button custom_search_child_clean;
    private ImageView custom_search_child_delete;
    private Drawable custom_search_child_edittext_bg;
    private Drawable custom_search_child_delete_img;
    private Drawable custom_search_child_button_clean_bg;
    private String edittext_hint;
    private String clean_text;
    private String input_edittext = ""; //输入的内容
    private MyAdapter myAdapter;
    private ArrayList<String> history_counts;


    public CustomSearchView(Context context) {
        super(context);
        initView(context);
        Log.i(TAG, "CustomSearchView: 1");
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_search);
        custom_search_child_edittext_bg = typedArray.getDrawable(R.styleable.custom_search_child_edittext_bg);
        if (null != custom_search_child_edittext_bg){
            setEditTextBackground(custom_search_child_edittext_bg);
        }
        custom_search_child_delete_img = typedArray.getDrawable(R.styleable.custom_search_child_delete_img);
        if (null != custom_search_child_delete_img){
            setDeleteImageBackground(custom_search_child_delete_img);
        }
        edittext_hint = typedArray.getString(R.styleable.custom_search_child_edittext_hint);
        if (!TextUtils.isEmpty(edittext_hint)){
            setEditTextHint(edittext_hint);
        }
        clean_text = typedArray.getString(R.styleable.custom_search_child_button_clean_text);
        if (!TextUtils.isEmpty(clean_text)){
            setDeleteButtonText(clean_text);
        }
        custom_search_child_button_clean_bg = typedArray.getDrawable(R.styleable.custom_search_child_button_clean_bg);
        if (null != custom_search_child_button_clean_bg){
            setCleanButtonBackground(custom_search_child_button_clean_bg);
        }
        int color = typedArray.getColor(R.styleable.custom_search_child_button_clean_text_color, 0);
        if (0 != color){
            setCleanButtonColor(color);
        }
        typedArray.recycle();
    }

    private void initView(Context context) {
        if (null == mView){
            mView = LayoutInflater.from(context).inflate(R.layout.custom_search,null);
        }
        custom_search_child_edittext = (EditText) mView.findViewById(R.id.custom_search_child_edittext);
        custom_search_child_delete = (ImageView)mView.findViewById(R.id.custom_search_child_delete);
        custom_search_child_container = (LinearLayout)mView.findViewById(R.id.custom_search_child_container);
        custom_search_child_listview = (ListView)mView.findViewById(R.id.custom_search_child_listview);
        custom_search_child_clean = (Button)mView.findViewById(R.id.custom_search_child_clean);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mView, lp);

        custom_search_child_clean.setOnClickListener(this);
        history_counts = getHistory();
        Log.i(TAG, "initView: ========"+history_counts.size());
        if (history_counts.size() >1){
            custom_search_child_container.setVisibility(View.VISIBLE);
        }
        myAdapter = new MyAdapter(history_counts);
        custom_search_child_listview.setAdapter(myAdapter);
        custom_search_child_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    custom_search_child_edittext.setText(history_counts.get(i));
                    //设置光标位置
                    custom_search_child_edittext.setSelection(history_counts.get(i).length());
            }
        });

        initEvent();
    }

    private void initEvent() {
        custom_search_child_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                input_edittext = charSequence.toString();
                if (!TextUtils.isEmpty(input_edittext) && input_edittext.length()>=1){
                    custom_search_child_delete.setBackground(getResources().getDrawable(R.drawable.delete_press));
                    custom_search_child_delete.setClickable(true);
                    custom_search_child_delete.setOnClickListener(CustomSearchView.this);
                }else{
                    custom_search_child_delete.setClickable(false);
                    custom_search_child_delete.setBackground(custom_search_child_delete_img);
                }

                history_counts = getHistory();
                myAdapter = new MyAdapter(history_counts);
                custom_search_child_listview.setAdapter(myAdapter);
                myAdapter.getFilter().filter(input_edittext);
                custom_search_child_container.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                input_edittext = editable.toString();
            }
        });

        //禁止输入空格和回车
        custom_search_child_edittext.setFilters(new InputFilter[]{filters});
    }

    private InputFilter filters = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if(" ".equals(source)||source.toString().contentEquals("\n"))return "";
            else return null;
        }
    };

    /**
     * 保存历史记录
     * @param inputs
     */
    public void saveHistory(String inputs) {
        SharedPreferences sp = mContext.getSharedPreferences("HISTORY_COUNT", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(inputs)) {
            String longhistory = sp.getString("history", " ");
            StringBuilder sb = new StringBuilder(longhistory);
            if (longhistory.contains(inputs + ", ")) {
                if (longhistory.indexOf(inputs + ", ")!= -1){
                    sb.delete(longhistory.indexOf(inputs + ", "),longhistory.indexOf(inputs + ", ")+inputs.length()+2);
                }
            }
            sb.insert(0, inputs + ", ");
            sp.edit().putString("history", sb.toString()).commit();

            //隐藏输入法
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(custom_search_child_edittext.getWindowToken(), 0);
        }
    }


    /**
     * 获取历史记录
     * @return
     */
    private  ArrayList<String> getHistory(){
        SharedPreferences sp = mContext.getSharedPreferences("HISTORY_COUNT", Context.MODE_PRIVATE);
        String history = sp.getString("history", " ");
        ArrayList list = new ArrayList();
        if (TextUtils.isEmpty(history)){
            return list;
        }else{
            String[] historyArray = history.toString().split(", ");
            for (int i = 0; i < historyArray.length; i++) {
                list.add(historyArray[i]);
            }
            return list;
        }
    }

    /**
     * 清除历史记录
     */
    private void clearHistory(){
        SharedPreferences sp = mContext.getSharedPreferences("HISTORY_COUNT", Context.MODE_PRIVATE);
        sp.edit().clear().commit();
        myAdapter.notifyDataSetChanged();
        custom_search_child_container.setVisibility(View.GONE);
    }

    /**
     * 设置edittext的背景图片
     * @param drawable
     */
    public void setEditTextBackground(Drawable drawable){
        custom_search_child_edittext.setBackground(drawable);
    }

    /**
     * 设置删除按钮的背景图片
     * @param drawable
     */
    public void setDeleteImageBackground(Drawable drawable){
        custom_search_child_delete.setBackground(drawable);
    }

    /**
     * 设置editetext 的hint值
     * @param hint
     */
    public void setEditTextHint(String hint){
        custom_search_child_edittext.setHint(hint);
    }

    /**
     * 设置删除历史记录按钮的text
     * @param text
     */
    public void setDeleteButtonText(String text){
        custom_search_child_clean.setText(text);
    }

    /**
     * 设置删除历史记录按钮的背景图片
     * @param drawable
     */
    public void setCleanButtonBackground(Drawable drawable){
        custom_search_child_clean.setBackground(drawable);
    }

    /**
     * 设置清除历史记录按钮的字体颜色
     * @param color
     */
    public  void setCleanButtonColor(int color){
        custom_search_child_clean.setTextColor(color);
    }

    /**
     * 获取输入的edittext内容
     * @return
     */
    public String getEditTextInput(){
        return input_edittext;
    }

    /**
     * 设置输入框的内容为空
     */
    public void setEditTextInputNull(){
        custom_search_child_edittext.setText("");
        input_edittext = "";
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.custom_search_child_clean: //清除历史记录按钮
                clearHistory();
                break;
            case R.id.custom_search_child_delete: //删除输入内容按钮
                custom_search_child_edittext.setText("");
                input_edittext = "";
                if (null != myAdapter){
                    myAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    class MyAdapter  extends BaseAdapter implements Filterable{
        private MyAdapter.MyFilter myFilter;
        private ArrayList<String> mHistory_count;

        public MyAdapter(ArrayList<String> history_count){
            mHistory_count = history_count;
        }

        @Override
        public int getCount() {
            return mHistory_count.size()-1;  //这里－1 是因为最下面会多出一个空的条目,点击后会角标越界,为何会多一个原因不清楚
        }

        @Override
        public Object getItem(int i) {
            return mHistory_count.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            Log.i(TAG, "getView: 大小"+mHistory_count.size());
            if (null != view){
                holder = (ViewHolder) view.getTag();
            }else{
                view = LayoutInflater.from(mContext).inflate(R.layout.item_history,null);
                holder = new ViewHolder();
                holder.item_history_textview = (TextView) view.findViewById(R.id.item_history_textview);
                view.setTag(holder);
            }
            holder.item_history_textview.setText(mHistory_count.get(i));
            return view;
        }

        @Override
        public Filter getFilter() {
            if (myFilter == null) {
                myFilter = new MyFilter();
            }
            return myFilter;
        }

        //我们需要定义一个过滤器的类来定义过滤规则
        class MyFilter extends Filter {
            //我们在这个方法中定义过滤规则
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults result = new FilterResults();
                List<String> data = new ArrayList<>();
                if (TextUtils.isEmpty(charSequence)) {//prefix过滤字符串 如果当前输入的字符串为空则显示全部的数据
                    data = mHistory_count;
                    result.values = data;
                    result.count = data.size();
                } else {
                    String prefixString = charSequence.toString().toLowerCase();//转换为小写
                    data = new ArrayList<String>();
                    final int count = mHistory_count.size();//原始数据的大小
                    for (int i = 0; i < count; i++) {
                        final String content = mHistory_count.get(i).toLowerCase();

//                        if (content.startsWith(prefixString)) {
                        if (content.contains(prefixString)) {    //这个是只要历史记录中,有包含输入的内容,就显示出来
                            data.add(content);
                        } else {
                            final String[] words = content.split(" ");
                            final int wordCount = words.length;
                            // Start at index 0, in case valueText starts with space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    data.add(content);
                                    break;
                                }
                            }
                        }
                    }

                }
                result.values = data;
                result.count = data.size();
                return result;
            }

            //在publishResults方法中告诉适配器更新界面
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mHistory_count = (ArrayList<String>) filterResults.values;
                if (filterResults.count > 0) {
                    notifyDataSetChanged();//通知数据发生了改变
                } else {
//                    notifyDataSetInvalidated();//通知数据失效
                    custom_search_child_container.setVisibility(View.GONE);
                }
            }
        }

        class ViewHolder{
             TextView item_history_textview;
        }
    }

}
