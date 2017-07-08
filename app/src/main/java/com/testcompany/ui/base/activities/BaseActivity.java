package com.testcompany.ui.base.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.testcompany.BuildConfig;
import com.testcompany.R;
import com.testcompany.dagger.BaseInjectingActivity;
import com.testcompany.ui.BaseApplication;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends BaseInjectingActivity{

    protected abstract Integer getLayoutId();

    public Context context;
    Unbinder unbinder;


    //List of current running activities.
    //Do not forget to extend your activity to BaseActivity then only this logic will work.
    public static ArrayList<String> runningActivity = new ArrayList<>();
    public static final long BACKGROUND_CHECK_DELAY = 1000L;// Perform background/Foreground thread after 2 second

    public static Handler backgroundHandler = new Handler();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() == null) {
            return;
        }
        setContentView(getLayoutId());
        //we are importing unbinber and binder in base activity.
        // so no need to import it in child classes.
        unbinder = ButterKnife.bind(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        context = this;
        activityDidForeground();
    }



    public Runnable backgroundRunnable = () -> {
        if (runningActivity.size() == 0) {
            BaseApplication.isAppWentToBg = true;
            //           Toast.makeText(context, "App is Going to Background", Toast.LENGTH_SHORT).show();
        } else if (BaseApplication.isAppWentToBg) {
            BaseApplication.isAppWentToBg = false;
            //       Toast.makeText(context, "App is came to Foreground", Toast.LENGTH_SHORT).show();

        }

    };

    private void activityDidForeground() {
        runningActivity.add(this.getClass().getName());
        backgroundHandler.removeCallbacks(backgroundRunnable);
        backgroundHandler.postDelayed(backgroundRunnable, BACKGROUND_CHECK_DELAY);
    }


    public  boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = null;
        try {
            connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivityManager.getAllNetworks();
                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            } else {

                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Utils", "isConnectingToInternet: NETWORKNAME=" + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private void activityDidBackground() {
        runningActivity.remove(this.getClass().getName());
        backgroundHandler.removeCallbacks(backgroundRunnable);
        backgroundHandler.postDelayed(backgroundRunnable, BACKGROUND_CHECK_DELAY);
    }

    /**
     * Method to show toast for short time duration with release build flag.
     * @param message message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     */
    public void showToastForShortTime(String message,boolean showInReleaseBuild){
        showToastForLongTime(message,showInReleaseBuild,Toast.LENGTH_SHORT);
    }

    /**
     * Method to show toast for short time duration.
     * @param message message which will be shown in toast.
     */
    public void showToastForShortTime(String message){
        showToastForLongTime(message,false,Toast.LENGTH_SHORT);
    }

    /**
     * Method to show toast for long time duration with release build flag.
     * @param message message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     */
    public void showToastForLongTime(String message,boolean showInReleaseBuild){
        showToastForLongTime(message,showInReleaseBuild,Toast.LENGTH_LONG);
    }

    /**
     * Method to show toast for long time duration.
     * @param message message which will be shown in toast.
     */
    public void showToastForLongTime(String message){
        showToastForLongTime(message,false,Toast.LENGTH_LONG);
    }

    /**
     * Method to show toast.
     * @param message message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     * @param toastLength length of Toast duration. Toast.LENGTH_LONG/Toast.LENGTH_SHORT
     */
    public void showToastForLongTime(String message,boolean showInReleaseBuild,int toastLength){
        if (!showInReleaseBuild) {
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, message, toastLength).show();
            }
        }else{
            Toast.makeText(this, message, toastLength).show();
        }

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
    public void navigateToDifferentScreen(Intent nextScreenIntent, View view, String sharedElementName,
                                          boolean isAnimate, boolean finishActivity, int startAnimation, int endAnimation){

        try {
            if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, view, sharedElementName);
                startActivity(nextScreenIntent, options.toBundle());
                if (finishActivity){
                    finish();
                }
            }
            else {
                startActivity(nextScreenIntent);
                if (isAnimate){
                    overridePendingTransition(startAnimation,endAnimation);
                }
                if (finishActivity){
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            //snackbar.setActionTextColor(getResources().getColor(R.color.gray_medium));
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(3);
            //snackbarView.setBackgroundColor(getResources().getColor(R.color.black_transparent_70));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUpActionBar(String screenName, boolean isBackVisible){
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            if (isBackVisible) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            getSupportActionBar().setTitle(screenName);
            getSupportActionBar().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        activityDidBackground();

    }

    private void finishActivity(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                supportFinishAfterTransition();
            }
            else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

}
