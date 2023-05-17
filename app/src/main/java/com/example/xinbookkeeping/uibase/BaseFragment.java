package com.example.xinbookkeeping.uibase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(getLayoutId(), container, false);
        }
        init();
        return root;
    }

    protected void init() {

    }

    @LayoutRes
    public abstract int getLayoutId();

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        startActivity(intent);
    }

    /**
     * 吐司
     * */
    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
