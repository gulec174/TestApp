package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import retrofit2.Call;
import retrofit2.Response;


@Database(entities = {Note.class}, version = 3)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;
    static List<Note> notes;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "my_database")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private PopulateDbAsyncTask(NoteDatabase db){
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            NetworkService.getInstance()
                    .getJSONApi()
                    .getAllNotes()
                    .enqueue(new retrofit2.Callback<List<Note>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Note>> call, @NonNull Response<List<Note>> response) {
                            notes = response.body();
                            for(Note e: notes){
                                noteDao.insert(e);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Note>> call, @NonNull Throwable t) {
                            t.printStackTrace();
                        }
                    });
            return null;
        }
    }
}
