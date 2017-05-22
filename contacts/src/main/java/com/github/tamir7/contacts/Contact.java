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
    private Long id;
    private String displayName;
    private String givenName;
    private String familyName;

    private final Set<PhoneNumber> phoneNumbers = new HashSet<>();
    private String photoUri;
    private final Set<Email> emails = new HashSet<>();
    private final Set<Event> events = new HashSet<>();
    private String companyName;
    private String companyTitle;
    private final Set<String> websites = new HashSet<>();
    private final Set<Address> addresses = new HashSet<>();
    private String note;

    interface AbstractField {
        String getMimeType();

        String getColumn();
    }

    public enum Field implements AbstractField {
        ContactId(null, ContactsContract.RawContacts.CONTACT_ID),
        DisplayName(null, ContactsContract.Data.DISPLAY_NAME),
        GivenName(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME),
        FamilyName(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME),
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
                ContactsContract.CommonDataKinds.Event.LABEL),
        CompanyName(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Organization.COMPANY),
        CompanyTitle(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Organization.TITLE),
        Website(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Website.URL),
        Note(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Note.NOTE),
        Address(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS),
        AddressType(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE),
        AddressStreet(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.STREET),
        AddressCity(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.CITY),
        AddressRegion(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.REGION),
        AddressPostcode(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE),
        AddressCountry(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY),
        AddressLabel(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.LABEL);

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

    void setId(Long id) {
        this.id = id;
    }

    Contact addDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    Contact addGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    Contact addFamilyName(String familyName) {
        this.familyName = familyName;
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

    Contact addCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    Contact addCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
        return this;
    }

    Contact addWebsite(String website) {
        websites.add(website);
        return this;
    }

    Contact addNote(String note) {
        this.note = note;
        return this;
    }

    Contact addAddress(Address address) {
        addresses.add(address);
        return this;
    }

    /**
     * Gets a the phone contact id.
     *
     * @return contact id.
     */
    public Long getId() {
        return id;
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
     * Gets a the given name the contact.
     *
     * @return Given Name.
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Gets a the Family name the contact.
     *
     * @return Family Name.
     */
    public String getFamilyName() {
        return familyName;
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

    /**
     * Gets the name of the company the contact works on
     *
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Gets the job title of the contact
     *
     * @return the job title
     */
    public String getCompanyTitle() {
        return companyTitle;
    }
  
    /**
     * Gets the list of all websites the contact has
     *
     * @return A list of websites
     */
    public List<String> getWebsites() {
        return Arrays.asList(websites.toArray(new String[websites.size()]));
    }

    /**
     * Gets the note of the contact
     *
     * @return the note
     */
    public String getNote() {
        return note;
    }
  
    /**
     * Gets the list of addresses
     *
     * @return A list of addresses
     */
    public List<Address> getAddresses() {
        return Arrays.asList(addresses.toArray(new Address[addresses.size()]));
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
