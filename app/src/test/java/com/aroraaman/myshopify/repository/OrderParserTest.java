package com.aroraaman.myshopify.repository;

import com.aroraaman.myshopify.BuildConfig;
import com.aroraaman.myshopify.model.Customer;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19, constants = BuildConfig.class)
public class OrderParserTest {
    private OrderParser mSut;

    @Before
    public void setUp() throws Exception {
        mSut = new OrderParser();
    }

    @Test
    public void getOrders_nullString_returnsNull() throws Exception {
        // Arrange

        // Act
        ArrayList<Order> result = mSut.getOrders(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getOrders_invalidJson_returnsNull() throws Exception {
        // Arrange
        String jsonString = "{\"orders\"{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}},{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}}]}";

        // Act
        ArrayList<Order> result = mSut.getOrders(jsonString);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getOrders_validJson_returnsOrdersList() throws Exception {
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
        
        String jsonString = "{\"orders\":[{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}},{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"billing_address\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}}]}";

        // Act
        ArrayList<Order> result = mSut.getOrders(jsonString);

        // Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).customer.firstName).isEqualTo(orders.get(0).customer.firstName);
        assertThat(result.get(0).customer.lastName).isEqualTo(orders.get(0).customer.lastName);
        assertThat(result.get(0).items.size()).isEqualTo(3);
        assertThat(result.get(0).items.get(0).title).isEqualTo(orders.get(0).items.get(0).title);
        assertThat(result.get(0).items.get(0).quantity).isEqualTo(orders.get(0).items.get(0).quantity);
        assertThat(result.get(0).items.get(1).title).isEqualTo(orders.get(0).items.get(1).title);
        assertThat(result.get(0).items.get(1).quantity).isEqualTo(orders.get(0).items.get(1).quantity);
        assertThat(result.get(0).items.get(2).title).isEqualTo(orders.get(0).items.get(2).title);
        assertThat(result.get(0).items.get(2).quantity).isEqualTo(orders.get(0).items.get(2).quantity);
        assertThat(result.get(1).customer.firstName).isEqualTo(orders.get(1).customer.firstName);
        assertThat(result.get(1).customer.lastName).isEqualTo(orders.get(1).customer.lastName);
        assertThat(result.get(1).items.get(0).title).isEqualTo(orders.get(1).items.get(0).title);
        assertThat(result.get(1).items.get(0).quantity).isEqualTo(orders.get(1).items.get(0).quantity);
        assertThat(result.get(1).items.get(1).title).isEqualTo(orders.get(1).items.get(1).title);
        assertThat(result.get(1).items.get(1).quantity).isEqualTo(orders.get(1).items.get(1).quantity);
        assertThat(result.get(1).items.get(2).title).isEqualTo(orders.get(1).items.get(2).title);
        assertThat(result.get(1).items.get(2).quantity).isEqualTo(orders.get(1).items.get(2).quantity);
    }
}