package com.game.xvso.deserializer;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.arch.core.util.Function;

import com.game.xvso.object.User;
import com.google.firebase.database.DataSnapshot;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Deserializer implements Function<DataSnapshot, User> {
    @Override
    public User apply(DataSnapshot dataSnapshot) {
        return dataSnapshot.getValue(User.class);
    }
}
