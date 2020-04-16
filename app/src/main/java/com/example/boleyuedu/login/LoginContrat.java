package com.example.boleyuedu.login;

public interface LoginContrat {
    interface LoginIView{
        void updateUI();
    }
    interface LoginIpre{
        void senddata();
        void receivedata();
    }
    interface LoginModel{
        void sendhttp();
    }
}
