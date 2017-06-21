package com.github.tamir7.contacts.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.ContactTransformer;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class SampleActivity extends AppCompatActivity {
    private static final String TAG = SampleActivity.class.getSimpleName();
    private static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 76;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        checkPermission();
    }

    private void queryContacts() {
        Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Query<CustomModel> q = Contacts.getQuery();
                q.include(Contact.Field.ContactId, Contact.Field.DisplayName, Contact.Field.PhoneNumber, Contact.Field.PhoneNormalizedNumber, Contact.Field.Email, Contact.Field.EventStartDate, Contact.Field.PhoneLabel, Contact.Field.SortKey, Contact.Field.PhoneBookLabel);
                /*Query q1 = Contacts.getQuery();
                q1.whereEqualTo(Contact.Field.DisplayName, "Tamir Shomer");
                q1.hasPhoneNumber();

                Query q2 = Contacts.getQuery();
                q2.whereStartsWith(Contact.Field.ContactId, "791");
                q2.hasPhoneNumber();
                List<Query> queries = new ArrayList<>();
                queries.add(q1);
                queries.add(q2);
                q.or(queries);*/

                q.sortOrder(Contact.Field.SortKey);

                q.transformer(new ContactTransformer<CustomModel>() {
                    @Override
                    public CustomModel transform(Contact source) {
                        CustomModel model = new CustomModel();
                        model.setName(source.getDisplayName());
                        if (source.getPhoneNumbers() != null && source.getPhoneNumbers().size() > 0) {
                            model.setPhone(source.getPhoneNumbers().get(0).getNumber());
                        }
                        return model;
                    }
                });
                List<CustomModel> contacts = q.find();
                Log.e(TAG, new GsonBuilder().setPrettyPrinting().create().toJson(contacts));
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                if (task.isFaulted()) {
                    Log.e(TAG, "find failed", task.getError());
                }
                return null;
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            queryContacts();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACT_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == READ_CONTACT_PERMISSION_REQUEST_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            queryContacts();
        }
    }
}
