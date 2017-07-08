package com.testcompany.ui.base.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.testcompany.R;
import com.testcompany.utils.view.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.tvPetient)       CustomTextView tvPetient;
    @BindView(R.id.tvPastResult)    CustomTextView tvPastResult;
    @BindView(R.id.cvPatient)       CardView cvPatient;
    @BindView(R.id.cvPastResult)    CardView cvPastResult;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        layoutAnimation();
    }

    private void layoutAnimation(){
        try {
            Animation animationPatientView = AnimationUtils.loadAnimation(this, R.anim.zoom_in_home_flag);
            Animation animationHistoryView = AnimationUtils.loadAnimation(this, R.anim.zoom_in_home_flag);

            Animation animationPatientText = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            Animation animationHistoryText = AnimationUtils.loadAnimation(this, R.anim.fade_in);

            cvPatient.startAnimation(animationPatientView);
            cvPastResult.startAnimation(animationHistoryView);
            tvPetient.startAnimation(animationPatientText);
            tvPastResult.startAnimation(animationHistoryText);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void navigateToPatientsListScreen(View view) {
        Intent intent = new Intent(this, PatientsListActivity.class);
        navigateToDifferentScreen(intent,false,R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private void navigateToPastResultsScreen(View view) {
        Intent intent = new Intent(this, PatientHistoryActivity.class);
        navigateToDifferentScreen(intent,false,R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @OnClick({R.id.cvPatient, R.id.cvPastResult,R.id.tvPetient, R.id.tvPastResult})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cvPatient:
            case R.id.tvPetient:
                navigateToPatientsListScreen(view);
                break;
            case R.id.tvPastResult:
            case R.id.cvPastResult:
                navigateToPastResultsScreen(view);
                break;
        }
    }
}