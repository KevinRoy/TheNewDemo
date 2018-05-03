package com.mattbaby.vega.lib.base;

/**
 * Created by kevin on 16/6/26.
 */

public class LibBaseModel<P extends LibBasePresenter> {

    private P mPresenter;

    public LibBaseModel(P presenter) {
        this.mPresenter = presenter;
    }

    public P getPresenter() {
        return mPresenter;
    }
}
