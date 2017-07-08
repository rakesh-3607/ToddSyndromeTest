package com.testcompany.dagger;

import com.testcompany.ui.base.activities.MainActivity;
import com.testcompany.ui.base.activities.PatientDetailsActivity;
import com.testcompany.ui.base.activities.PatientHistoryActivity;
import com.testcompany.ui.base.activities.PatientsListActivity;
import com.testcompany.ui.base.activities.SplashActivity;
import com.testcompany.ui.base.fragments.BookMarkfragment;
import com.testcompany.ui.base.fragments.HistoryFragment;

import dagger.Component;


@ActivityInstanceScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityInstanceModule.class)
public interface ActivityInstanceComponent {

    void inject(SplashActivity activity);
    void inject(MainActivity activity);
    void inject(PatientsListActivity activity);
    void inject(PatientDetailsActivity activity);
    void inject(PatientHistoryActivity activity);


    void inject(HistoryFragment fragment);
    void inject(BookMarkfragment fragment);
}

