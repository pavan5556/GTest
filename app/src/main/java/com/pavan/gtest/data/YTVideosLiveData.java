package com.pavan.gtest.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class YTVideosLiveData extends LiveData<List<YTdataModel>> {

    private DatabaseReference databaseReference;
    private MyDbListener dbListener;

    public YTVideosLiveData(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        dbListener = new MyDbListener();
    }

    @Override
    protected void onActive() {
        databaseReference.addValueEventListener(dbListener);
    }

    @Override
    protected void onInactive() {
        databaseReference.removeEventListener(dbListener);
    }

    class MyDbListener implements ValueEventListener {

        List<YTdataModel> yTdataModelList;

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            yTdataModelList = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                yTdataModelList.add(dataSnapshot.getValue(YTdataModel.class));
            }
            setValue(yTdataModelList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }
}
