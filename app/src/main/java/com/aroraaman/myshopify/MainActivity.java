package com.aroraaman.myshopify;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aroraaman.myshopify.model.ChartEntry;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

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

    LinearLayout mRootLayout;
    PieChart mChartBatz;
    PieChart mChartAwesomeBags;
    TextView mLoadingTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootLayout = new LinearLayout(this);
        mRootLayout.setOrientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRootLayout.setLayoutParams(params);
        mRootLayout.setPadding(10, 0, 10, 0);

        mChartBatz = createChart();
        mChartAwesomeBags = createChart();

        mLoadingTextView = new TextView(this);
        mLoadingTextView.setLayoutParams(params);
        mLoadingTextView.setGravity(Gravity.CENTER);
        mLoadingTextView.setText(R.string.fetching_data);

        mRootLayout.addView(mLoadingTextView);
        mRootLayout.addView(mChartBatz);
        mRootLayout.addView(mChartAwesomeBags);

        setContentView(mRootLayout);

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .build();

        httpClient.newCall(request).enqueue(new ResponseCallback(new WeakReference<>(mLoadingTextView), new WeakReference<>(mChartBatz), new WeakReference<>(mChartAwesomeBags)));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mRootLayout.setOrientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        mChartBatz.setLayoutParams(getOrientationBasedLayoutParams(newConfig.orientation));
        mChartAwesomeBags.setLayoutParams(getOrientationBasedLayoutParams(newConfig.orientation));
        super.onConfigurationChanged(newConfig);
    }

    private class ResponseCallback implements Callback {

        final WeakReference<TextView> loadingTextView;
        final WeakReference<PieChart> weakChartBatz, weakPieChartAwesomeBags;

        private ResponseCallback(WeakReference<TextView> loadingTextView, WeakReference<PieChart> weakChartBatz, WeakReference<PieChart> weakPieChartAwesomeBags) {
            this.loadingTextView = loadingTextView;
            this.weakChartBatz = weakChartBatz;
            this.weakPieChartAwesomeBags = weakPieChartAwesomeBags;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingTextView.get().setText(R.string.crunching_numbers);
                    }
                });
                double batzExpenditure = 0;
                double othersExpenditure = 0;
                int awesomeBagsSold = 0;
                int otherItemsSold = 0;

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

                for (Order order : data) {
                    if (order.customer != null && "Napoleon".equals(order.customer.firstName) && "Batz".equals(order.customer.lastName)) {
                        batzExpenditure += order.totalPrice;
                    } else {
                        othersExpenditure += order.totalPrice;
                    }
                    for (Item item : order.items) {
                        if ("Awesome Bronze Bag".equals(item.title)) {
                            awesomeBagsSold += item.quantity;
                        } else {
                            otherItemsSold += item.quantity;
                        }
                    }
                }

                final ArrayList<ChartEntry> batzPieEntries = new ArrayList<>();
                batzPieEntries.add(new ChartEntry(getString(R.string.nap_batz),
                        (float) batzExpenditure, Color.rgb(39, 111, 191)));
                batzPieEntries.add(new ChartEntry(getString(R.string.others),
                        (float) othersExpenditure, Color.rgb(175, 91, 91)));


                final ArrayList<ChartEntry> awesomeBagPieEntries = new ArrayList<>();
                awesomeBagPieEntries.add(new ChartEntry(getString(R.string.awesome_bronze_bags),
                        (float) awesomeBagsSold, Color.rgb(240, 58, 71)));
                awesomeBagPieEntries.add(new ChartEntry(getString(R.string.others),
                        (float) otherItemsSold, Color.rgb(175, 91, 91)));


                final String napBatzLabel = getString(R.string.nap_batz_label, batzExpenditure);
                final String awesomeBagLabel = getString(R.string.awesome_bronze_bag_label, awesomeBagsSold);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setChartData(weakChartBatz.get(), batzPieEntries, getString(R.string.customer_expenditure), napBatzLabel);
                        setChartData(weakPieChartAwesomeBags.get(), awesomeBagPieEntries, getString(R.string.items_sold), awesomeBagLabel);
                        loadingTextView.get().setVisibility(View.GONE);
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

    private PieChart createChart() {
        PieChart chart = new PieChart(this);

        int orientation = getResources().getConfiguration().orientation;

        LinearLayout.LayoutParams params = getOrientationBasedLayoutParams(orientation);

        chart.setLayoutParams(params);

        chart.setVisibility(View.GONE);

        return chart;
    }

    @NonNull
    private LinearLayout.LayoutParams getOrientationBasedLayoutParams(int orientation) {
        return new LinearLayout.LayoutParams(
                orientation == Configuration.ORIENTATION_PORTRAIT ? ViewGroup.LayoutParams.MATCH_PARENT: 0,
                orientation == Configuration.ORIENTATION_LANDSCAPE? ViewGroup.LayoutParams.MATCH_PARENT: 0, 1);
    }

    private void setChartData(PieChart chart, ArrayList<ChartEntry> entries, String dataSetLabel, String centerText) {
        chart.setDrawHoleEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (ChartEntry entry: entries) {
            pieEntries.add(new PieEntry(entry.value, entry.label));
            colors.add(entry.color);
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, dataSetLabel);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(0f);
        chart.setData(data);

        chart.setCenterText(centerText);
        chart.setCenterTextSize(12);
        chart.setCenterTextOffset(0, 10);

        chart.getDescription().setEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.invalidate();

        chart.setVisibility(View.VISIBLE);
    }
}
