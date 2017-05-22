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

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Query class defines a query that is used to fetch Contact objects.
 */
public final class Query {
    private final Context context;
    private final Map<String, Where> mimeWhere = new HashMap<>();
    private Where defaultWhere = null;
    private Set<Contact.Field> include = new HashSet<>();
    private List<Query> innerQueries;

    Query(Context context) {
        this.context = context;
        include.addAll(Arrays.asList(Contact.Field.values()));
    }

    /**
     * Add a constraint to the query for finding string values that contain the provided string.
     *
     * @param field     The field that the string to match is stored in.
     * @param value     The substring that the value must contain.
     * @return          this, so you can chain this call.
     */
    public Query whereContains(Contact.Field field, Object value) {
        addNewConstraint(field, Where.contains(field.getColumn(), value));
        return this;
    }

    /**
     * Add a constraint to the query for finding string values that start with the provided string.
     *
     * @param field     The field that the string to match is stored in.
     * @param value     The substring that the value must start with.
     * @return          this, so you can chain this call.
     */
    public Query whereStartsWith(Contact.Field field, Object value) {
        addNewConstraint(field, Where.startsWith(field.getColumn(), value));
        return this;
    }

    /**
     * Add a constraint to the query for finding values that equal the provided value.
     *
     * @param field     The field that the value to match is stored in.
     * @param value     The value that the field value must be equal to.
     * @return          this, so you can chain this call.
     */
    public Query whereEqualTo(Contact.Field field, Object value) {
        addNewConstraint(field, Where.equalTo(field.getColumn(), value));
        return this;
    }


    /**
     * Add a constraint to the query for finding values that NOT equal the provided value.
     *
     * @param field     The field that the value to match is stored in.
     * @param value     The value that the field value must be NOT equal to.
     * @return          this, so you can chain this call.
     */
    public Query whereNotEqualTo(Contact.Field field, Object value) {
        addNewConstraint(field, Where.notEqualTo(field.getColumn(), value));
        return this;
    }

    /**
     * Restrict the return contacts to only include contacts with a phone number.
     *
     * @return this, so you can chain this call.
     */
    public Query hasPhoneNumber() {
        defaultWhere = addWhere(defaultWhere, Where.notEqualTo(ContactsContract.Data.HAS_PHONE_NUMBER, 0));
        return this;
    }

    /**
     * Constructs a query that is the or of the given queries.
     * Previous calls to include are disregarded for the inner queries.
     * Calling those functions on the returned query will have the desired effect.
     * Calling where* functions on the return query is not permitted.
     *
     * @param queries The list of Queries to 'or' together.
     * @return A query that is the 'or' of the passed in queries.
     */
    public Query or(List<Query> queries) {
        innerQueries = queries;
        return this;
    }

    /**
     * Restrict the fields of returned Contacts to only include the provided fields.
     *
     * @param fields The array of keys to include in the result.
     * @return this, so you can chain this call.
     */
    public Query include(Contact.Field... fields) {
        include.clear();
        include.addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * Retrieves a list of contacts that satisfy this query.
     *
     * @return A list of all contacts obeying the conditions set in this query.
     */
    public List<Contact> find() {
        List<Long> ids = new ArrayList<>();

        if (innerQueries != null) {
            for (Query query : innerQueries) {
                ids.addAll(query.findInner());
            }
        } else {
            if (mimeWhere.isEmpty()) {
                return find(null);
            }

            for (Map.Entry<String, Where> entry : mimeWhere.entrySet()) {
                ids = findIds(ids, entry.getKey(), entry.getValue());
            }
        }

        return find(ids);
    }

    private List<Long> findIds(List<Long> ids, String mimeType, Where innerWhere) {
        String[] projection = { ContactsContract.RawContacts.CONTACT_ID};
        Where where = Where.equalTo(ContactsContract.Data.MIMETYPE, mimeType);
        where = addWhere(where, innerWhere);
        if (!ids.isEmpty()) {
            where = addWhere(where, Where.in(ContactsContract.RawContacts.CONTACT_ID, new ArrayList<Object>(ids)));
        }

        Cursor c = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                projection,
                where.toString(),
                null,
                ContactsContract.RawContacts.CONTACT_ID);

        List<Long> returnIds = new ArrayList<>();

        if (c != null) {
            while (c.moveToNext()) {
                CursorHelper helper = new CursorHelper(c);
                returnIds.add(helper.getContactId());
            }

            c.close();
        }

        return returnIds;
    }

    private List<Long> findInner() {
        List<Long> ids = new ArrayList<>();

        if (mimeWhere.isEmpty()) {
            Cursor c = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.RawContacts.CONTACT_ID},
                    defaultWhere.toString(),
                    null,
                    ContactsContract.RawContacts.CONTACT_ID);
            if (c != null) {
                while (c.moveToNext()) {
                    CursorHelper helper = new CursorHelper(c);
                    ids.add(helper.getContactId());
                }

                c.close();
            }
        } else {
            for (Map.Entry<String, Where> entry : mimeWhere.entrySet()) {
                ids = findIds(ids, entry.getKey(), entry.getValue());
            }
        }
        return ids;
    }

    private List<Contact> find(List<Long> ids) {
        Where where;
        if (ids == null) {
            where = defaultWhere;
        } else if (ids.isEmpty()) {
            return new ArrayList<>();
        } else {
            where = Where.in(ContactsContract.RawContacts.CONTACT_ID, new ArrayList<>(ids));
        }

        Cursor c = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                buildProjection(),
                addWhere(where, buildWhereFromInclude()).toString(),
                null,
                ContactsContract.Data.DISPLAY_NAME);

        Map<Long, Contact> contactsMap = new LinkedHashMap<>();

        if (c != null) {
            while (c.moveToNext()) {
                CursorHelper helper = new CursorHelper(c);
                Long contactId = helper.getContactId();
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    contact = new Contact();
                    contactsMap.put(contactId, contact);
                }

                contact.setId(contactId);
                updateContact(contact, helper);
            }

            c.close();
        }

        return new ArrayList<>(contactsMap.values());
    }

    private Where buildWhereFromInclude() {
        Set<String> mimes = new HashSet<>();
        for (Contact.Field field : include) {
            if (field.getMimeType() != null) {
                mimes.add(field.getMimeType());
            }
        }
        return Where.in(ContactsContract.Data.MIMETYPE, new ArrayList<Object>(mimes));
    }

    private void addNewConstraint(Contact.Field field, Where where)  {
        if (field.getMimeType() == null) {
            defaultWhere = addWhere(defaultWhere, where);
        } else {
            Where existingWhere = mimeWhere.get(field.getMimeType());
            mimeWhere.put(field.getMimeType(), addWhere(existingWhere, where));
        }
    }

    private void updateContact(Contact contact, CursorHelper helper) {
        String displayName = helper.getDisplayName();
        if (displayName != null) {
            contact.addDisplayName(displayName);
        }

        String photoUri = helper.getPhotoUri();
        if (photoUri != null) {
            contact.addPhotoUri(photoUri);
        }

        String mimeType = helper.getMimeType();
        switch (mimeType) {
            case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                PhoneNumber phoneNumber = helper.getPhoneNumber();
                if (phoneNumber != null) {
                    contact.addPhoneNumber(phoneNumber);
                }
                break;
            case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                Email email = helper.getEmail();
                if (email != null) {
                    contact.addEmail(email);
                }
                break;
            case ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE:
                Event event = helper.getEvent();
                if (event != null) {
                    contact.addEvent(event);
                }
                break;
            case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                String givenName = helper.getGivenName();
                if (givenName != null) {
                    contact.addGivenName(givenName);
                }

                String familyName = helper.getFamilyName();
                if (familyName != null) {
                    contact.addFamilyName(familyName);
                }
                break;
            case ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE:
                String companyName = helper.getCompanyName();

                if (companyName != null) {
                    contact.addCompanyName(companyName);
                }

                String companyTitle = helper.getCompanyTitle();

                if (companyTitle != null) {
                    contact.addCompanyTitle(companyTitle);
                }
                break;
            case ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE:
                String website = helper.getWebsite();
                if (website != null) {
                    contact.addWebsite(website);
                }
                break;
            case ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE:
                String note = helper.getNote();
                if (note != null) {
                    contact.addNote(note);
                }
                break;
            case ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE:
                Address address = helper.getAddress();
                if (address != null) {
                    contact.addAddress(address);
                }
                break;
        }
    }

    private String[] buildProjection() {
        Set<String> projection = new HashSet<>();

        for (Contact.AbstractField field : Contact.InternalField.values()) {
            projection.add(field.getColumn());
        }

        for (Contact.AbstractField field : include) {
            projection.add(field.getColumn());
        }

        return projection.toArray(new String[projection.size()]);
    }

    private Where addWhere(Where where, Where otherWhere) {
        return where == null ? otherWhere : where.and(otherWhere);
    }
}
