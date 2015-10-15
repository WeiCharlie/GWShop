package com.charlie.gwshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.charlie.gwshop.R;
import com.charlie.gwshop.model.CartItem;

import java.util.List;

/**
 * Created
 * Author:Charlie Wei[]
 * Email:charlie_net@163.com
 * Date:2015/10/14
 */

/**
 * 基本的购物车列表适配器
 */
public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<CartItem> items;
    /**
     * 代表当前listView的显示模式，包含：0--普通模式，1--编辑模式
     */
    private int listMode;
    /**
     *当某一个条目通过checkBox选中状态发生变化，回调的接口
     */
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    /**
     * 数量调整按钮的处理
     */
    private View.OnClickListener countOnClickListener;

    // ALT+ insert 生成构造方法
    public CartAdapter(Context context, List<CartItem> items) {
        this.context = context;
        this.items = items;
    }

    /**
     * 用于调整购买数量
     * @return
     */
    public View.OnClickListener getCountOnClickListener() {
        return countOnClickListener;
    }

    public void setCountOnClickListener(View.OnClickListener countOnClickListener) {
        this.countOnClickListener = countOnClickListener;
    }

    /**
     * 设置接口，用于条目的选中
     * @param onCheckedChangeListener
     */
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (items != null) {
            ret = items.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        // 1 视图复用
        if (convertView != null) {
            ret = convertView;
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            ret = inflater.inflate(R.layout.cart_item,parent,false);
        }

        // 2 ViewHolder创建
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();

            // TODO 设置UI控件

            holder.checkBox = (CheckBox) ret.findViewById(R.id.cart_item_checkBox);

            // TODO 设置CheckBox选中变化的事件，选中之后改变cartItem的内容
            // ！！！核心：adapter传递事件监听器
            holder.checkBox.setOnCheckedChangeListener(onCheckedChangeListener);

            holder.imgIcon = (ImageView) ret.findViewById(R.id.cart_item_product_icon);
            holder.txtProductName = (TextView) ret.findViewById(R.id.cart_item_productName);
            holder.TxtProductPrice = (TextView) ret.findViewById(R.id.cart_item_product_price);
            // 加号按钮
            holder.btnInc = ret.findViewById(R.id.cart_item_inc);
            // TODO 事件
            holder.btnInc.setOnClickListener(countOnClickListener);
            holder.txtConut = (TextView) ret.findViewById(R.id.cart_item_count);
            // 删除
            holder.btnDelete = ret.findViewById(R.id.cart_item_delete);
            holder.btnDelete.setOnClickListener(countOnClickListener);

            // 减号按钮
            holder.btndec = ret.findViewById(R.id.cart_item_dec);
            // 减一件商品
            holder.btndec.setOnClickListener(countOnClickListener);



            ret.setTag(holder);
        }

        // 3 获取数据
        CartItem cartItem = items.get(position);


        // 4 显示数据
        String productName = cartItem.getProductName();
        holder.txtProductName.setText(productName);

        // 4.2显示数量
        int count = cartItem.getCount();
        holder.txtConut.setText(Integer.toString(count));
        // 4.2.1 设置按钮的Tag，用于给监听接口返回当前条目的位置
        holder.btnInc.setTag(position);
        holder.btndec.setTag(position);
        holder.btnDelete.setTag(position);
        /*设置按钮可见和不可见
        if (count==1){
            holder.btndec.setVisibility(View.INVISIBLE);
        }else {
            holder.btndec.setVisibility(View.VISIBLE);
        }
        */
        /**
         * 设置按钮的可点击和不可点击
         */
        if(count<=1){
            holder.btndec.setEnabled(false);
            holder.btndec.setAlpha(0.2f);
        }else {
            holder.btndec.setEnabled(true);
            holder.btndec.setAlpha(1.0f);
            if (count>=99){
                holder.btnInc.setEnabled(false);
                Toast.makeText(context,"数量达到上限",Toast.LENGTH_SHORT).show();
            }
        }

        // 4.3 显示价格
        float productPrice = cartItem.getProductPrice();
        holder.TxtProductPrice.setText(Float.toString(productPrice));
        // 4.4 显示图片（暂时保留）

        // 4.5 根据模式，处理CheckBox

        // 0 设置checkBox的Tag
        holder.checkBox.setTag(position);

        // 1 不论任何状态，VIewHolder中的所有控件在每一次getView时都必须重新设置与刷新
        if (listMode==1)
        {
            // 当编辑模式显示的时候，CheckBox是否选中依赖于cartItem的变量的
            boolean isChecked = cartItem.isChecked();
            holder.checkBox.setChecked(isChecked);
            // 只要是编辑模式，那么CheckBox可见
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }else {
            // 非编辑模式，CheckBox取消选中
            holder.checkBox.setChecked(false);
            // 注意，当非编辑模式，数据实体中的checked，也应该变成false
            holder.checkBox.setVisibility(View.INVISIBLE);
            holder.btnDelete.setVisibility(View.INVISIBLE);
        }

        return ret;
    }

    /**
     * 切换内部变量，进行编辑模式的切换；
     * 因为ListView显示内容的变化需要使用getView方法，
     * 那么 切换模式时让Adapter进行NotifyDataSetChanged，强制触发getView（）
     *
     */
    public void switchEditMode(){
        if (listMode == 1){
            listMode = 0;
        }else if(listMode == 0){
            listMode = 1;
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder{

        public CheckBox checkBox;

        public ImageView imgIcon;

        public TextView txtProductName;

        public TextView TxtProductPrice;
        // 加号
        public View btnInc;
        // 个数
        public TextView txtConut;
        // 减号
        public View btndec;
        // 删除
        public View btnDelete;
    }
}
