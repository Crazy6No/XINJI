package com.example.xinbookkeeping.ui.user;

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
import com.example.xinbookkeeping.bean.PayBean;
import com.example.xinbookkeeping.bean.StaffBean;
import com.example.xinbookkeeping.bean.UserBean;
import com.example.xinbookkeeping.common.SPConstant;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.PaySqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.ui.common.SelectRoleActivity;
import com.example.xinbookkeeping.uibase.BaseMainActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

/**
 *
 * 用户主页
 * */
public class UserMainActivity extends BaseMainActivity {
    private TextView mTvSayHello, mTvUserInfo, mTvCompany;
    private UserSqLiteHelper mUserHelper;

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
        mUserHelper = new UserSqLiteHelper(this);

        DrawerLayout dl_root = findViewById(R.id.dl_root);
        findViewById(R.id.iv_menu).setOnClickListener(v -> dl_root.openDrawer(GravityCompat.START));

        findViewById(R.id.iv_close).setOnClickListener(v -> dl_root.close());

        // 信息管理
        findViewById(R.id.tv_info).setOnClickListener(v -> {
            startActivity(UserInfoActivity.class);
        });

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
        // 用户信息
        mTvUserInfo = findViewById(R.id.tv_user_info);

        // 绑定公司
        mTvCompany = findViewById(R.id.tv_company);
        mTvCompany.setOnClickListener(v -> {
            if (mTvCompany.getText().equals("取消绑定")) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否确认与公司解除绑定")
                        .setPositiveButton("确定", (dialogInterface, i) -> {
                            int userId = UserDataManager.getInstance().getId();
                            CompanySqLiteHelper companySqLiteHelper = new CompanySqLiteHelper(this);
                            StaffBean staffBean = companySqLiteHelper.queryOne(String.valueOf(userId), this);
                            companySqLiteHelper.delete(staffBean.getId());
                            mUserHelper.updateCompanyId(String.valueOf(userId), "");
                            onResume();
                        })
                        .setNeutralButton("取消", (dialogInterface, i) -> {

                        }).create();

                dialog.show();
            } else {
                startActivity(UserRequestActivity.class);
            }
        });

        // 加载分页
        mTabLayout = findViewById(R.id.tl);
        mViewPage = findViewById(R.id.vp);
        final String[] tabs = new String[]{"公司信息", "账簿", "可视化"};
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
                        fragment = UserCompanyFragment.getInstance();
                        break;
                    case 1:
                        fragment = UserRecordFragment.getInstance();
                        break;
                    default:
                        fragment = UserChartFragment.getInstance();
                        break;
                }
                return fragment;
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        });
        mMediator = new TabLayoutMediator(mTabLayout, mViewPage, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabs[position]);
            }
        });
        // 要执行这一句才是真正将两者绑定起来
        mMediator.attach();
        // 设置默认起始页
        mViewPage.setCurrentItem(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = UserDataManager.getInstance().getId();
        UserBean userBean = mUserHelper.queryById(this, id);
        if (userBean == null) {
            mTvSayHello.setText("-");
        } else {
            mTvSayHello.setText("你好！" + userBean.getNickname());
            mTvUserInfo.setText("用户名：" + userBean.getUid() + "\nID：" + userBean.getId());

            String companyId = userBean.getCompanyId();
            if (companyId != null) {
                mTvCompany.setText("取消绑定");
                mTvCompany.setTag(companyId);
            } else {
                mTvCompany.setText("绑定公司");
            }
        }
    }

    @Override
    protected void onDestroy() {
        mMediator.detach();
        super.onDestroy();
    }
}
