package com.xlmiao.coolweather.common;

import android.content.Context;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by XuLeMiao on 2016/12/22.
 */

public abstract class SubscriberCallBack<M> extends Subscriber<M> implements ProgressCancelListener{

    public abstract void onSuccess(M model);
    public abstract void onFailure(String msg);
    //public abstract void onFinish();

    private SimpleLoadDialog dialogHandler;

    public SubscriberCallBack(Context context) {
        dialogHandler = new SimpleLoadDialog(context,this,true);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            //LogUtil.d("code=" + code);
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "服务器异常，请稍后再试";
            }
            onFailure(msg);
        } else {
            onFailure(e.getMessage());
        }
        dismissProgressDialog();
        onCancelProgress();
    }

    @Override
    public void onNext(M model) {
        onSuccess(model);
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
        onCancelProgress();
    }


    /**
     * 显示Dialog
     */
    public void showProgressDialog(){
        if (dialogHandler != null) {
//            dialogHandler.obtainMessage(SimpleLoadDialog.SHOW_PROGRESS_DIALOG).sendToTarget();
            dialogHandler.show();
        }
    }

    /**
     * 隐藏Dialog
     */
    public void dismissProgressDialog() {
        if (dialogHandler != null) {
//            dialogHandler.obtainMessage(SimpleLoadDialog.DISMISS_PROGRESS_DIALOG).sendToTarget();
            dialogHandler.dismiss();
            dialogHandler = null;
        }
    }
}
