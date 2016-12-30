package com.xlmiao.coolweather.common;

import android.util.Log;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.observers.Observers;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by XuLeMiao on 2016/12/23.
 */

public class RxHelper {
    /**
     *  对返回的请求进行预处理
     */
    public static  final Observable.Transformer TRANSFORMER = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {


            return ((Observable)observable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static <T>Observable.Transformer<T,T> applySchedules(ActivityLifeCycleEvent event, PublishSubject<ActivityLifeCycleEvent> lifecycleSubject){

        return (Observable.Transformer<T,T>)new Observable.Transformer(){

            @Override
            public Object call(Object observable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
                            @Override
                            public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
                                return activityLifeCycleEvent.equals(event); //true则结束这次请求 //通常为onDestroy
                            }
                        });
                return ((Observable)observable)
                        .takeUntil(compareLifecycleObservable) //true则结束这次请求 通常为onDestroy
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
