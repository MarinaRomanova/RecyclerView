package com.example.recyclerview;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.recyclerview.recycleAdapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_popular,
            R.drawable.ic_favorite
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);
        setUpViewPager(viewPager);


        viewPager.setCurrentItem(getIntent().getIntExtra("tab", 1));
        // Find the tab layout that shows the tabs
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TopTenBestsellersFragment(), getString(R.string.top_10));
        adapter.addFragment(new FavoritesFragment(), getString(R.string.favorites));
        viewPager.setAdapter(adapter);
    }

    public void searchBooks(View view) {
        Intent intent = new Intent(this, GoogleBookActivity.class);
        this.startActivity(intent);

    }
}
