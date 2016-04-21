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

import android.provider.ContactsContract;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Contact {
    private final Set<String> displayNames = new HashSet<>();
    private final Set<PhoneNumber> phoneNumbers = new HashSet<>();
    private final Set<String> photoUris = new HashSet<>();
    private final Set<Email> emails = new HashSet<>();

    interface AbstractField {
        String getMimeType();
        List<String> getColumns();
    }

    public enum Field implements AbstractField {
        DisplayName(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.Data.DISPLAY_NAME),
        PhoneNumber(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.LABEL),
        Email(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.TYPE,
                ContactsContract.CommonDataKinds.Email.LABEL),
        PhotoUri(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.Data.PHOTO_URI);

        private final List<String> columns;
        private final String mimeType;

        Field(String mimeType, String... columns) {
            this.mimeType = mimeType;
            this.columns = Arrays.asList(columns);
        }

        @Override
        public List<String> getColumns() {
            return columns;
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

    enum InternalField implements AbstractField {
        ContactId(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.RawContacts.CONTACT_ID),
        MimeType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.Data.MIMETYPE);

        private final List<String> columns;
        private final String mimeType;

        InternalField(String mimeType, String... columns) {
            this.mimeType = mimeType;
            this.columns = Arrays.asList(columns);
        }

        @Override
        public List<String> getColumns() {
            return columns;
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

    Contact() {}

    Contact addDisplayName(String displayName) {
        displayNames.add(displayName);
        return this;
    }

    Contact addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(phoneNumber);
        return this;
    }

    Contact addPhotoUri(String photoUri) {
        photoUris.add(photoUri);
        return this;
    }

    Contact addEmail(Email email) {
        emails.add(email);
        return this;
    }

    public List<String> getDisplayNames() {
        return Arrays.asList(displayNames.toArray(new String[displayNames.size()]));
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return Arrays.asList(phoneNumbers.toArray(new PhoneNumber[phoneNumbers.size()]));
    }

    public List<String> getPhotoUris() {
        return Arrays.asList(photoUris.toArray(new String[photoUris.size()]));
    }

    public List<Email> getEmails() {
        return Arrays.asList(emails.toArray(new Email[emails.size()]));
    }
}
