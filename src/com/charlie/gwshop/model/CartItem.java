package com.charlie.gwshop.model;

/**
 * Created
 * Author:Charlie Wei[]
 * Email:charlie_net@163.com
 * Date:2015/10/14
 */

/**
 * 购物车条目数据，用于描述购物车的一个Item，也是ListView加载时的数据对象
 */
public class CartItem {

    // 商品ID
    private long productId;
    // 商品名称
    private String productName;
    // 商品单价
    private float productPrice;
    //商品图标
    private String productIcon;


    // 购物车条目相关内容
    /**
     * 购买数量
     */
    private int count;

    // TODO 考虑增加其他字段
    // 代表在购物车中当前条目是否选中了CheckBox
    private boolean checked;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
