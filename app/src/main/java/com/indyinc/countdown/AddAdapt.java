package com.indyinc.countdown;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;

public class AddAdapt extends PagerAdapter {

    private final String[] data = new String[]{"Day", "Week", "Fortnight", "Month"};
    private int currentPosition = 0;
    private final Context context;

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    public AddAdapt(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView textView = new TextView(container.getContext());
        textView.setText(data[position]);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        Typeface customFont = ResourcesCompat.getFont(context,R.font.viga);
        //get the custom font and set it as the font for the textview
        textView.setTypeface(customFont);
        container.addView(textView);
        return textView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
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

