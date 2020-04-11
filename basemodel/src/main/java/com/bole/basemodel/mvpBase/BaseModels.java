package com.bole.basemodel.mvpBase;

import android.util.Log;

/**
 * **************************************
 * 项目名称：BoleYueDu
 *
 * @Author jack
 * 邮箱：1253865188@qq.com
 * 创建时间2020/4/11 11:41
 * 用途
 * **************************************
 */
public  class BaseModels<P extends IPresenter> implements IBaseModel<P> {
    P mvpPresent;

    public void setMvpPresent(P mvpPresent) {
        this.mvpPresent = mvpPresent;
    }
}
