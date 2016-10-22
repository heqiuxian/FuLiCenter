package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;

    CategoryAdapter mAdapter;
    MainActivity mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    int groupCount=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        mContext= (MainActivity) getContext();
        mGroupList=new ArrayList<>();
        mChildList=new ArrayList<ArrayList<CategoryChildBean>>();
        initData();
        initView();
        setListener();
//        mAdapter=new CategoryAdapter(mContext,mGroupList,mChildList);
        return layout;
    }


    private void initView() {
        elvCategory.setGroupIndicator(null);
//        elvCategory.setAdapter(mAdapter);
    }


    private void initData() {
        downloadGroup();
    }

    private void downloadGroup() {
        L.e("我在这 ");
        NetDao.downloadCategoryGroup(mContext, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if(result!=null&&result.length>0){
                    if(result!=null&&result.length>0){
                        ConvertUtils.array2List(result);
                        ArrayList<CategoryGroupBean> groupList=ConvertUtils.array2List(result);
                        mGroupList.addAll(groupList);
                        L.e("groupList="+groupList.get(0).toString());
                        for (int i=0;i<groupList.size();i++){
                            mChildList.add(new ArrayList<CategoryChildBean>());
                            CategoryGroupBean g=groupList.get(i);
                            downloadChild(g.getId(),i);
                        }
                    }
                }
                mAdapter=new CategoryAdapter(mContext,mGroupList,mChildList);
                elvCategory.setAdapter(mAdapter);
            }

            @Override
            public void onError(String error) {

            }
        });
       /* L.e("Category","mGroup="+mGroupList.get(0).toString());
        L.e("Category",",child="+mChildList.toString());*/

    }

    private void downloadChild(int id, final int index) {
        NetDao.downloadCategoryChild(mContext, id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;
                if(result!=null&&result.length>0){
                    ArrayList<CategoryChildBean> childList=ConvertUtils.array2List(result);
                    mChildList.set(index,childList);
                    L.e("groupList"+"childList.size"+mChildList.get(0).toString());
                }
                if(groupCount==mGroupList.size()){
                    mAdapter.initData(mGroupList,mChildList);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void setListener() {

    }

}
