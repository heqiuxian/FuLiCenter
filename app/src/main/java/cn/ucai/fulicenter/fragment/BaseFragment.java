package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.e("BaseFragment");
        initView();
        initData();
        setListener();
        return inflater.inflate(R.layout.fragment_base, container, false);
    }
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void setListener();

}
