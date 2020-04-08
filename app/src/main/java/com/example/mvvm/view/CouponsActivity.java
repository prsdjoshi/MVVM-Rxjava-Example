package com.example.mvvm.view;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.mvvm.R;
import com.example.mvvm.model.Coupon;
import com.example.mvvm.modelview.CouponViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CouponsActivity extends AppCompatActivity {
    @NonNull
    private CompositeDisposable compositeDisposable;
    @NonNull
    private ListView categoriesLst;
    @NonNull
    private ListView couponsLst;
    @NonNull
    private CouponViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        categoriesLst = findViewById(R.id.categories);
        couponsLst = findViewById(R.id.coupons);

        viewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(CouponViewModel.class);

        setItemClickListener();
    }

    private void setItemClickListener(){
        categoriesLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String cat = (String)adapterView.getItemAtPosition(i);
                viewModel.setSelectedCat(cat);
            }
        });
    }
        @Override
    protected void onResume() {
        super.onResume();
        bind();
    }

    @Override
    protected void onPause() {
        unBind();
        super.onPause();
    }

    private void bind() {
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(viewModel.getCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setCategories));

        compositeDisposable.add(viewModel.getCouponsByCat()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setCoupons));
    }

    private void unBind() {
        compositeDisposable.clear();
    }

    private void setCategories(ArrayList<String> cats){
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, cats);
        categoriesLst.setAdapter(itemsAdapter);
    }

    private void setCoupons(List<Coupon> coupons){
        ArrayAdapter<Coupon> itemsAdapter =
                new ArrayAdapter<Coupon>(this,
                        android.R.layout.simple_list_item_1, coupons);
        couponsLst.setAdapter(itemsAdapter);
    }
}
