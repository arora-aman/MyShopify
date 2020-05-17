package com.aroraaman.myshopify.repository;

import com.aroraaman.myshopify.model.Customer;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;
import com.aroraaman.myshopify.model.Province;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class OrderParserTest {
    private OrderParser mSut;

    @Before
    public void setUp() throws Exception {
        mSut = new OrderParser();
    }

    @Test
    public void fromJson_nullString_returnsNull() throws Exception {
        // Arrange

        // Act
        ArrayList<Order> result = mSut.fromJson(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void fromJson_invalidJson_returnsNull() throws Exception {
        // Arrange
        String jsonString = "{\"orders\"{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}},{\"total_price\":1.1,\"line_items\":[{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"},{\"fulfillable_quantity\":1,\"title\":\"title\"}],\"customer\":{\"last_name\":\"lastName\",\"first_name\":\"firstName\"}}]}";

        // Act
        ArrayList<Order> result = mSut.fromJson(jsonString);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void fromJson_validJson_returnsOrdersList() throws Exception {
        // Arrange
        Item item = new Item("title", 1);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item);
        items.add(item);

        Customer customer = new Customer("firstName", "lastName", new Province("CO", "Colorado"));
        Customer billingAddressCustomer = new Customer("billingAddressFirstName", "billingAddressLastName", new Province("CA", "California"));
        String createdAt = "2016-12-05T23:04:52-05:00";
        Order order1 = new Order(customer, items, 1.1, createdAt);
        Order order2 = new Order(billingAddressCustomer, items, 1.1, createdAt);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        
        String jsonString =
                "{\"orders\":[" +
                "{" +
                    "\"total_price\":1.1," +
                    "\"created_at\":\"" + createdAt + "\"," +
                    "\"line_items\":[" +
                    "{" +
                        "\"fulfillable_quantity\":1," +
                        "\"title\":\"title\"" +
                    "}," +
                    "{" +
                        "\"fulfillable_quantity\":1," +
                        "\"title\":\"title\"" +
                    "}," +
                    "{" +
                        "\"fulfillable_quantity\":1," +
                        "\"title\":\"title\"" +
                    "}]," +
                    "\"customer\":{" +
                        "\"last_name\":\"lastName\"," +
                        "\"first_name\":\"firstName\"," +
                        "\"default_address\": {" +
                            "\"province\":\"Colorado\"," +
                            "\"province_code\":\"CO\"" +
                        "}" +
                    "}" +
                "}," +
                "{" +
                    "\"total_price\":1.1," +
                    "\"created_at\":\"" + createdAt + "\"," +
                    "\"line_items\":[" +
                    "{" +
                        "\"fulfillable_quantity\":1," +
                        "\"title\":\"title\"" +
                    "}," +
                    "{" +
                        "\"fulfillable_quantity\":1," +
                        "\"title\":\"title\"" +
                    "}," +
                    "{" +
                        "\"fulfillable_quantity\":1," +
                        "\"title\":\"title\"" +
                    "}]," +
                    "\"billing_address\":{" +
                        "\"last_name\":\"billingAddressLastName\"," +
                        "\"first_name\":\"billingAddressFirstName\"," +
                        "\"province\":\"California\"," +
                        "\"province_code\":\"CA\"" +
                    "}" +
                "}]" +
                "}";

        // Act
        ArrayList<Order> results = mSut.fromJson(jsonString);

        // Assert
        assertThat(results.size()).isEqualTo(2);
        for (int i = 0; i < results.size(); ++i) {
            Order order = orders.get(i);
            Order result = results.get(i);
            assertThat(result.totalPrice).isEqualTo(order.totalPrice);
            assertThat(result.createdAt).isEqualTo(order.createdAt);
            assertThat(result.customer.firstName).isEqualTo(order.customer.firstName);
            assertThat(result.customer.lastName).isEqualTo(order.customer.lastName);
            assertThat(result.customer.province.provinceCode).isEqualTo(order.customer.province.provinceCode);
            assertThat(result.customer.province.province).isEqualTo(order.customer.province.province);
            assertThat(result.items.size()).isEqualTo(order.items.size());
            for (int j = 0; j < items.size(); ++j) {
                Item expectedItem = order.items.get(i);
                Item outputItem = result.items.get(i);
                assertThat(outputItem.quantity).isEqualTo(expectedItem.quantity);
                assertThat(outputItem.title).isEqualTo(expectedItem.title);
            }
        }
    }

    @Test
    public void toJson_nullOrders_returnsNull()throws Exception {
        // Arrange

        // Act
        String result = mSut.toJson(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void toJson_validOrdersList_returnsValidJson() throws Exception {
        // Arrange
        String expectedItemName = "title";
        int expectedItemQuantity = 1;

        Item item = new Item(expectedItemName, expectedItemQuantity);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item);
        items.add(item);

        String expectedFirstName = "firstName";
        String expectedLastName = "lastName";
        String expectedProvinceCode = "CO";
        String expectedProvince = "Colorado";

        Customer customer = new Customer(expectedFirstName, expectedLastName, new Province(expectedProvinceCode, expectedProvince));

        String expectedCreatedAt = "2016-12-05T23:04:52-05:00";
        double expectedTotalPrice = 1.1;
        Order order = new Order(customer, items, expectedTotalPrice, expectedCreatedAt);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order);

        // Act
        String result = mSut.toJson(orders);

        // Assert
        JSONObject jsonObject = new JSONObject(result);
        assertThat(jsonObject).isNotNull();
        assertThat(jsonObject.has("orders")).isTrue();

        JSONArray ordersJson = jsonObject.getJSONArray("orders");
        assertThat(ordersJson.length()).isEqualTo(2);

        for (int i = 0; i < ordersJson.length(); ++i) {
            JSONObject orderJson = ordersJson.getJSONObject(i);

            JSONObject customerJson = orderJson.getJSONObject("customer");
            assertThat(customerJson.getString("first_name")).isEqualTo(expectedFirstName);
            assertThat(customerJson.getString("last_name")).isEqualTo(expectedLastName);
            assertThat(customerJson.getJSONObject("default_address").getString("province")).isEqualTo(expectedProvince);
            assertThat(customerJson.getJSONObject("default_address").getString("province_code")).isEqualTo(expectedProvinceCode);

            assertThat(orderJson.getDouble("total_price")).isEqualTo(expectedTotalPrice);
            assertThat(orderJson.getString("created_at")).isEqualTo(expectedCreatedAt);

            JSONArray lineItemsJson = orderJson.getJSONArray("line_items");
            assertThat(lineItemsJson.length()).isEqualTo(3);

            for (int j = 0; j < lineItemsJson.length(); ++j) {
                JSONObject itemObject = lineItemsJson.getJSONObject(i);
                assertThat(itemObject.getString("title")).isEqualTo(expectedItemName);
                assertThat(itemObject.getInt("fulfillable_quantity")).isEqualTo(expectedItemQuantity);
            }
        }
    }
}