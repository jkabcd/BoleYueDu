package com.example.boleyuedu.login;

import com.bole.basemodel.mvpBase.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContrat.LoginIView,LoginModel> implements LoginContrat.LoginIpre{

    @Override
    public void senddata() {
        mvpModel.sendhttp();
    }

    @Override
    public void receivedata() {
        mvpView.updateUI();
    }
}
