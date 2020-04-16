package com.bole.basemodel.mvpBase;

import android.util.Log;

import java.lang.reflect.ParameterizedType;

/**
 * **************************************
 * 项目名称：BoleYueDu
 *
 * @Author jack
 * 邮箱：1253865188@qq.com
 * 创建时间2020/4/11 11:30
 * 用途
 * **************************************
 */
public  class BasePresenter<V ,M extends BaseModels> implements IPresenter {
   public V mvpView;
   public M mvpModel;
    public  void setMvpView(V v){
        this.mvpView = v;
        setMvpModel();
    }
    public void setMvpModel(){
                try {
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<M> clazz = (Class<M>) pt.getActualTypeArguments()[1];
            mvpModel = clazz.newInstance();
            mvpModel.setMvpPresent(this);
        } catch (Exception e) {
            Log.e("BaseActivity","M请继承BaseModel,否则无法实例化");
            e.printStackTrace();
        }
    }
    /**
     * @description 解除绑定内存释放
     * @param
     * @return
     * @author Administrator
     * @time 2020/4/13 10:44
     */
    public void onDetachMvpView(){
        if(mvpModel!=null)
        mvpModel.clearHttps();
        mvpModel = null;
        mvpView = null;
    }

}
