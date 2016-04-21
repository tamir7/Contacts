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
        return c.getLong(c.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
    }

    String getMimeType() {
        return c.getString(c.getColumnIndex(ContactsContract.Data.MIMETYPE));
    }

    String getDisplayName() {
        return c.getString(c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
    }

    PhoneNumber getPhoneNumber() {
        String normalizedNumber =
                c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
        if (normalizedNumber == null) {
            return null;
        }

        PhoneNumber.Type type =
                PhoneNumber.Type.fromValue(c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
        if (!type.equals(PhoneNumber.Type.CUSTOM)) {
            return new PhoneNumber(normalizedNumber, type);
        }

        return new PhoneNumber(normalizedNumber,
                c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)));
    }

    Email getEmail() {
        String address =
                c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        if (address == null) {
            return null;
        }

        Email.Type type =
                Email.Type.fromValue(c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
        if (!type.equals(Email.Type.CUSTOM)) {
            return new Email(address, type);
        }

        return new Email(address,
                c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL)));
    }

    String getPhotoUri() {
        return c.getString(c.getColumnIndex(ContactsContract.Data.PHOTO_URI));
    }
}
