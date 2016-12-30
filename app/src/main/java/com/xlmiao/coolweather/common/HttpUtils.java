package com.xlmiao.coolweather.common;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by XuLeMiao on 2016/12/23.
 * HttpUtils 返回一个可操作的 Subscriber
 */

public class HttpUtils {

    private HttpUtils(){

    }

    private static final HttpUtils httpUtils = new HttpUtils();

    public static HttpUtils getHttpUtils(){
        return httpUtils;
    }

    /**
     *
     * @param ob Observable
     * @param event  event 判断当前生命周期 onDestroy 时退出数据加载 防内存溢出
     * @param lifecycleSubject 与普通的Subject不同，在订阅时并不立即触发订阅事件，而是允许我们在任意时刻手动调用onNext(),onError(),onCompleted来触发事件。 这里时拿来结束上面的event
     * @param subscriber Subscriber
     */
    public void toSubscribe(Observable ob, ActivityLifeCycleEvent event,  PublishSubject<ActivityLifeCycleEvent> lifecycleSubject, Subscriber subscriber){
        Observable.Transformer result = RxHelper.applySchedules(event,lifecycleSubject);
        Observable observable = ob.compose(result)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //显示Dialog和一些其他操作
                        ((SubscriberCallBack)subscriber).showProgressDialog();
                    }
                });
        observable.subscribe(subscriber);
        //RetrofitCache.load(cacheKey,observable,isSave,forceRefresh).subscribe(subscriber); 没有cacheKey这个需求 该应用key为本地加载
    }


    public void toSubscribe(Observable observable, Subscriber subscriber){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
