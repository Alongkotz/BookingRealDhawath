package com.example.asus.bookingreal.Database.Datasouce;

import com.example.asus.bookingreal.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSouce {

    private ICartDataSouce iCartDataSouce;

    public CartRepository(ICartDataSouce iCartDataSouce) {
        this.iCartDataSouce = iCartDataSouce;
    }

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSouce iCartDataSouce){
        if(instance == null)
            instance = new CartRepository(iCartDataSouce);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItem() {
        return iCartDataSouce.getCartItem();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return iCartDataSouce.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItem() {
        return iCartDataSouce.countCartItem();
    }

    @Override
    public void emtryCart() {
        iCartDataSouce.emtryCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSouce.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        iCartDataSouce.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSouce.deleteCartItem(cart);
    }
}
