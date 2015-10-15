package com.charlie.gwshop;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alipay.sdk.pay.demo.PayDemoActivity;
import com.charlie.gwshop.adapters.CartAdapter;
import com.charlie.gwshop.model.CartItem;

import java.util.LinkedList;

public class GWShopActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CartAdapter adapter;
    private LinkedList<CartItem> items;
    // 总金额
    private TextView txtTotal;
    private double sum = 0.0;// 设置总钱数
    /**
     * 计算总金额的观察者，检测Adapter
     */
    private DataSetObserver sumObserver = new DataSetObserver() {
        /**
         *
         * 当Adapter调用notifyDataSetInvalidate（）时回调
         */
        @Override
        public void onInvalidated() {

        }

        /**
         * 当Adapter的notifyDataSetChanged被调用，那么自动回调这个方法
         */
        @Override
        public void onChanged() {
            // TODO 计算总金额
            sum = 0.0;
            for (CartItem item : items) {
                    int count = item.getCount();
                    float productPrice = item.getProductPrice();
                if (item.isChecked()) {
                    sum += (count * productPrice);
                }else {
                    sum += 0;
                }

            }
//            Log.d("Cart","总金额 = " +sum);
            txtTotal.setText(sum + "元");

        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getActionBar().hide();

        txtTotal = (TextView) findViewById(R.id.cart_total_text);
        ListView listView = (ListView) findViewById(R.id.cart_listView);

        items = new LinkedList<>();
        for (int i = 0; i < 30; i++) {
            CartItem cartItem = new CartItem();
            cartItem.setProductName("商品" + i);
            float productPrice = (int) (Math.random() * 98 + 1);
            cartItem.setProductPrice(productPrice);
            cartItem.setCount(1);

            items.add(cartItem);
        }
        adapter = new CartAdapter(this, items);
        // 由Activity来监听LiatView条目内部，CheckBox的选中变化
        adapter.setOnCheckedChangeListener(this);
        // 由Activity来处理数量的变化
        adapter.setCountOnClickListener(this);
        // 设置Adapter的数据比那话观察者只要Adapter的notifyDataSetChanged被调用，那么观察者自动回调
        adapter.registerDataSetObserver(sumObserver);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.unregisterDataSetObserver(sumObserver);
    }

    /**
     * 点击按钮 切换编辑模式
     * @param view
     */
    public void btnSwitchEditMode(View view) {
        for (CartItem item : items) {
            item.setChecked(false);
        }
        sum = 0.0;
        adapter.switchEditMode();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        Object tag = buttonView.getTag();
        // 获取Adapter中设置的Tag
        if (tag != null&&tag instanceof Integer) {
            int position = (Integer)tag;
//          Toast.makeText(this,position+" 切换选择 "+ isChecked,Toast.LENGTH_SHORT).show();
            CartItem cartItem = items.get(position);
            // 数据状态改变，不需要强制的notifyDataSetChanged
            cartItem.setChecked(isChecked);


        }
    }

    @Override
    public void onClick(View v) {
        // 点击接口，用于处理ListView内部的按钮的点击
        int id = v.getId();
        Object tag = v.getTag();
        switch (id){
            case R.id.cart_item_inc:// 加一
                if (tag != null && tag instanceof Integer){
                    int position = (Integer)tag;
                    CartItem cartItem = items.get(position);
                    int count = cartItem.getCount();
                    count++;
                    // 设置回去，
                    cartItem.setCount(count);
                    // 强制刷新适配器就会自动更新数量TextView
                    adapter.notifyDataSetChanged();
                }

                break;
            case R.id.cart_item_dec: // 减一
                if (tag != null && tag instanceof Integer){
                    int position = (Integer)tag;
                    CartItem cartItem = items.get(position);
                    int count = cartItem.getCount();
                    count--;
                    if (count>0) {
                        // 设置回去，
                        cartItem.setCount(count);

                        // 强制刷新适配器就会自动更新数量TextView
                        adapter.notifyDataSetChanged();
                    }else {
                        // TODO 对于小于1 的情况可以是不处理，也可以是删除数据条目
                    }
                }
                break;
            case R.id.cart_item_delete:// 删除，TODO 暂未完善
                if (tag != null && tag instanceof Integer){// 注意必须要设置Tag
                    int position = (Integer)tag;
                    Log.d("delete","这个地方是删除日志，说明进入到此方法");
                    items.remove(position);

                    }else {
                        // TODO 对于小于1 的情况可以是不处理，也可以是删除数据条目
                    }
                adapter.notifyDataSetChanged();

                break;
        }
    }

    public void btnPay(View view) {// 调用支付宝支付的DemoActivity来进行实际的支付操作
        Intent intent = new Intent(this, PayDemoActivity.class);
        intent.putExtra("total",sum);
        // TODO 传递订单名称以及钱的总数，让payDemoActivity能够正确的显示以及支付
        startActivity(intent);
    }
    boolean isAllChecked = false;
    public void AllChecked(View view) {// 设置全选按钮

        isAllChecked = !isAllChecked;

        for (CartItem item : items) {
            item.setChecked(isAllChecked);

        }

        adapter.notifyDataSetChanged();
    }

    public void checkBoxItem(View view) {// item中的checkBox点击
        adapter.notifyDataSetChanged();
    }
}
