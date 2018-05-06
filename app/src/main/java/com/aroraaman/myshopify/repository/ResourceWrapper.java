package com.aroraaman.myshopify.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ResourceWrapper<T> {
    public enum State {
        LOADING,
        SUCCESS,
        ERROR,
        REQUEST_FAILED
    }

    @NonNull public final State state;
    @Nullable public final T data;
    @Nullable public final Throwable error;

    private ResourceWrapper(@NonNull State state, @Nullable T data, @Nullable Throwable error) {
        this.state = state;
        this.data = data;
        this.error = error;
    }

    static <T> ResourceWrapper<T> loading(T data) {
        return new ResourceWrapper<>(State.LOADING, data, null);
    }

    static <T> ResourceWrapper<T> success(T data) {
        return new ResourceWrapper<>(State.SUCCESS, data, null);
    }

    static <T> ResourceWrapper<T> error(@NonNull Throwable t) {
        return new ResourceWrapper<>(State.ERROR, null, t);
    }

    static <T> ResourceWrapper<T> failed(T data) {
        return new ResourceWrapper<>(State.REQUEST_FAILED, data, null);
    }
}
