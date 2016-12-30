package com.xlmiao.coolweather.presenter;

import com.xlmiao.coolweather.common.IHttpService;
import com.xlmiao.coolweather.common.HttpApi;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by XuLeMiao on 2016/12/21.
 * BasePresenter
 */

public class BasePresenter<V> {
    public V mvpView;
    protected IHttpService iHttpService;
    private CompositeSubscription compositeSubscription;

    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        iHttpService = HttpApi.getDefaultApi();
    }

    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }


    /**
     * Base Subscription  的获取。节省代码，
     * CompositeSubscription 防止处理多个 Observables 和 Subscribers 内存泄露
     *  CompositeSubscription.unsubscribe() 方法在同一时间进行退订(unsubscribed)。
     * 参考的写法，有更优选择 废弃。
     * @param observable
     * @param subscriber
     */
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(observable
                // subscribeOn指定subscribe（）所发生的线程 即observable.subscribeOn被激活时所处的线程
                .subscribeOn(Schedulers.io())        // Schedulers.io()i/o操作（读写文件，读写数据库，网络信息交互），其行为模式与newThread（）差不多，区别内部实现无上限线程池，服用空闲线程，更有效率
                //observeOn指定subscribe（）所运行的线程。或者叫做事件消费的线程
                .observeOn(AndroidSchedulers.mainThread()) // AndroidSchedulers.mainThread() 指定操作将在Android主线程进行
                .subscribe(subscriber));
    }

    public void addSubscription(Observable observable, Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }
}
