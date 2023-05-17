package com.example.xinbookkeeping.ui.company;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.CompanyBean;
import com.example.xinbookkeeping.common.SPConstant;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.ui.common.SelectRoleActivity;
import com.example.xinbookkeeping.ui.user.UserCompanyFragment;
import com.example.xinbookkeeping.ui.user.UserInfoActivity;
import com.example.xinbookkeeping.uibase.BaseMainActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * 公司主页
 */
public class CompanyMainActivity extends BaseMainActivity {
    private TextView mTvSayHello, mTvUserInfo;
    private CompanySqLiteHelper helper;

    private TabLayout mTabLayout;
    private TabLayoutMediator mMediator;
    private ViewPager2 mViewPage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_main;
    }

    @Override
    public void init() {
        super.init();
        helper = new CompanySqLiteHelper(this);

        DrawerLayout dl_root = findViewById(R.id.dl_root);
        findViewById(R.id.iv_menu).setOnClickListener(v -> dl_root.openDrawer(GravityCompat.START));

        findViewById(R.id.iv_close).setOnClickListener(v -> dl_root.close());

        // 信息管理
        findViewById(R.id.tv_info).setOnClickListener(v -> {
            startActivity(CompanyInfoActivity.class);
        });

        // 处理申请
        findViewById(R.id.tv_company).setOnClickListener(v -> startActivity(CompanyRequestActivity.class));

        // 退出登录
        findViewById(R.id.tv_out_login).setOnClickListener(v -> {

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("退出登录后将会清空本地缓存")
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        SharedPreferences common = getSharedPreferences(SPConstant.COMMON, MODE_PRIVATE);
                        SharedPreferences.Editor editor = common.edit();
                        editor.clear();
                        editor.commit();
                        startActivity(SelectRoleActivity.class);
                        finish();
                    })
                    .setNeutralButton("取消", (dialogInterface, i) -> {

                    }).create();

            dialog.show();
        });

        // 欢迎
        mTvSayHello = findViewById(R.id.tv_say_hello);
        // 公司信息
        mTvUserInfo = findViewById(R.id.tv_user_info);
        // 加载分页
        mTabLayout = findViewById(R.id.tl);
        mViewPage = findViewById(R.id.vp);
        final String[] tabs = new String[]{"员工", "工资条编辑", "图表"};
        // 禁用预加载
        mViewPage.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        // Adapter
        mViewPage.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment fragment;
                switch (position) {
                    case 0:
                        fragment = CompanyStaffFragment.getInstance();
                        break;
                    case 1:
                        fragment = CompanyPaySlipFragment.getInstance();
                        break;
                    default:
                        fragment = CompanyChartFragment.getInstance();
                        break;
                }
                return fragment;
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        });
        mMediator = new TabLayoutMediator(mTabLayout, mViewPage, (tab, position) -> tab.setText(tabs[position]));
        // 要执行这一句才是真正将两者绑定起来
        mMediator.attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = UserDataManager.getInstance().getId();
        CompanyBean bean = helper.queryById(id);
        if (bean == null) {
            mTvSayHello.setText("-");
        } else {
            mTvSayHello.setText("你好！" + bean.getUnitname());
            mTvUserInfo.setText("用户名：" + bean.getUid() + "\nID：" + bean.getId());
        }
    }

    @Override
    protected void onDestroy() {
        mMediator.detach();
        super.onDestroy();
    }
}
