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

/**
 * Represents a compound contact. aggregating all phones, email and photo's a contact has.
 */
public final class Contact {
    private final Set<String> displayNames = new HashSet<>();
    private final Set<PhoneNumber> phoneNumbers = new HashSet<>();
    private final Set<String> photoUris = new HashSet<>();
    private final Set<Email> emails = new HashSet<>();
    private final Set<Event> events = new HashSet<>();

    public interface Comparator<T> {
        T compare(T first, T second);
    }

    interface AbstractField {
        String getMimeType();

        String getColumn();
    }

    public enum Field implements AbstractField {
        DisplayName(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.Data.DISPLAY_NAME),
        PhoneNumber(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER),
        PhoneNumberRaw(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER),
        PhoneType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE),
        PhoneLabel(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.LABEL),
        Email(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.ADDRESS),
        EmailType(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE),
        EmailLabel(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.LABEL),
        PhotoUri(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.Data.PHOTO_URI),
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
        ContactId(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.RawContacts.CONTACT_ID),
        MimeType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.Data.MIMETYPE);

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

    Contact() {
    }

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

    Contact addEvent(Event event) {
        events.add(event);
        return this;
    }

    boolean contains(Field field) {
        switch (field) {
            case DisplayName:
                return !displayNames.isEmpty();
            case PhoneNumber:
            case PhoneType:
            case PhoneLabel:
                return !phoneNumbers.isEmpty();
            case Email:
            case EmailType:
            case EmailLabel:
                return !emails.isEmpty();
            case PhotoUri:
                return !photoUris.isEmpty();
            case EventStartDate:
            case EventType:
            case EventLabel:
                return !events.isEmpty();
        }

        // we should never get here.
        throw new IllegalArgumentException(String.format("Field %s is not supported", field));
    }

    /**
     * Gets a list of all display names the contact has.
     *
     * @return A List of display names.
     */
    public List<String> getDisplayNames() {
        return Arrays.asList(displayNames.toArray(new String[displayNames.size()]));
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
     * Gets a list of all photo uri's the contact has.
     *
     * @return A List of photo uri's.
     */
    public List<String> getPhotoUris() {
        return Arrays.asList(photoUris.toArray(new String[photoUris.size()]));
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
     * Gets the "best" email address, Using the comparator.
     *
     * @param emailComparator The comparator used to compare Emails.
     * @return the "Best" Email
     */
    public Email getBestEmail(Comparator<Email> emailComparator) {
        return getBestValue(getEmails(), emailComparator);
    }

    /**
     * Gets the "best" email address using the default or the installed comparator.
     * Default Comparator returns the first email.
     *
     * @return the "Best" Email
     */
    public Email getBestEmail() {
        return getBestEmail(Contacts.getEmailComparator());
    }

    /**
     * Gets the "best" phone number, Using the comparator.
     *
     * @param phoneNumberComparator The comparator used to compare Phone Numbers.
     * @return the "Best" Email
     */
    public PhoneNumber getBestPhoneNumber(Comparator<PhoneNumber> phoneNumberComparator) {
        return getBestValue(getPhoneNumbers(), phoneNumberComparator);
    }

    /**
     * Gets the "best" phone number using the default or the installed comparator.
     * Default Comparator returns the first phone number.
     *
     * @return the "Best" Phone Number
     */
    public PhoneNumber getBestPhoneNumber() {
        return getBestPhoneNumber(Contacts.getPhoneNumberComparator());
    }

    /**
     * Gets the "best" display name, Using the comparator.
     *
     * @param displayNameComparator The comparator used to compare display names.
     * @return the "Best" Display Name
     */
    public String getBestDisplayName(Comparator<String> displayNameComparator) {
        return getBestValue(getDisplayNames(), displayNameComparator);
    }

    /**
     * Gets the "best" display name using the default or the installed comparator.
     * Default Comparator returns the first display name.
     *
     * @return the "Best" display name
     */
    public String getBestDisplayName() {
        return getBestDisplayName(Contacts.getDisplayNameComparator());
    }

    /**
     * Gets the "best" photo Uri Using the comparator.
     *
     * @param photoUriComparator The comparator used to compare photo uri's.
     * @return the "Best" Photo URI
     */
    public String getBestPhotoUri(Comparator<String> photoUriComparator) {
        return getBestValue(getPhotoUris(), photoUriComparator);
    }

    /**
     * Gets the "best" photo uri using the default or the installed comparator.
     * Default Comparator returns the first photoUri.
     *
     * @return the "Best" photo URI
     */
    public String getBestPhotoUri() {
        return getBestPhotoUri(Contacts.getPhotoUriComparator());
    }

    private <T> T getBestValue(List<T> values, Comparator<T> comparator) {
        T best = null;
        for (T value : values) {
            best = best == null ? value : comparator.compare(best, value);
        }

        return best;
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
        for (Event event : events) {
            if (type.equals(event.getType())) {
                return event;
            }
        }

        return null;
    }
}
