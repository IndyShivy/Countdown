package com.indyinc.countdown;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

public class AddAdapt extends PagerAdapter {

    private String[] data = new String[]{"Day", "Week", "Fortnight", "Month"};
    private int currentPosition = 0;

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView textView = new TextView(container.getContext());
        textView.setText(data[position]);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        container.addView(textView);
        return textView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getFormat() {
        //return the string of the current format that is selected
        return data[currentPosition];
    }
}

