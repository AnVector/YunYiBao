package com.anyihao.ayb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/9 0009.
 */

public class UTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public UTabAdapter(FragmentManager fm, List<Fragment> mFragments, List<String> mTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments == null || mFragments.size() <= position)
            return null;
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return (mFragments == null ? 0 : mFragments.size());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles == null || mTitles.size() <= position)
            return null;
        return mTitles.get(position);
    }
}
