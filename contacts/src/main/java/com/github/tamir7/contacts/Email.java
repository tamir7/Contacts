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

/**
 * Represents an Email.
 *
 */
public class Email {
    private final String address;
    private final Type type;
    private final String label;

    public enum Type {
        CUSTOM,
        HOME,
        WORK,
        OTHER,
        MOBILE,
        UNKNOWN;

        static Type fromValue(int value) {
            switch (value) {
                case ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM:
                    return CUSTOM;
                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                    return HOME;
                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                    return WORK;
                case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                    return OTHER;
                case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                    return MOBILE;
                default:
                    return UNKNOWN;
            }
        }
    }

    Email(String address, Type type) {
        this.address = address;
        this.type = type;
        this.label = null;
    }

    Email(String address, String label) {
        this.address = address;
        this.type = Type.CUSTOM;
        this.label = label;
    }

    /**
     * Gets the email address.
     *
     * @return address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the type of email.
     *
     * @return type of email.
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the label. (null unless type = TYPE_CUSTOM)
     *
     * @return label.
     */
    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email email = (Email) o;

        return address.equals(email.address) && type == email.type &&
                !(label != null ? !label.equals(email.label) : email.label != null);
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
