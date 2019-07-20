package com.example.administrator.whyapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public
class tabacessoradaptor extends FragmentPagerAdapter {
    public
    tabacessoradaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public
    Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                ChatFragment chatFragment=new ChatFragment();
                return chatFragment;
            case 1:
                groupFragment group=new groupFragment();
                return group;
            case 2:
                ContactFragment contactFragment=new ContactFragment();
                return contactFragment;
                default:
                    return null;
        }

    }

    @Override
    public
    int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public
    CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Chats";
            case 1:
                return "Groups";
            case 2:
                return "Contact";
                default:
                    return null;
        }
    }
}
