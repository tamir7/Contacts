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

import android.annotation.SuppressLint;
import android.provider.ContactsContract;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a compound contact. aggregating all phones, email and photo's a contact has.
 */
public final class Contact {
    private String displayName;
    private final Set<PhoneNumber> phoneNumbers = new HashSet<>();
    private String photoUri;
    private final Set<Email> emails = new HashSet<>();
    private final Set<Event> events = new HashSet<>();

    interface AbstractField {
        String getMimeType();
        String getColumn();
    }

    public enum Field implements AbstractField {
        DisplayName(null, ContactsContract.Data.DISPLAY_NAME),
        PhoneNumber(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER),
        PhoneType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE),
        PhoneLabel(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.LABEL),
        @SuppressLint("InlinedApi")
        PhoneNormalizedNumber(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER),
        Email(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.ADDRESS),
        EmailType(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE),
        EmailLabel(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.LABEL),
        PhotoUri(null, ContactsContract.Data.PHOTO_URI),
        EventStartDate(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Event.START_DATE),
        EventType(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Event.TYPE),
        EventLabel(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Event.LABEL);

        private final String column;
        private final String mimeType;

        Field(String mimeType, String column) {
            this.mimeType = mimeType;
            this.column = column;
        }

        @Override
        public String getColumn() {
            return column;
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

    enum InternalField implements AbstractField {
        ContactId(null, ContactsContract.RawContacts.CONTACT_ID),
        MimeType(null, ContactsContract.Data.MIMETYPE);

        private final String column;
        private final String mimeType;

        InternalField(String mimeType, String column) {
            this.mimeType = mimeType;
            this.column = column;
        }

        @Override
        public String getColumn() {
            return column;
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

    Contact() {}

    Contact addDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    Contact addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(phoneNumber);
        return this;
    }

    Contact addPhotoUri(String photoUri) {
        this.photoUri = photoUri;
        return this;
    }

    Contact addEmail(Email email) {
        emails.add(email);
        return this;
    }

    Contact addEvent(Event event) {
        events.add(event);
        return this;
    }

    /**
     * Gets a the display name the contact.
     *
     * @return Display Name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets a list of all phone numbers the contact has.
     *
     * @return A List of phone numbers.
     */
    public List<PhoneNumber> getPhoneNumbers() {
        return Arrays.asList(phoneNumbers.toArray(new PhoneNumber[phoneNumbers.size()]));
    }

    /**
     * Gets a contacts photo uri.
     *
     * @return Photo URI.
     */
    public String getPhotoUri() {
        return photoUri;
    }

    /**
     * Gets a list of all emails the contact has.
     *
     * @return A List of emails.
     */
    public List<Email> getEmails() {
        return Arrays.asList(emails.toArray(new Email[emails.size()]));
    }

    /**
     * Gets a list of all events the contact has.
     *
     * @return A List of emails.
     */
    public List<Event> getEvents() {
        return Arrays.asList(events.toArray(new Event[events.size()]));
    }

    /**
     * Gets the birthday event if exists.
     *
     * @return Birthday event or null.
     */
    public Event getBirthday() {
        return getEvent(Event.Type.BIRTHDAY);
    }

    /**
     * Gets the anniversary event if exists.
     *
     * @return Anniversary event or null.
     */
    public Event getAnniversary() {
        return getEvent(Event.Type.ANNIVERSARY);

    }

    private Event getEvent(Event.Type type) {
        for (Event event: events) {
            if (type.equals(event.getType())) {
                return event;
            }
        }

        return null;
    }
}
