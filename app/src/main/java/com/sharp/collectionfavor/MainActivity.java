package com.sharp.collectionfavor;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.adapter.ViewPagerStateAdapter;
import com.sharp.fragments.AccountFragment;
import com.sharp.fragments.DiscoverFragment;
import com.sharp.fragments.HomeFragment;
import com.sharp.util.ToolUtil;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    public static final LinearOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private FloatingActionButton mFloatingBtn;

    private RadioGroup mRadioGroup;
    private RadioButton mRBtn1, mRBtn2, mRBtn3;

    private LinearLayout mLL;
    private RelativeLayout mRL;
    private TextView mTopLebal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initDatas();
        initView();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {

        mLL = (LinearLayout) findViewById(R.id.main_ll);
//        mLL.setVisibility(View.GONE);
        mRL = (RelativeLayout) findViewById(R.id.main_top_search);
        mTopLebal = (TextView) findViewById(R.id.main_top_lebal);
        setHeaderShow(0);

        mFloatingBtn = (FloatingActionButton) findViewById(R.id.main_floatingBtn);
        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCollection.class);
                startActivity(intent);
            }
        });

        mRadioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        mRadioGroup.setOnCheckedChangeListener(this);

        mRBtn1 = (RadioButton) findViewById(R.id.rb_home);
        mRBtn2 = (RadioButton) findViewById(R.id.rb_direct_seeding);
        mRBtn3 = (RadioButton) findViewById(R.id.rb_me);

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        ViewPagerStateAdapter adapter = new ViewPagerStateAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    if (mFloatingBtn.getVisibility() == View.GONE || mFloatingBtn.getVisibility() == View.INVISIBLE){
                        mFloatingBtn.setVisibility(View.VISIBLE);
                        ViewCompat.animate(mFloatingBtn)
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .alpha(1.0f)
                                .setDuration(400)
                                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                                .start();
                    }
//                    mFloatingBtn.setVisibility(View.VISIBLE);
                    setHeaderShow(0);
                    mRBtn1.setChecked(true);
                }else if(position == 1){
                    if (mLL.getVisibility() == View.GONE){
                        mLL.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Gone here"+mLL.getMeasuredHeight()+"=="+mLL.getX()+"==="+mLL.getY(), Toast.LENGTH_SHORT).show();
                        /*LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLL.getLayoutParams();
                        lp.height = mLL.getMeasuredHeight();
                        mLL.setLayoutParams(lp);*/
                    }
                    if (mRadioGroup.getVisibility() == View.INVISIBLE){
                        show(mRadioGroup);
                    }
                    mFloatingBtn.setVisibility(View.INVISIBLE);
                    setHeaderShow(1);
                    mTopLebal.setText("发 现");
                    mRBtn2.setChecked(true);
                }else if (position == 2){

                    if (mRadioGroup.getVisibility() == View.INVISIBLE){
                        show(mRadioGroup);
                    }
                    mFloatingBtn.setVisibility(View.INVISIBLE);
                    setHeaderShow(2);
                    mTopLebal.setText("个人信息");
                    mRBtn3.setChecked( true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRBtn1.setChecked(true);
    }

    private void initDatas(){
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new DiscoverFragment());
        mFragments.add(new AccountFragment());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_home:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rb_direct_seeding:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rb_me:
                mViewPager.setCurrentItem(2);
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setHeaderShow(int tag){
        if (tag == 0){
            mRL.setVisibility(View.VISIBLE);
            mLL.setVisibility(View.GONE);
            mRL.setNestedScrollingEnabled(true);
            mLL.setNestedScrollingEnabled(true);
        }else{
            mRL.setVisibility(View.GONE);
            mLL.setVisibility(View.VISIBLE);
            mRL.setNestedScrollingEnabled(false);
            mLL.setNestedScrollingEnabled(false);
        }
    }

    private void hide(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(view.getHeight()).
                setInterpolator(INTERPOLATOR).setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                show(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private void show(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(0).
                setInterpolator(INTERPOLATOR).
                setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                hide(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }


    /*mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    TestRecycleViewAdapter adapter = new TestRecycleViewAdapter(this, datas);
    mRecyclerView.setAdapter(adapter);*/


}
