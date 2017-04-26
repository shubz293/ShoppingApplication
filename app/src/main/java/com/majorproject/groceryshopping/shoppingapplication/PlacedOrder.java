package com.majorproject.groceryshopping.shoppingapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubhank Dubey on 07-04-2017.
 */

class PlacedOrder {
    String orderID;
    String customerName;
    String customerAddress;
    int total;
    boolean status;
    List <ListItem> cartItem = new ArrayList<>();

    public PlacedOrder(String orderID ,String customerName, String customerAddress, int total, boolean status, List<ListItem> cartItem) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.total = total;
        this.status = status;
        this.cartItem = cartItem;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ListItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<ListItem> cartItem) {
        this.cartItem = cartItem;
    }
}
