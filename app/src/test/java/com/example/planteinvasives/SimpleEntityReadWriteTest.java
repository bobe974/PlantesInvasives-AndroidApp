package com.example.planteinvasives;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Photographie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class SimpleEntityReadWriteTest {

    private Controle db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, Controle.class).build();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    // DATA SET FOR TEST

    private static Photographie PHOTO_DEMO = new Photographie(1,"/android/photo","20/29/2018",123,332);


    @Test
    public void insertAndGetPhoto() throws InterruptedException {
        // BEFORE : Adding a new user
        this.db.photoDao().insert(PHOTO_DEMO);
        // TEST
        Photographie photo  =  LiveDataTestUtil.getValue(this.db.photoDao().getPhotoById(1));
        assertTrue(photo.getChemin().equals(PHOTO_DEMO.getChemin()));
    }
}
