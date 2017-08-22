package com.aroraaman.myshopify.myshopify.ui;

import com.aroraaman.myshopify.dagger.PerActivity;
import com.aroraaman.myshopify.myshopify.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
}
