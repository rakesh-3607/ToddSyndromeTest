package com.testcompany.dagger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.testcompany.ui.BaseApplication;

public class BaseInjectingActivity extends AppCompatActivity {
    private ActivityInstanceComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        createComponent();
        Injector.injectActivity(this);
        super.onCreate(savedInstanceState);

    }

    private void createComponent(){

        ApplicationComponent applicationComponent = ((BaseApplication) getApplication()).getComponent();

        component = DaggerActivityInstanceComponent.builder().applicationComponent(applicationComponent)
                .build();
    }

    public ActivityInstanceComponent getComponent() {
        return component;
    }
}
