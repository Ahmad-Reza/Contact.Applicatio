package com.example.contactapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.contactapplication.R;
import com.example.contactapplication.adapter.ViewPagerAdapter;
import com.example.contactapplication.fragment.ContactFragment;
import com.example.contactapplication.fragment.MessageFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.material_toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.contact_app_label));

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        TextView contactTab = findViewById(R.id.contact_tab_view);
        TextView messageTab = findViewById(R.id.message_tab_view);

        ViewPager2 viewPager = findViewById(R.id.viewpager_view);
        viewPager.setAdapter(new ViewPagerAdapter(this, getFragments()));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    contactTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_select));
                    contactTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.white));

                    messageTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_deselected));
                    messageTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.textColor));

                } else if (position == 1) {
                    messageTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_select));
                    messageTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.white));

                    contactTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_deselected));
                    contactTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.textColor));
                }
            }
        });

        View.OnClickListener listener = view -> {
            if (view.getId() == R.id.contact_tab_view) {
                contactTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_select));
                contactTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.white));

                messageTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corners));
                messageTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.textColor));

                viewPager.setCurrentItem(0, true);

            } else if (view.getId() == R.id.message_tab_view) {
                messageTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_select));
                messageTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.white));

                contactTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corners));
                contactTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.textColor));
                viewPager.setCurrentItem(1, true);
            }
        };

        contactTab.setOnClickListener(listener);
        messageTab.setOnClickListener(listener);

        FloatingActionButton dialPadBtn = findViewById(R.id.dialpad_view);
        dialPadBtn.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            startActivity(dialIntent);
        });
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ContactFragment());
        fragments.add(new MessageFragment());

        return fragments;
    }
}