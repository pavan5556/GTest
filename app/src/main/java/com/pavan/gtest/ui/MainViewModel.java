package com.pavan.gtest.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.pavan.gtest.data.YTVideosLiveData;
import com.pavan.gtest.data.YTdataModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<YTdataModel> selectedVideoLiveData = new MutableLiveData<>();

    public YTVideosLiveData getyoutubeVideos(DatabaseReference reference) {
        return new YTVideosLiveData(reference);
    }

    public LiveData<YTdataModel> getSelectedVideo() {
        return selectedVideoLiveData;
    }

    public void setSelectedVideoLiveData(YTdataModel yTdataModel) {
        selectedVideoLiveData.postValue(yTdataModel);
    }
}
