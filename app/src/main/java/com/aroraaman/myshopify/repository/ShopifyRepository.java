package com.aroraaman.myshopify.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.aroraaman.myshopify.model.Order;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class ShopifyRepository implements IShopifyRepository{
    private final IOrderStore mOrderStore;
    private final IOrderParser mOrderParser;
    private final OkHttpClient mClient;

    private boolean isMakingCall;

    ShopifyRepository(IOrderStore orderStore, IOrderParser orderParser, OkHttpClient client) {
        this.mOrderStore = orderStore;
        this.mOrderParser = orderParser;
        this.mClient = client;
    }

    @Override
    public LiveData<ResourceWrapper<ArrayList<Order>>> getOrders(String webAddress) {
        MutableLiveData<ResourceWrapper<ArrayList<Order>>> data = new MutableLiveData<>();

        if (!isMakingCall) {
            isMakingCall = true;
            Request request = new Request.Builder()
                    .url(webAddress)
                    .build();
            mClient.newCall(request).enqueue(new RequestCallback(data));
        }

        ArrayList<Order> orders = mOrderStore.getOrders();

        data.postValue(ResourceWrapper.loading(orders));
        return data;
    }

    private class RequestCallback implements Callback {

        private final MutableLiveData<ResourceWrapper<ArrayList<Order>>> mData;

        private RequestCallback(MutableLiveData<ResourceWrapper<ArrayList<Order>>> mData) {
            this.mData = mData;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            isMakingCall = false;
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body == null) {
                    mData.postValue(ResourceWrapper.<ArrayList<Order>>error(new Throwable("No orders found")));
                    return;
                }
                String responseString = body.string();

                ArrayList<Order> orders = mOrderParser.fromJson(responseString);
                if (orders == null) {
                    mData.postValue(ResourceWrapper.<ArrayList<Order>>error(new Throwable("Invalid response received")));
                    return;
                }

                mOrderStore.persistOrders(orders);
                mData.postValue(ResourceWrapper.success(orders));
            } else {
                mData.postValue(ResourceWrapper.<ArrayList<Order>>error(new Throwable("An error occurred")));
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            isMakingCall = false;
            mData.postValue(ResourceWrapper.<ArrayList<Order>>failed(null));
        }
    }
}
