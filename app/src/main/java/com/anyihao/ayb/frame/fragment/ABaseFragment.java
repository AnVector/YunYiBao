package com.anyihao.ayb.frame.fragment;


import android.support.v4.app.Fragment;

import com.anyihao.androidbase.fragment.BKBaseFragment;
import com.anyihao.androidbase.mvp.IView;
import com.anyihao.ayb.common.PresenterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ABaseFragment extends BKBaseFragment implements IView<Integer> {

    @Override
    public void onDestroy() {
        super.onDestroy();
        PresenterFactory.getInstance().remove(this);
    }
}
