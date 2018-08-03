package com.example.asus.bookingreal.Database.Datasouce;

import com.example.asus.bookingreal.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSouce {
    Flowable<List<Cart>> getCartItem();

    Flowable<List<Cart>> getCartItemById(int cartItemId);

    int countCartItem();

    void emtryCart();

    void insertToCart(Cart... carts);

    void updateCart(Cart... carts);

    void deleteCartItem(Cart cart);
}
