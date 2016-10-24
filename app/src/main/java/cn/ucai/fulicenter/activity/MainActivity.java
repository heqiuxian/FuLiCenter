package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CartFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.NewGoodFragment;
import cn.ucai.fulicenter.fragment.PersonalFragment;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {


    int index;
    RadioButton[] rbs;
    @BindView(R.id.layout_new_good)
    RadioButton layoutNewGood;
    @BindView(R.id.layout_boutique)
    RadioButton layoutBoutique;
    @BindView(R.id.layout_category)
    RadioButton layoutCategory;
    @BindView(R.id.layout_cart)
    RadioButton layoutCart;
    @BindView(R.id.tvCartHint)
    TextView tvCartHint;
    @BindView(R.id.shopping_cart_num_bg)
    LinearLayout shoppingCartNumBg;
    @BindView(R.id.layout_personal_center)
    RadioButton layoutPersonalCenter;
    @BindView(R.id.main_bottom)
    LinearLayout mainBottom;
    ArrayList<Fragment> list;
    NewGoodFragment newGoodFragment;
    BoutiqueFragment boutiqueFragment;
    CategoryFragment categoryFragment;
    CartFragment cartFragment;
    PersonalFragment personalFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity onCreate");
        initView();
    }


    private void initView() {
        rbs = new RadioButton[5];
        rbs[0] = layoutNewGood;
        rbs[1] = layoutBoutique;
        rbs[2] = layoutCategory;
        rbs[3] = layoutCart;
        rbs[4] = layoutPersonalCenter;

        newGoodFragment=new NewGoodFragment();
        boutiqueFragment=new BoutiqueFragment();
        categoryFragment=new CategoryFragment();
        cartFragment=new CartFragment();
        personalFragment=new PersonalFragment();
        list=new ArrayList<>();
        list.add(newGoodFragment);
        list.add(boutiqueFragment);
        list.add(categoryFragment);
        list.add(cartFragment);
        list.add(personalFragment);
        //设置默认的fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container,newGoodFragment).show(newGoodFragment).commit();
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                index = 3;
                break;
            case R.id.layout_personal_center:
                if (FuLiCenterApplication.getUser()==null){
                    MFGT.gotoLogin(this);
                }else {
                    index = 4;
                    break;
                }
        }
        setChecked();
        switchFragment();
    }
    int currentIndex=0;
    private void switchFragment() {
        if(index==currentIndex){
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = list.get(index);
        if(!list.get(index).isAdded()){
            ft.add(R.id.fragment_container,fragment);
        }
        ft.show(fragment).hide(list.get(currentIndex)).commit();
        currentIndex=index;
    }

    private void setChecked() {
        for (int i = 0; i < rbs.length; i++) {
            if (i == index) {
                rbs[i].setChecked(true);
            } else {
                rbs[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FuLiCenterApplication.getUser()!=null){
            index=4;
        }
        switchFragment();
    }
}
