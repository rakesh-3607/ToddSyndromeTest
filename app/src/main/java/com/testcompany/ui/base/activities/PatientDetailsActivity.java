package com.testcompany.ui.base.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.testcompany.R;
import com.testcompany.datalayer.models.PatientModel;
import com.testcompany.datalayer.storage.DBHandler;
import com.testcompany.utils.StaticUtils;
import com.testcompany.utils.view.CustomButton;
import com.testcompany.utils.view.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;


public class PatientDetailsActivity extends BaseActivity {

    @BindView(R.id.ivGender)
    ImageView ivGender;
    @BindView(R.id.tvPatientName)
    CustomTextView tvPatientName;
    @BindView(R.id.tvPatientAge)
    CustomTextView tvPatientAge;
    @BindView(R.id.tvParsentage)
    CustomTextView tvParsentage;
    @BindView(R.id.llDeces)
    LinearLayout llDeces;
    @BindView(R.id.btnBookmark)
    CustomButton btnBookmark;
    @BindView(R.id.tvDisorder)
    CustomTextView tvDisorder;
    @BindView(R.id.tvDrugs)
    CustomTextView tvDrugs;
    @BindView(R.id.llDisorder)
    LinearLayout llDisorder;
    @BindView(R.id.llDrugs)
    LinearLayout llDrugs;

    DBHandler dbHandler;
    PatientModel patientData;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar(getString(R.string.patient_detail_screen), true);
        dbHandler = DBHandler.getInstance(PatientDetailsActivity.this); // created database singleton instance
        getBundleData();
    }

    /**
     * Method to get bundle data from previous screen
     */
    private void getBundleData() {
        if (getIntent().hasExtra("isApicall")) {
            btnBookmark.setVisibility(View.GONE);
        }

        if (getIntent().hasExtra("data")) {
            // variable to get parcelable data
            patientData = getIntent().getParcelableExtra("data");
        }
        setData();
    }

    /**
     * Method to render views
     */
    private void setData() {
        setBackGroundColor();
        buttonVisibelitySet();
        tvParsentage.setText(patientData.getPercentage() + "%");
        tvPatientName.setText(patientData.getPatient_name());
        tvPatientAge.setText(
                getString(R.string.age).concat(String.valueOf(patientData.getPatient_age())));
        setGenderImage();
        if (!TextUtils.isEmpty(patientData.getDisorderString())) {
            tvDisorder.setText(patientData.getDisorderString());
        } else {
            tvDisorder.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(patientData.getDisorderString())) {
            llDisorder.setVisibility(View.VISIBLE);
            tvDisorder.setText(patientData.getDisorderString());
        } else {
            llDisorder.setVisibility(View.GONE);
        }
        if (patientData.getPatient_drugs() != null && patientData.getPatient_drugs().size() > 0) {
            tvDrugs.setVisibility(View.VISIBLE);
            tvDrugs.setText(StaticUtils.getStringFromDrugList(patientData.getPatient_drugs()));
        } else {
            tvDrugs.setVisibility(View.GONE);
        }
        startInAnimation();
    }

    /**
     * Bookmark is visible if user navigate from history screen else it remains gone.
     */
    private void buttonVisibelitySet() {
        if (patientData.isBookmarked()) {
            btnBookmark.setText(R.string.remove_bookmark);
        } else {
            btnBookmark.setText(getString(R.string.bookmark));
        }
    }

    /**
     * Method for initial view animation
     */
    private void startInAnimation() {
        try {
            ObjectAnimator imageAnimator = ObjectAnimator.ofFloat(ivGender, "translationY", -1000f, 0f);
            imageAnimator.setDuration(600);

            ObjectAnimator imageAnimatorTitle = ObjectAnimator.ofFloat(tvPatientName, "translationY", -1000f, 0f);
            imageAnimatorTitle.setDuration(600);

            ObjectAnimator tvAnimatorAge = ObjectAnimator.ofFloat(tvPatientAge, "translationY", -1000f, 0f);
            tvAnimatorAge.setDuration(600);

            ObjectAnimator tvAnimatorDisorder = ObjectAnimator.ofFloat(llDisorder, "translationY", -1000f, 0f);
            tvAnimatorDisorder.setDuration(600);

            ObjectAnimator tvAnimatorDrug = ObjectAnimator.ofFloat(llDrugs, "translationY", -1000f, 0f);
            tvAnimatorDrug.setDuration(600);

            ObjectAnimator tvAnimatorPercentage = ObjectAnimator.ofFloat(tvParsentage, "translationY", 500f, 0f);
            tvAnimatorPercentage.setDuration(600);

            ObjectAnimator imageAnimatorBottom = ObjectAnimator.ofFloat(llDeces, "translationY", 500f, 0f);
            imageAnimatorBottom.setDuration(600);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setTarget(imageAnimatorTitle);
            animatorSet.setTarget(tvAnimatorPercentage);
            animatorSet.setTarget(tvAnimatorAge);
            animatorSet.setTarget(imageAnimator);
            animatorSet.setTarget(tvAnimatorDrug);
            animatorSet.setTarget(tvAnimatorDisorder);
            animatorSet.setTarget(imageAnimatorBottom);
            animatorSet.playTogether(imageAnimatorTitle, tvAnimatorPercentage, tvAnimatorAge, tvAnimatorDrug, tvAnimatorDisorder, imageAnimatorBottom, imageAnimator);
            animatorSet.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check patient's gender and set image resources.
     */
    private void setGenderImage() {
        if (patientData.getPatient_sex().equalsIgnoreCase("male")) {
            ivGender.setImageDrawable(getResources().getDrawable(R.drawable.img_male));
        } else {
            ivGender.setImageDrawable(getResources().getDrawable(R.drawable.img_female));
        }
    }

    //This method check percentage and set as a background.
    private void setBackGroundColor() {
        if (patientData.getPercentage() <= 15) {
            setcolor(getResources().getColor(R.color.green));
        } else if (patientData.getPercentage() >= 75) {
            setcolor(getResources().getColor(R.color.red));
        } else {
            setcolor(getResources().getColor(R.color.yellow));
        }
        setContactDataInVIew();
    }

    @OnClick(R.id.btnBookmark)
    public void onViewClicked() {
        patientData.setBookmarked(!patientData.isBookmarked());
        dbHandler.updateBookmarked(String.valueOf(patientData.isBookmarked()), String.valueOf(patientData.getPatient_id()));
        buttonVisibelitySet();
    }

    /**
     * Method to filter percentage color.
     *
     * @param i i is integer color.
     */
    private void setcolor(int i) {
        Drawable shape = getResources().getDrawable(R.drawable.drawable_circulwhite);
        shape.mutate().setColorFilter(i, PorterDuff.Mode.MULTIPLY);
        tvParsentage.setBackground(shape);
        //ivGestureColor.setImageDrawable(shape);
    }

    /**
     * Method execute with for loop and create view dynamically.
     */
    private void setContactDataInVIew() {

        for (int i = 0; i < 4; i++) {
            View contectListView = getLayoutInflater().inflate(R.layout.item_disorder, llDeces, false);
            TextView tvKey = (TextView) contectListView.findViewById(R.id.tvKey);
            TextView tvValue = (TextView) contectListView.findViewById(R.id.tvValue);
            switch (i) {
                case 0:
                    tvKey.setText("Male");
                    boolean isMale = patientData.getPatient_sex().equalsIgnoreCase("male");
                    tvValue.setText(String.valueOf(isMale));
                    break;
                case 1:
                    tvKey.setText("Age < 15");
                    tvValue.setText(String.valueOf(patientData.isUnderAge()));
                    break;
                case 2:
                    tvKey.setText("IsMigraine");
                    tvValue.setText(String.valueOf(patientData.isHasDisorder()));
                    break;
                case 3:
                    tvKey.setText("hallucinogenic");
                    tvValue.setText(String.valueOf(patientData.isHasDrug()));
                    break;
            }


            llDeces.addView(contectListView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Activity transition
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
