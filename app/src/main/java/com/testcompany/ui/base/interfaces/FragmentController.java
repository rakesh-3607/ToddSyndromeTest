package com.testcompany.ui.base.interfaces;

import android.support.v4.app.Fragment;

public interface FragmentController {

    void addFragmentToRootContainer(Fragment fragment);

    void replaceRootContainerFragment(Fragment fragment);

}
