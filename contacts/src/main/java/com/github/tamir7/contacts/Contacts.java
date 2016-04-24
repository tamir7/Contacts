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

import java.util.HashMap;
import java.util.Map;

public final class Contacts {
    private static Context context;
    private static Map<Contact.Field, Contact.Comparator> comparators = new HashMap<>();
    private static final DefaultComparator defaultComparator = new DefaultComparator<>();

    private static class DefaultComparator<T> implements Contact.Comparator<T> {

        @Override
        public T compare(T first, T second) {
            return first;
        }
    }


    private Contacts() {}

    /**
     * Initialize the Contacts library
     *
     * @param context context
     */
    public static void initialize(Context context) {
        Contacts.context = context.getApplicationContext();
    }

    /**
     * Get a new Query object to find contacts.
     *
     * @return  A new Query object.
     */
    public static Query getQuery() {
        if (Contacts.context == null) {
            throw new IllegalStateException("Contacts library not initialized");
        }

        return new Query(context);
    }

    /**
     * set a comparator to compare between phoneNumbers of same contact.
     *
     * @param phoneNumberComparator     phoneNumberComparator
     */
    public static void setPhoneNumberComparator(Contact.Comparator<PhoneNumber> phoneNumberComparator) {
        comparators.put(Contact.Field.PhoneNumber, phoneNumberComparator);
    }

    /**
     * set a comparator to compare between emails of same contact.
     *
     * @param emailComparator     emailComparator
     */
    public static void setEmailComparator(Contact.Comparator<Email> emailComparator) {
        comparators.put(Contact.Field.Email, emailComparator);
    }

    /**
     * set a comparator to compare between photoUri's of same contact.
     *
     * @param photoUriComparator     photoUriComparator
     */
    public static void setPhotoUriComparator(Contact.Comparator<String> photoUriComparator) {
        comparators.put(Contact.Field.PhotoUri, photoUriComparator);
    }

    /**
     * set a comparator to compare between display names of same contact.
     *
     * @param displayNameComparator     displayNameComparator
     */
    public static void setDisplayNameComparator(Contact.Comparator<String> displayNameComparator) {
        comparators.put(Contact.Field.DisplayName, displayNameComparator);
    }

    @SuppressWarnings("unchecked")
    private static <T> Contact.Comparator<T> getComparator(Contact.Field field) {
        return comparators.containsKey(field) ? comparators.get(field) : defaultComparator;
    }

    static Contact.Comparator<Email> getEmailComparator() {
        return getComparator(Contact.Field.Email);
    }

    static Contact.Comparator<PhoneNumber> getPhoneNumberComparator() {
        return getComparator(Contact.Field.PhoneNumber);
    }

    static Contact.Comparator<String> getPhotoUriComparator() {
        return getComparator(Contact.Field.PhotoUri);
    }

    static Contact.Comparator<String> getDisplayNameComparator() {
        return getComparator(Contact.Field.DisplayName);
    }
}
