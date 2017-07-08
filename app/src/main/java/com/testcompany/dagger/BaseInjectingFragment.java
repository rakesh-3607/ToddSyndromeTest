package com.testcompany.dagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;


public class BaseInjectingFragment extends Fragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        Injector.injectFragment(this);
        super.onCreate(savedInstanceState);
    }

}
