package com.mattbaby.vega.lib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kevin on 16/6/18.
 */

public abstract class LibBaseActivity extends AppCompatActivity implements LibIBaseView {

    private LibBasePresenter presenter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected abstract int getLayout();

    protected abstract void initView();

    private static Stack<Activity> mList = new Stack<Activity>();

    public void addSubscribe(Disposable disposable) {
        if (compositeDisposable != null)
            compositeDisposable.add(disposable);
    }

    public void unAllSubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
        presenter = new LibBasePresenter(this);

        mList.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.unAllSubscribe();
        }

        unAllSubscribe();
    }

    @Override
    public Context getContext() {
        return this;
    }

    protected void baseExit() {
        if (mList == null || mList.size() == 0)
            return;

        for (Activity activity : mList) {
            if (activity != null)
                if (activity instanceof LibBaseActivity)
                    activity.finish();
        }
        mList.clear();
    }
}
