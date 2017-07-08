package com.testcompany.ui.base.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.testcompany.R;
import com.testcompany.dagger.BaseInjectingFragment;
import com.testcompany.ui.base.interfaces.FragmentController;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragments extends BaseInjectingFragment {

    Unbinder unbinder;
    private FragmentController fragmentController;

    public abstract Integer layoutId();

    @Override
    public void onAttach(final Context activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentController) {
            fragmentController = (FragmentController) activity;
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layoutId() == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        View view = inflater.inflate(layoutId(), container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    protected void addFragment(final Fragment fragment) {
        fragmentController.addFragmentToRootContainer(fragment);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public boolean onBackPressed(){
        return false;
    }


    public void showToastForShortTime(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToastForLongTime(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void navigateToDifferentScreen(Intent nextScreenIntent){
        navigateToDifferentScreen(nextScreenIntent,null,"",false,false,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,View view,String sharedElementName,boolean finishActivity){
        navigateToDifferentScreen(nextScreenIntent,view,sharedElementName,false,finishActivity,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,View view,String sharedElementName){
        navigateToDifferentScreen(nextScreenIntent,view,sharedElementName,false,false,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,boolean finishActivity){
        navigateToDifferentScreen(nextScreenIntent,null,"",false,finishActivity,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,boolean finishActivity,int startAnimation,int endAnimation){
        navigateToDifferentScreen(nextScreenIntent,null,"",true,finishActivity,startAnimation,endAnimation);

    }

    /**
     * Common method to navigate in different activity class
     * @param nextScreenIntent object of Intent
     * @param view view which will be animate
     * @param sharedElementName transition name
     * @param isAnimate boolean for screen animation
     * @param finishActivity boolean set true if want to finish current activity
     * @param startAnimation this is start animation use full if isAnimate is true
     * @param endAnimation this is end animation use full if isAnimate is true
     */
    public void navigateToDifferentScreen(Intent nextScreenIntent,View view,String sharedElementName,
                                          boolean isAnimate,boolean finishActivity,int startAnimation,int endAnimation){

        try {
            if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), view, sharedElementName);
                startActivity(nextScreenIntent, options.toBundle());
                if (finishActivity){
                    getActivity().finish();
                }
            }
            else {
                startActivity(nextScreenIntent);
                if (isAnimate){
                    getActivity().overridePendingTransition(startAnimation,endAnimation);
                }
                if (finishActivity){
                    getActivity().finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to show snack bar message with one button for short time
     * @param view  main view group
     * @param message string which is to be displayed
     */
    public void showSnackBarForShortTime(View view,String message){
        showSnackBar(view,message,true);

    }

    /**
     * Method to show snack bar message with one button for long time
     * @param view  main view group
     * @param message string which is to be displayed
     */
    public void showSnackBarForLongTime(View view,String message){
        showSnackBar(view,message,false);
    }
    private void showSnackBar(View view,String message,boolean isShortTime){
        try {
            int timeDuration = 0;
            if (isShortTime){
                timeDuration = Snackbar.LENGTH_SHORT;
            }else{
                timeDuration = Snackbar.LENGTH_LONG;
            }
            Snackbar snackbar = Snackbar.make(view, message, timeDuration);
            snackbar.setAction(R.string.ok, v -> {

            });
            snackbar.setActionTextColor(getResources().getColor(R.color.gray_medium));
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(3);
            snackbarView.setBackgroundColor(getResources().getColor(R.color.black_transparent_70));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
