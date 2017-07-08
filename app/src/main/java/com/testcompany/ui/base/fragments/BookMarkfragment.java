package com.testcompany.ui.base.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.testcompany.R;
import com.testcompany.datalayer.models.PatientModel;
import com.testcompany.datalayer.storage.DBHandler;
import com.testcompany.ui.base.activities.PatientDetailsActivity;
import com.testcompany.ui.base.adapters.PatientsListAdapter;
import com.testcompany.utils.recyclerview.CustomRecyclerView;
import com.testcompany.utils.recyclerview.Paginate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BookMarkfragment extends BaseFragments implements Paginate.Callbacks {


    @BindView(R.id.rvPatients)
    CustomRecyclerView rvPatients;
    @BindView(R.id.llEmptyViewMain)
    LinearLayout llEmptyViewMain;

    public boolean loading = false;
    private boolean loadMoreIsRequired = true;
    PatientsListAdapter patientsListAdapter;
    List<PatientModel> lstPatientData = new ArrayList<>();

    @Override
    public Integer layoutId() {
        return R.layout.fragment_history;
    }

    DBHandler dbHandler;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHandler = DBHandler.getInstance(getContext());
        initView();
    }

    @Override
    public void onResume() {
        loading = true;
        getDataFromDB();
        super.onResume();
    }

    private void initView() {
        setAdapter();
    }

    /**
     * Method to set empty adapter
     */
    private void setAdapter() {
        patientsListAdapter = new PatientsListAdapter(lstPatientData, getActivity()) {
            @Override
            public void onItemClick(int position, PatientsHolder holder) {
                navigateToDetailScreen(position, holder);
            }
        };

        rvPatients.setEmptyView(llEmptyViewMain, null);
        rvPatients.setAdapter(patientsListAdapter);
        rvPatients.setListPagination(this);

    }


    @Override
    public void onLoadMore() {
        if (loadMoreIsRequired) {
            loading = true;
            getDataFromDB();
        }
    }

    /**
     * Method to get data from local storage
     */
    private void getDataFromDB() {
        lstPatientData = dbHandler.getAllDataFromBookmarkedDetails();
        if (lstPatientData.size() == 0) {
            rvPatients.setEmptydata(getString(R.string.no_bookmark_found), "", R.drawable.icn_no_data);
        }
        loading = false;
        loadMoreIsRequired = false;
        updateListAdapter();
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

    /**
     * Method to navigate user to detail screen
     * @param position position on selected data in list
     * @param holder object of adapter holder class to manage appCompacttransition animation
     */
    private void navigateToDetailScreen(int position, PatientsListAdapter.PatientsHolder holder) {
        Intent intent = new Intent(getActivity(), PatientDetailsActivity.class);
        intent.putExtra("data", lstPatientData.get(position));
        navigateToDifferentScreen(intent);
    }
}
