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
 * Represents an Event.
 *
 */
public class Event {
    private final String startDate;
    private final Type type;
    private final String label;

    public enum Type {
        CUSTOM,
        ANNIVERSARY,
        OTHER,
        BIRTHDAY,
        UNKNOWN;

        static Type fromValue(int value) {
            switch (value) {
                case ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM:
                    return CUSTOM;
                case ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY:
                    return ANNIVERSARY;
                case ContactsContract.CommonDataKinds.Event.TYPE_OTHER:
                    return OTHER;
                case ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY:
                    return BIRTHDAY;
                default:
                    return UNKNOWN;
            }
        }
    }

    Event(String startDate, Type type) {
        this.startDate = startDate;
        this.type = type;
        this.label = null;
    }

    Event(String startDate, String label) {
        this.startDate = startDate;
        this.type = Type.CUSTOM;
        this.label = label;
    }

    /**
     * Gets the event start date as a string.
     *
     * @return start date.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets the type of event.
     *
     * @return type of event.
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

        Event email = (Event) o;

        return startDate.equals(email.startDate) && type == email.type &&
                !(label != null ? !label.equals(email.label) : email.label != null);
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
