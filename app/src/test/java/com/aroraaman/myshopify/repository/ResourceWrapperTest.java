package com.aroraaman.myshopify.repository;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ResourceWrapperTest {
    private DataClass mData;

    @Before
    public void setUp() throws Exception {
        mData = new DataClass();
    }

    @Test
    public void loading() throws Exception {
        // Arrange

        // Act
        ResourceWrapper result = ResourceWrapper.loading(mData);

        // Assert
        assertThat(result.state).isEqualTo(ResourceWrapper.State.LOADING);
        assertThat(result.data).isEqualTo(mData);
        assertThat(result.error).isNull();
    }

    @Test
    public void success() throws Exception {
        // Arrange

        // Act
        ResourceWrapper result = ResourceWrapper.success(mData);

        // Assert
        assertThat(result.state).isEqualTo(ResourceWrapper.State.SUCCESS);
        assertThat(result.data).isEqualTo(mData);
        assertThat(result.error).isNull();
    }

    @Test
    public void error() throws Exception {
        // Arrange
        Throwable expectedException = new Throwable();

        // Act
        ResourceWrapper result = ResourceWrapper.error(expectedException);

        // Assert
        assertThat(result.state).isEqualTo(ResourceWrapper.State.ERROR);
        assertThat(result.data).isNull();
        assertThat(result.error).isEqualTo(expectedException);
    }

    @Test
    public void failed() throws Exception {
        // Arrange

        // Act
        ResourceWrapper result = ResourceWrapper.failed(mData);

        // Assert
        assertThat(result.state).isEqualTo(ResourceWrapper.State.REQUEST_FAILED);
        assertThat(result.data).isEqualTo(mData);
        assertThat(result.error).isNull();
    }

    private class DataClass{}
}