package com.aroraaman.myshopify.myshopify.ui;

import com.aroraaman.myshopify.model.Customer;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;
import com.aroraaman.myshopify.repository.IShopifyRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

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
    public void amountSpentProcessor_ordersArrayIsNull_returnsNull() throws Exception {
        // Arrange

        // Act
        OrdersViewModel.Data<Double> result = mSut.amountSpentProcessor(null, "", "");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void amountSpentProcessor_returnsCorrectValues() throws Exception {
        // Arrange
        ArrayList<Order> orders = arrangeTest();

        String firstName = "firstName";
        String lastName = "lastName";
        OrdersViewModel.Data expectedResult =
                new OrdersViewModel.Data<>(12.1, 1.1, firstName + " " + lastName);

        // Act
        OrdersViewModel.Data<Double> result = mSut.amountSpentProcessor(orders, "firstName", "lastName");

        // Assert
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void quantityProcessor_ordersArrayIsNull_returnsNull() throws Exception {
        // Arrange

        // Act
        OrdersViewModel.Data<Integer> result = mSut.quantityProcessor(null, "");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void quantityProcessor_returnsCorrectValues() throws Exception {
        // Arrange
        ArrayList<Order> orders = arrangeTest();
        String title = "title";

        OrdersViewModel.Data expectedResult =
                new OrdersViewModel.Data<>(10, 16, title);

        // Act
        OrdersViewModel.Data<Integer> result = mSut.quantityProcessor(orders, title);

        // Assert
        assertThat(result).isEqualTo(expectedResult);
    }

    private ArrayList<Order> arrangeTest() {
        ArrayList<Item> items = new ArrayList<>();
        Item item = new Item("title", 1);
        items.add(item);
        item = new Item("title", 4);
        items.add(item);
        item = new Item("title123", 8);
        items.add(item);

        ArrayList<Order> orders = new ArrayList<>();
        Customer customer = new Customer("firstName", "lastName");
        Order order = new Order(customer, items, 12.1);
        orders.add(order);
        customer = new Customer("first_name", "last_name");
        order = new Order(customer, items, 1.1);
        orders.add(order);

        return orders;
    }
}