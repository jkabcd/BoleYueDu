package com.example.boleyuedu.login;

import com.bole.basemodel.mvpBase.BaseModels;

public class LoginModel extends BaseModels<LoginContrat.LoginIpre> implements LoginContrat.LoginModel{
    @Override
    public void sendhttp() {
mvpPresent.receivedata();
    }
}
