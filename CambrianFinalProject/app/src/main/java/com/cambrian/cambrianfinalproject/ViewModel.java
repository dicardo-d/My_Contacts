package com.cambrian.cambrianfinalproject;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private MyContactDao dao;
    private LiveData<List<Contact>> contacts;

    public ViewModel(Application application) {
        super(application);
        RoomDB db = RoomDB.getDatabase(application);
        dao = db.dao();
        contacts = dao.getContacts();
    }

    LiveData<List<Contact>> getContacts() {
        return contacts;
    }

    public void insert (Contact contactModel) {
        new insertAsyncTask(dao).execute(contactModel);
    }

    public void delete (Contact contactModel) {
        new deleteAsyncTask(dao).execute(contactModel);
    }

    private static class insertAsyncTask extends AsyncTask<Contact, Void, Void> {
        private MyContactDao asyncTaskDao;
        insertAsyncTask(MyContactDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Contact, Void, Void> {
        private MyContactDao asyncTaskDao;
        deleteAsyncTask(MyContactDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}