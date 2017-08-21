package com.aroraaman.myshopify.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceWrapper<T> {
    enum State {
        LOADING,
        SUCCESS,
        ERROR,
        REQUEST_FAILED;
    }

    @NotNull public final State state;
    @Nullable public final T data;
    @Nullable public final Throwable throwable;

    private ResourceWrapper(@NotNull State state, @Nullable T data, @Nullable Throwable throwable) {
        this.state = state;
        this.data = data;
        this.throwable = throwable;
    }

    static <T> ResourceWrapper<T> loading(T data) {
        return new ResourceWrapper<>(State.LOADING, data, null);
    }

    static <T> ResourceWrapper<T> success(T data) {
        return new ResourceWrapper<>(State.SUCCESS, data, null);
    }

    static <T> ResourceWrapper<T> error(T data, @NotNull Throwable t) {
        return new ResourceWrapper<T>(State.ERROR, null, t);
    }

    static <T> ResourceWrapper<T> failed(T data) {
        return new ResourceWrapper<T>(State.REQUEST_FAILED, null, null);
    }
}
