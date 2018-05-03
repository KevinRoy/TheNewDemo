package com.mattbaby.vega.lib.base;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kevin on 16/6/18.
 */

public class LibBasePresenter<T extends LibIBaseView> {

    public T view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LibBasePresenter(T view) {
        this.view = view;
    }

    public void addSubscribe(Disposable disposable) {
        if (compositeDisposable != null)
            compositeDisposable.add(disposable);
    }

    public void unAllSubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.clear();

    }

    public T getView() {
        return view;
    }

    public Context getContext() {
        return view.getContext();
    }
}
