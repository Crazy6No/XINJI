package com.example.xinbookkeeping.ui.user;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.PayUserAdapter;
import com.example.xinbookkeeping.bean.PayBean;
import com.example.xinbookkeeping.bean.StaffBean;
import com.example.xinbookkeeping.bean.UserBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.PaySqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseFragment;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

/**
 * 用户公司信息
 */
public class UserCompanyFragment extends BaseFragment {

    private TextView mTvCompany, mTvPost;
    private PayUserAdapter adapter;

    private UserSqLiteHelper userSqLiteHelper;

    public static UserCompanyFragment getInstance() {
        return new UserCompanyFragment();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_company;
    }

    @Override
    protected void init() {
        super.init();
        userSqLiteHelper = new UserSqLiteHelper(getContext());
        mTvCompany = root.findViewById(R.id.tv_company);
        mTvPost = root.findViewById(R.id.tv_post);

        RecyclerView rv = root.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayUserAdapter();
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        int userId = UserDataManager.getInstance().getId();
        UserBean userBean = userSqLiteHelper.queryById(getContext(), userId);
        String companyId = userBean.getCompanyId();
        if (companyId != null) {
            // 公司信息
            mTvCompany.setText(userBean.getUnitName());
            CompanySqLiteHelper companySqLiteHelper = new CompanySqLiteHelper(getContext());
            StaffBean staffBean = companySqLiteHelper.queryOne(String.valueOf(userId), getContext());
            mTvPost.setText(staffBean.getPost());
            // 最近薪资
            PaySqLiteHelper paySqLiteHelper = new PaySqLiteHelper(getContext());
            List<PayBean> list = paySqLiteHelper.queryByUserId(String.valueOf(userId), companyId);
            adapter.setNewData(list);
        } else {
            mTvCompany.setText("-");
            mTvPost.setText("-");
            adapter.setNewData(null);
        }
    }
}
