package com.testcompany.ui.base.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.testcompany.R;
import com.testcompany.ui.base.fragments.BookMarkfragment;
import com.testcompany.ui.base.fragments.HistoryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PatientHistoryActivity extends BaseActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        initview();
        super.onResume();
    }

    /**
     * view initialize method
     */
    private void initview() {
        setUpActionBar(getString(R.string.patient_history_screen), true);
        setupViewPagerAndTabs();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Manage back button from topbar
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method to setup tab layout and viewpager
     */
    private void setupViewPagerAndTabs() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewpager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewpager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                final int POSITION_HISTORY = 0;
                final int POSITION_BOOKMARKS = 1;

                if (tab.getPosition() == POSITION_HISTORY) {
                    //Toast.makeText(AppManagerActivity.this, "Uninstall", Toast.LENGTH_SHORT).show();
                }

                if (tab.getPosition() == POSITION_BOOKMARKS) {
                    //Toast.makeText(AppManagerActivity.this, "Disable", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Initialize viewpagerAdapter and set tab  titles
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HistoryFragment(), getString(R.string.history));
        adapter.addFragment(new BookMarkfragment(), getString(R.string.bookmarks));
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
