package com.aroraaman.myshopify.repository;

import android.arch.lifecycle.LiveData;

import com.aroraaman.myshopify.BuildConfig;
import com.aroraaman.myshopify.model.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19, constants = BuildConfig.class)
public class ShopifyRepositoryTest {
    private ShopifyRepository mSut;

    @Mock IOrderParser mOrderParser;
    @Mock IOrderStore mOrderStore;
    @Mock OkHttpClient mClient;
    @Mock OrdersCall mCall;

    private static final String TEST_URL = "https://www.shopify.com/";

    private ArrayList<Order> mOrders;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mClient.newCall(any(Request.class))).thenReturn(mCall);

        mSut = new ShopifyRepository(mOrderStore,mOrderParser, mClient);

        mOrders = new ArrayList<>();
    }

    @Test
    public void getOrders_buildsRequestWithRequiredUrl() throws Exception {
        // Arrange
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);

        // Act
        mSut.getOrders(TEST_URL);

        // Assert
        verify(mClient).newCall(captor.capture());
        assertThat(captor.getValue().url().toString()).isEqualTo(TEST_URL);
    }

    @Test
    public void getOrders_enqueuesRequest() throws Exception {
        // Arrange

        // Act
        mSut.getOrders(TEST_URL);

        // Assert
        verify(mCall).enqueue(any(Callback.class));
    }

    @Test
    public void getOrders_noOrdersPersisted_returnsNullWrappedWithLoadingState() throws Exception {
        // Arrange
        when(mOrderStore.getOrders()).thenReturn(null);

        // Act
        LiveData<ResourceWrapper<ArrayList<Order>>> result = mSut.getOrders (TEST_URL);

        // Assert
        ResourceWrapper<ArrayList<Order>> orders = result.getValue();
        assertThat(orders).isNotNull();
        assertThat(orders.state).isEqualTo(ResourceWrapper.State.LOADING);
        assertThat(orders.data).isNull();
        assertThat(orders.error).isNull();
    }

    @Test
    public void getOrders_ordersPersisted_returnsPersistedOrdersWrappedWithLoadingState() throws Exception {
        // Arrange
        when(mOrderStore.getOrders()).thenReturn(mOrders);

        // Act
        LiveData<ResourceWrapper<ArrayList<Order>>> result = mSut.getOrders (TEST_URL);

        // Assert
        ResourceWrapper<ArrayList<Order>> orders = result.getValue();
        assertThat(orders).isNotNull();
        assertThat(orders.state).isEqualTo(ResourceWrapper.State.LOADING);
        assertThat(orders.data).isEqualTo(mOrders);
        assertThat(orders.error).isNull();
    }

    @Test
    public void getOrdersRequestCallback_successfulResponseNullBody_setsErrorState() throws Exception {
        // Arrange
        LiveData<ResourceWrapper<ArrayList<Order>>> data = mSut.getOrders(TEST_URL);

        Response response = createResponse(200, null);

        // Act
        getRequestCallback().onResponse(mCall, response);

        // Assert
        ResourceWrapper wrappedOrders = data.getValue();
        assertThat(wrappedOrders).isNotNull();
        assertThat(wrappedOrders.state).isEqualTo(ResourceWrapper.State.ERROR);
        assertThat(wrappedOrders.error).isNotNull();
        assertThat(wrappedOrders.data).isNull();
    }

    @Test
    public void getOrdersRequestCallback_successfulResponseInvalidBody_setsErrorState() throws Exception {
        // Arrange
        LiveData<ResourceWrapper<ArrayList<Order>>> data = mSut.getOrders(TEST_URL);

        String content = "{\"orders\":[]";
        ResponseBody body = ResponseBody.create(null, content);
        Response response = createResponse(200, body);

        when(mOrderParser.getOrders(content)).thenReturn(null);

        // Act
        getRequestCallback().onResponse(mCall, response);

        // Assert
        ResourceWrapper wrappedOrders = data.getValue();
        assertThat(wrappedOrders).isNotNull();
        assertThat(wrappedOrders.state).isEqualTo(ResourceWrapper.State.ERROR);
        assertThat(wrappedOrders.error).isNotNull();
        assertThat(wrappedOrders.data).isNull();
    }

    @Test
    public void getOrdersRequestCallback_successfulResponseValidBody_setsSuccessBodyAndPersistsOrders() throws Exception {
        // Arrange
        LiveData<ResourceWrapper<ArrayList<Order>>> data = mSut.getOrders(TEST_URL);

        String content = "{\"orders\":[]}";
        ResponseBody body = ResponseBody.create(null, content);
        Response response = createResponse(200, body);

        ArrayList<Order> expectedOrders = new ArrayList<>();

        when(mOrderParser.getOrders(content)).thenReturn(expectedOrders);

        // Act
        getRequestCallback().onResponse(mCall, response);

        // Assert
        verify(mOrderStore).persistOrders(expectedOrders);

        ResourceWrapper wrappedOrders = data.getValue();
        assertThat(wrappedOrders).isNotNull();
        assertThat(wrappedOrders.state).isEqualTo(ResourceWrapper.State.SUCCESS);
        assertThat(wrappedOrders.error).isNull();
        assertThat(wrappedOrders.data).isEqualTo(expectedOrders);
    }

    @Test
    public void getOrdersRequestCallback_unsuccessfulResponse_setsErrorState() throws Exception {
        // Arrange
        LiveData<ResourceWrapper<ArrayList<Order>>> data = mSut.getOrders(TEST_URL);

        Response response = createResponse(404, null);

        // Act
        getRequestCallback().onResponse(mCall, response);

        // Assert
        ResourceWrapper wrappedOrders = data.getValue();
        assertThat(wrappedOrders).isNotNull();
        assertThat(wrappedOrders.state).isEqualTo(ResourceWrapper.State.ERROR);
        assertThat(wrappedOrders.error).isNotNull();
        assertThat(wrappedOrders.data).isNull();
    }

    @Test
    public void getOrdersRequestCallback_requestFailed_setsFailedState() throws Exception {
        // Arrange
        LiveData<ResourceWrapper<ArrayList<Order>>> data = mSut.getOrders(TEST_URL);

        // Act
        getRequestCallback().onFailure(mCall, new IOException());

        // Assert
        ResourceWrapper wrappedOrders = data.getValue();
        assertThat(wrappedOrders).isNotNull();
        assertThat(wrappedOrders.state).isEqualTo(ResourceWrapper.State.REQUEST_FAILED);
        assertThat(wrappedOrders.error).isNull();
        assertThat(wrappedOrders.data).isNull();
    }

    private Response createResponse(int code, ResponseBody body) {
        return new Response.Builder()
                .request(getRequest())
                .protocol(Protocol.HTTP_1_0)
                .code(code)
                .body(body)
                .message("TEST MESSAGE")
                .build();
    }

    private Request getRequest() {
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(mClient).newCall(captor.capture());
        return captor.getValue();
    }

    private Callback getRequestCallback() {
        ArgumentCaptor<RequestCallback> captor = ArgumentCaptor.forClass(RequestCallback.class);
        verify(mCall).enqueue(captor.capture());
        return captor.getValue();
    }

    private interface OrdersCall extends Call {
    }

    private interface RequestCallback extends Callback {
    }
}