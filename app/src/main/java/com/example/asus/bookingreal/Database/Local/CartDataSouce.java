package com.example.asus.bookingreal.Database.Local;

import com.example.asus.bookingreal.Database.Datasouce.ICartDataSouce;
import com.example.asus.bookingreal.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSouce implements ICartDataSouce {

    private CartDAO cartDAO;
    private static CartDataSouce instance;

    public CartDataSouce(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSouce getInstance(CartDAO cartDAO) {
        if (instance == null)
            instance = new CartDataSouce(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItem() {
        return cartDAO.getCartItem();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItem() {
        return cartDAO.countCartItem();
    }

    @Override
    public void emtryCart() {
        cartDAO.emtryCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        cartDAO.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);
    }
}
