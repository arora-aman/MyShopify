package com.aroraaman.myshopify.myshopify.ui;

import com.aroraaman.myshopify.model.Customer;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;
import com.aroraaman.myshopify.model.Province;
import com.aroraaman.myshopify.repository.IShopifyRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrdersViewModelTest {
    private OrdersViewModel mSut;

    @Mock IShopifyRepository mShopifyRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mSut = new OrdersViewModel(mShopifyRepository);
    }

    @Test
    public void getOrders() throws Exception {
        // Arrange

        // Act
        mSut.getOrders("https://www.shopify.com/");

        // Assert
        verify(mShopifyRepository).getOrders("https://www.shopify.com/");
    }


    @Test
    public void getProvinceData_ordersArrayNull_returnsEmptyList() throws Exception {
        // Arrange
        ArrayList<Order> orders = null;

        // Act
        ArrayList<OrdersViewModel.ProvinceData> result = mSut.getProvinceData(orders);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void getProvinceData_returnsCorrectData() throws Exception {
        // Arrange
        ArrayList<Order> orders = arrangeTest();

        Province colorado = new Province("CO", "Colorado");
        Province california = new Province("CA", "California");
        HashMap<Province, Integer> expectCounts = new HashMap<>();
        expectCounts.put(california, 1);
        expectCounts.put(colorado, 2);

        // Act
        ArrayList<OrdersViewModel.ProvinceData> result = mSut.getProvinceData(orders);

        // Assert
        for (OrdersViewModel.ProvinceData data : result) {
            assertThat(data.getOrders().size()).isEqualTo(expectCounts.get(data.getProvince()));
        }
    }

    @Test
    public void getYearData_ordersArrayNull_returnsEmptyList() throws Exception {
        // Arrange
        ArrayList<Order> orders = null;

        // Act
        ArrayList<Order> result = mSut.getYearData(orders, 2016);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void getYearData_returnsCorrectResult() throws Exception {
        // Arrange
        ArrayList<Order> orders = arrangeTest();

        // Act
        // TODO: Make this work
        ArrayList<Order> result = mSut.getYearData(orders, 2016);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    private ArrayList<Order> arrangeTest() {
        String createdAt2017 = "2017-12-05T23:04:52-05:00";
        String createdAt2016 = "2016-12-05T23:04:52-05:00";

        ArrayList<Item> items = new ArrayList<>();
        Item item = new Item("title", 1);
        items.add(item);
        item = new Item("title", 4);
        items.add(item);
        item = new Item("title123", 8);
        items.add(item);

        ArrayList<Order> orders = new ArrayList<>();
        Customer customer = new Customer("firstName", "lastName", new Province("CO", "Colorado"));

        Order order = new Order(customer, items, 12.1, createdAt2017);
        orders.add(order);

        order = new Order(customer, items, 12.1, createdAt2016);
        orders.add(order);

        customer = new Customer("first_name", "last_name", new Province("CA", "California"));
        order = new Order(customer, items, 1.1, createdAt2017);
        orders.add(order);

        customer = null;
        order = new Order(customer, items, 1.1, createdAt2017);
        orders.add(order);


        order = new Order(customer, items, 1.1, createdAt2016);
        orders.add(order);

        return orders;
    }
}