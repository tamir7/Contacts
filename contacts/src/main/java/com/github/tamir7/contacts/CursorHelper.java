/*
 * Copyright 2016 Tamir Shomer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tamir7.contacts;

import android.database.Cursor;
import android.provider.ContactsContract;

class CursorHelper {
    private final Cursor c;

    CursorHelper(Cursor c) {
        this.c = c;
    }

    Long getContactId() {
        return getLong(c, ContactsContract.RawContacts.CONTACT_ID);
    }

    String getMimeType() {
        return getString(c, ContactsContract.Data.MIMETYPE);
    }

    String getDisplayName() {
        return getString(c, ContactsContract.Data.DISPLAY_NAME);
    }

    PhoneNumber getPhoneNumber() {
        String normalizedNumber = getString(c, ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
        if (normalizedNumber == null) {
            return null;
        }

        PhoneNumber.Type type = PhoneNumber.Type.fromValue(getInt(c,
                ContactsContract.CommonDataKinds.Phone.TYPE));
        if (!type.equals(PhoneNumber.Type.CUSTOM)) {
            return new PhoneNumber(normalizedNumber, type);
        }

        return new PhoneNumber(normalizedNumber,
                getString(c, ContactsContract.CommonDataKinds.Phone.LABEL));
    }

    Email getEmail() {
        String address = getString(c, ContactsContract.CommonDataKinds.Email.ADDRESS);
        if (address == null) {
            return null;
        }

        Email.Type type = Email.Type.fromValue(getInt(c, ContactsContract.CommonDataKinds.Email.TYPE));
        if (!type.equals(Email.Type.CUSTOM)) {
            return new Email(address, type);
        }

        return new Email(address, getString(c, ContactsContract.CommonDataKinds.Email.LABEL));
    }

    String getPhotoUri() {
        return getString(c, ContactsContract.Data.PHOTO_URI);
    }

    private String getString(Cursor c, String column) {
        int index = c.getColumnIndex(column);
        return index == -1 ? null : c.getString(index);
    }

    private Integer getInt(Cursor c, String column) {
        int index = c.getColumnIndex(column);
        return index == -1 ? null : c.getInt(index);
    }

    private  Long getLong(Cursor c, String column) {
        int index = c.getColumnIndex(column);
        return index == -1 ? null : c.getLong(index);
    }
}
