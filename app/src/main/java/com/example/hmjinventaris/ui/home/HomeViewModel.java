package com.example.hmjinventaris.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Selamat Datang Di Data Inventaris");
    }

    public LiveData<String> getText() {
        return mText;
    }
}