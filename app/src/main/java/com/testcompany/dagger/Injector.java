package com.testcompany.dagger;

import com.testcompany.ui.base.activities.MainActivity;
import com.testcompany.ui.base.activities.PatientDetailsActivity;
import com.testcompany.ui.base.activities.PatientsListActivity;
import com.testcompany.ui.base.activities.SplashActivity;
import com.testcompany.ui.base.fragments.BookMarkfragment;
import com.testcompany.ui.base.fragments.HistoryFragment;


public class Injector {

    public static void injectActivity(BaseInjectingActivity activity){

        ActivityInstanceComponent component = activity.getComponent();

        if (activity instanceof SplashActivity){
            component.inject((SplashActivity) activity);
        }else if (activity instanceof MainActivity){
            component.inject((MainActivity) activity);
        }else if (activity instanceof PatientsListActivity){
            component.inject((PatientsListActivity) activity);
        }else if (activity instanceof PatientDetailsActivity){
            component.inject((PatientDetailsActivity) activity);
        }
    }

    public static void injectService(){

    }

    public static void injectFragment(final BaseInjectingFragment fragment){
        BaseInjectingActivity activity = (BaseInjectingActivity) fragment.getActivity();
        ActivityInstanceComponent component = activity.getComponent();

        if (fragment instanceof HistoryFragment) {
            component.inject((HistoryFragment) fragment);
        }else if (fragment instanceof BookMarkfragment) {
            component.inject((BookMarkfragment) fragment);
        }
    }

}
