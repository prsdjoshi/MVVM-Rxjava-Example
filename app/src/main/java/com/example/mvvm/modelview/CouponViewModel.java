package com.example.mvvm.modelview;

import android.arch.lifecycle.ViewModel;

import com.example.mvvm.model.Coupon;
import com.example.mvvm.model.CouponModel;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CouponViewModel extends ViewModel {

    private CouponModel couponModel;
    private final BehaviorSubject<String> selectedCategory = BehaviorSubject.create();

    public CouponViewModel(){
        couponModel = new CouponModel();
    }

    public Observable<ArrayList<String>> getCategories(){
        return couponModel.getCategories();
    }

    public Observable<List<Coupon>> getCouponsByCat(){
        return selectedCategory
                .observeOn(Schedulers.computation())
                .flatMap(couponModel::getCouponsByCat);
    }

    public void setSelectedCat(String cat){
        selectedCategory.onNext(cat);
    }
}
