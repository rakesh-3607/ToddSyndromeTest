package com.testcompany.ui.base.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.testcompany.R;
import com.testcompany.datalayer.models.PatientData;
import com.testcompany.datalayer.models.PatientModel;
import com.testcompany.datalayer.retrofit.APIResponseInterface;
import com.testcompany.datalayer.retrofit.APIService;
import com.testcompany.datalayer.retrofit.ApiData;
import com.testcompany.datalayer.retrofit.BaseAPIClass;
import com.testcompany.datalayer.retrofit.RetrofitProvider;
import com.testcompany.datalayer.storage.DBHandler;
import com.testcompany.ui.base.adapters.PatientsListAdapter;
import com.testcompany.utils.StaticUtils;
import com.testcompany.utils.recyclerview.CustomRecyclerView;
import com.testcompany.utils.recyclerview.Paginate;
import com.testcompany.utils.view.CustomButton;
import com.testcompany.utils.view.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

public class PatientsListActivity extends BaseActivity implements APIResponseInterface, Paginate.Callbacks {

    private static final int PATIENT_API_CALL = 101;

    @BindView(R.id.rvPatients)              CustomRecyclerView rvPatients;
    @BindView(R.id.swipeRefreshLayout)      SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.ivEmptyImage)            ImageView ivEmptyImage;
    @BindView(R.id.tvEmptyTitle)            CustomTextView tvEmptyTitle;
    @BindView(R.id.btnEmpty)                CustomButton btnEmpty;
    @BindView(R.id.tvEmptyDescription)      CustomTextView tvEmptyDescription;
    @BindView(R.id.llEmptyViewMain)         LinearLayout llEmptyViewMain;

    Call<BaseAPIClass> patientApiCall ;
    public boolean loading = false;
    private boolean loadMoreIsRequired = true;
    PatientsListAdapter patientsListAdapter;
    List<PatientModel> lstPatientData = new ArrayList<>();
    DBHandler dbHandler;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_patient_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * View initialize when Activity view is created.
     */
    private void init() {
        //Actionbar setup
        setUpActionBar(getString(R.string.patient_list_screen), true);
        dbHandler = DBHandler.getInstance(PatientsListActivity.this);
        stopRefresh();
        swipeRefreshLayout.setEnabled(true);
        setupSwipeReferenceLayout();
        setAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Manage actionbar back event
                finish();
                this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method to request to get patients data from server
     */
    private void callApiToGetPatientData() {
        if (!isConnectingToInternet()) {
            //Show no internet connection message and click event
            rvPatients.setEmptydata(getString(R.string.no_connection), "",
                    getString(R.string.try_again), R.drawable.icn_no_connection, (v) -> tryAgainClicked());
            stopLoadMoreDialog();
            return;
        }

        try {
            patientsListAdapter.clearData();
            loading = true;
            loadMoreIsRequired = true;
            rvPatients.setEmptydata(getString(R.string.fetching_data_title),
                    getString(R.string.fetching_data_title), R.drawable.icn_no_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Api call
        RetrofitProvider retrofitProvider = new RetrofitProvider(PATIENT_API_CALL, 0, this);
        APIService apiService = retrofitProvider.createService(APIService.class);
        patientApiCall = apiService.getPatients();
        patientApiCall.enqueue(retrofitProvider); // retrofit async api call
    }

    /**
     * try again method execution if no connection available.
     */
    private void tryAgainClicked() {
        loadMoreIsRequired = true;
        loading = true;
        rvPatients.setListPagination(this);
        callApiToGetPatientData();
    }

    /**
     * Method to manage pull to refresh function.
     */
    private void setupSwipeReferenceLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            stopRefresh();
            if (!isConnectingToInternet()) {
                stopRefresh();
                if (patientApiCall != null) {
                    patientApiCall.cancel();
                }
                return;
            }
            callApiToGetPatientData();
        });
    }

    /**
     * Empty adapter initialize and item click event bind.
     */
    private void setAdapter() {
        patientsListAdapter = new PatientsListAdapter(lstPatientData, PatientsListActivity.this) {
            @Override
            public void onItemClick(int position, PatientsHolder holder) {
                navigateToDetailScreen(position, holder);
            }
        };

        rvPatients.setEmptyView(llEmptyViewMain, swipeRefreshLayout);
        rvPatients.setAdapter(patientsListAdapter);
        rvPatients.setListPagination(this);

    }

    /**
     * Method to navigate detail screen
     * @param position list position
     * @param holder object of adapter holder class to manage appCompacttransition animation
     */
    private void navigateToDetailScreen(int position, PatientsListAdapter.PatientsHolder holder) {
        Intent intent = new Intent(this,PatientDetailsActivity.class);
        intent.putExtra("isApicall", true);
        intent.putExtra("data",lstPatientData.get(position));
        navigateToDifferentScreen(intent,false,R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    public void onResponseSuccess(ApiData data, Response<BaseAPIClass> response, int requestCode, int nextRequestCode) {

        switch (requestCode) {
            case PATIENT_API_CALL:
                lstPatientData = data.getData(PatientData.class).get(0).getPatient_list();
                if (lstPatientData.size() > 0) {
                    for (int i = 0; i < lstPatientData.size(); i++) {
                        PatientModel patientList = lstPatientData.get(i);
                        //Function to calculate todd's probability
                        patientList = StaticUtils.calculateToddProbability(patientList);
                        lstPatientData.set(i, patientList);
                    }

                    if (dbHandler.getAllDataFromDetails().size() == 0) {
                        new InsertData().execute();
                    }
                }
                //stop Load more function after getting response from server
                loading = false;
                loadMoreIsRequired = false;
                stopRefresh();
                updateListAdapter();
                break;
        }

    }

    private class InsertData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            insertDataToDataBase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    /**
     * Data insertion in database
     */
    private void insertDataToDataBase() {
        dbHandler.insertBulkDataInPatientList(lstPatientData);

    }

    @Override
    public void onResponseFailed(Call<BaseAPIClass> call, Throwable t, int requestCode, int nextRequestCode) {
        switch (requestCode) {
            case PATIENT_API_CALL:
                stopRefresh();
                stopLoadMoreDialog();
                loading = false;
                rvPatients.setEmptydata(getString(R.string.failed_response_title), "",
                        getString(R.string.try_again), R.drawable.icn_no_data, (v) -> tryAgainClicked());
                break;
        }
    }

    public void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Method to update data in adapter
     */
    private void updateListAdapter() {
        if (patientsListAdapter.getData().size() > 0) {
            patientsListAdapter.clearData();
        }
        patientsListAdapter.updateList(lstPatientData);
    }

    @Override
    public void onLoadMore() {
        if (loadMoreIsRequired) {
            loading = true;
            callApiToGetPatientData();
        }
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return needLoadMore();
    }

    @Override
    public boolean stopLoading() {
        return false;
    }

    private boolean needLoadMore() {
        //If you want to load more subIndicatorDetail
        return !loadMoreIsRequired;
    }

    private void stopLoadMoreDialog() {
        loading = false;
        loadMoreIsRequired = false;
        try {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                }

                @Override
                public boolean isLoading() {
                    return true;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return true;
                }

                @Override
                public boolean stopLoading() {
                    patientsListAdapter.notifyDataSetChanged();
                    return true;
                }
            };
            callbacks.stopLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}