package com.kudosku.falling;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FirstGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstguide);

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new Pager(this));

        pager.requestTransparentRegion(pager);
    }

    class Pager extends PagerAdapter implements View.OnClickListener {

        private LayoutInflater inflater;

        public Pager(Context context) {
            super();
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 5;
        }

        public Object instantiateItem(View p, int pos) {
            View v = null;

            switch (pos) {
                case 0:
                    v = inflater.inflate(R.layout.guide1,null);
                    break;
                case 1:
                    v = inflater.inflate(R.layout.guide2,null);
                    break;
                case 2:
                    v = inflater.inflate(R.layout.guide3,null);
                    break;
                case 3:
                    v = inflater.inflate(R.layout.guide4,null);
                    break;
                case 4:
                    v = inflater.inflate(R.layout.guide5,null);
                    Button button = (Button)v.findViewById(R.id.guidebutton);
                    button.setOnClickListener(this);
                    break;
            }

            ((ViewPager) p).addView(v, null);

            return v;
        }

        public void destroyItem(View p, int pos, Object view) {
            ((ViewPager)p).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.guidebutton:
                    finish();
            }

        }
    }
}
