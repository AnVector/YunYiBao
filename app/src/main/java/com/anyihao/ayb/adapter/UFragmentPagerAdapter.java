package com.anyihao.ayb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/9 0009.
 */

public class UFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public UFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments == null || mFragments.isEmpty())
            return null;
        return
                mFragments.get(position);
    }

    @Override
    public int getCount() {
        return (mFragments == null ? 0 : mFragments.size());
    }
}
