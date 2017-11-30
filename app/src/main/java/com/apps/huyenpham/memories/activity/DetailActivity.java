package com.apps.huyenpham.memories.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.adapter.DetailPagerAdapter;
import com.apps.huyenpham.memories.fragment.FragmentNote;
import com.apps.huyenpham.memories.fragment.FragmentPhoto;

public class DetailActivity extends AppCompatActivity {
    TabLayout layoutDetail;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        layoutDetail = (TabLayout) findViewById(R.id.detail_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        DetailPagerAdapter adapter = new DetailPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentPhoto(), "Photo");
        adapter.addFragment(new FragmentNote(), "Note");
        viewPager.setAdapter(adapter);
        layoutDetail.setupWithViewPager(viewPager);
    }
}
