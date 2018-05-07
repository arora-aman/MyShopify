package com.aroraaman.myshopify.myshopify.ui;

import android.app.Activity;

import com.aroraaman.myshopify.dagger.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @PerActivity
    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @PerActivity
    @Provides
    IFragmentNavigator provideFragmentNavigation(){
        if (mActivity instanceof IFragmentNavigator) {
            return (IFragmentNavigator) mActivity;
        }

        return null;
    }

}
