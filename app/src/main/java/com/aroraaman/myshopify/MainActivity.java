package com.aroraaman.myshopify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    TextView textView1, textView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .build();

        httpClient.newCall(request).enqueue(new ResponseCallback(new WeakReference<>(textView1), new WeakReference<>(textView2)));

    }

    private class ResponseCallback implements Callback {

        final WeakReference<TextView> weakTextView1, weakTextView2;

        private ResponseCallback(WeakReference<TextView> weakTextView1, WeakReference<TextView> weakTextView2) {
            this.weakTextView1 = weakTextView1;
            this.weakTextView2 = weakTextView2;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                double totalSpent = 0;
                int totalQuantity = 0;

                    ResponseBody body = response.body();
                    if (body == null) {
                        Toast.makeText(MainActivity.this, "Response body is empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String jsonString = body.string();

                ArrayList<Order> data;

                try {
                    data = OrderParser.getOrders(jsonString);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Invalid response", Toast.LENGTH_LONG).show();
                    return;
                }

                for (Order order: data) {
                    if (order.customer != null && "Napoleon".equals(order.customer.firstName) && "Batz".equals(order.customer.lastName)) {
                        totalSpent += order.totalPrice;
                    }
                    for (Item item: order.items) {
                        if ("Awesome Bronze Bag".equals(item.title)) {
                            totalQuantity += item.quantity;
                        }
                    }
                }

                final double constTotalSpent = totalSpent;
                final int constTotalQuantity = totalQuantity;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weakTextView1.get().setText(getString(R.string.nap_batz, constTotalSpent));
                        weakTextView2.get().setText(getString(R.string.awesome_bronze_bag, constTotalQuantity));
                    }
                });

            } else {
                Toast.makeText(MainActivity.this, "Response failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Toast.makeText(MainActivity.this, "Failed to make request", Toast.LENGTH_LONG).show();
        }
    }
}
