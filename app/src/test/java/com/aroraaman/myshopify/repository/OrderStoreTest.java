package com.aroraaman.myshopify.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.aroraaman.myshopify.BuildConfig;
import com.aroraaman.myshopify.model.Customer;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19, constants = BuildConfig.class)
public class OrderStoreTest {
    private OrderStore mSut;

    private static final String PREF_NAME = "com.aroraaman.myshopify.repo_orders";
    private static final String KEY_ORDERS_JSON = "KEY_ORDERS_JSON";

    @Mock IOrderParser mOrderParser;

    private SharedPreferences mPrefs;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mPrefs = RuntimeEnvironment.application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        mSut = new OrderStore(RuntimeEnvironment.application, mOrderParser);
    }

    @Test
    public void persistOrders() throws Exception {
        // Arrange
        Item item = new Item("title", 1);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item);
        items.add(item);

        Customer customer = new Customer("firstName", "lastName");

        Order order = new Order(customer, items, 1.1);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order);

        String expectedJsonString = "{\"orders\":[{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}},{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}}]}";

        // Act
        mSut.persistOrders(orders);

        // Assert
        assertThat(mPrefs.getString(KEY_ORDERS_JSON, null)).isEqualToIgnoringWhitespace(expectedJsonString);
    }

    @Test
    public void getOrders_validJsonPersisted_returnsOrdersArray() throws Exception {
        // Arrange
        ArrayList<Order> orders = new ArrayList<>();
        when(mOrderParser.getOrders(anyString())).thenReturn(orders);

        String ordersJson = "{\"orders\":[{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}},{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}}]}";
        mPrefs.edit().putString(KEY_ORDERS_JSON, ordersJson).apply();

        // Act
        ArrayList<Order> result = mSut.getOrders();

        // Assert
        assertThat(result).isEqualTo(orders);
    }

    @Test
    public void getOrders_invalidJsonPersisted_returnsEmptyList() throws Exception {
        // Arrange
        String ordersJson = "{\"orders:[{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}},{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}}]}";
        mPrefs.edit().putString(KEY_ORDERS_JSON, ordersJson).apply();

        // Act
        ArrayList<Order> result = mSut.getOrders();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void clearOrders() throws Exception {
        // Arrange
        String ordersJson = "{\"orders\":[{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}},{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}}]}";
        mPrefs.edit().putString(KEY_ORDERS_JSON, ordersJson).apply();

        // Act
        mSut.clearOrders();

        // Assert
        assertThat(mPrefs.contains(KEY_ORDERS_JSON)).isFalse();
    }
}