package com.androidwind.androidquick.demo.features.architecture.mvvm.activity;

import com.androidwind.androidquick.demo.bean.NameBean;
import com.androidwind.androidquick.demo.constant.Constants;
import com.androidwind.androidquick.demo.features.module.network.retrofit.GankApis;
import com.androidwind.androidquick.demo.features.module.network.retrofit.RetrofitManager;
import com.androidwind.androidquick.demo.features.module.network.retrofit.TestApis;
import com.androidwind.androidquick.module.exception.ApiException;
import com.androidwind.androidquick.module.rxjava.BaseObserver;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ddnosh
 * @website http://blog.csdn.net/ddnosh
 */
public class MVVMRepository1 {

    private MutableLiveData<List<NameBean>> testLiveData = new MutableLiveData<>();

    public Observable getGankResData() {
        return RetrofitManager.INSTANCE.getRetrofit(Constants.GANK_API_URL).create(GankApis.class)
                .getHistoryDate();
    }

    public MutableLiveData<List<NameBean>> getTestData() {
        RetrofitManager.INSTANCE.getRetrofit(Constants.GANK_API_URL).create(TestApis.class)
                .getTestData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<NameBean>>() {

                               @Override
                               public void onError(ApiException exception) {

                               }

                               @Override
                               public void onSuccess(List<NameBean> testBeans) {
                                   testLiveData.setValue(testBeans);
                               }
                           }
                );
        return testLiveData;
    }
}
