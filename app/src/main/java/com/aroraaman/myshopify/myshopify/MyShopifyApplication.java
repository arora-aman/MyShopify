package com.aroraaman.myshopify.myshopify;

import android.app.Application;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

public class MyShopifyApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = createAppComponent();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    /**
     * Returns application component from context.
     * @param context Source context.
     * @return Application component instance.
     */
    public static ApplicationComponent getComponent(@NotNull Context context) {
        return from(context).getComponent();
    }

    /**
     * Returns application instance from context.
     * @param context Source context.
     * @return Application instance.
     */
    public static MyShopifyApplication from(@NotNull Context context) {
        return (MyShopifyApplication) context.getApplicationContext();
    }

    protected ApplicationComponent createAppComponent() {
        return DaggerApplicationComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }
}
