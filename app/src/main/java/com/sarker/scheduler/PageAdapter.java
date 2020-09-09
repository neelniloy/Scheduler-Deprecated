package com.sarker.scheduler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int numoftab;
    public PageAdapter(@NonNull FragmentManager fm,int numofTab) {
        super(fm);

        this.numoftab = numofTab;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position)
        {
            case 0 :
                return new Saturday();

            case 1 :
                return new Sunday();

            case 2 :
                return new Monday();

            case 3 :
                return new Tuesday();

            case 4 :
                return new Wednesday();

            case 5 :
                return new Thursday();

                default:
                    return null;
        }


    }

    @Override
    public int getCount() {
        return numoftab;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
