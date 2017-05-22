# Contacts

Android Contacts API.

## Quick Start

Initialize Contacts Library

```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contacts.initialize(this);
    }
```
Get All Contacts 

```java
List<Contact> contacts = Contacts.getQuery().find();
```

Get Contacts with phone numbers only

```java
Query q = Contacts.getQuery();
q.hasPhoneNumber();
List<Contact> contacts = q.find();
```

Get Specific fields

```java
Query q = Contacts.getQuery();
q.include(Contact.Field.DisplayName, Contact.Field.Email, Contact.Field.PhotoUri);
List<Contact> contacts = q.find();
```

Search By Display Name

```java
Query q = Contacts.getQuery();
q.whereContains(Contact.Field.DisplayName, "some string");
List<Contact> contacts = q.find();
```

Find all numbers with a specific E164 code

```java
Query q = Contacts.getQuery();
q.whereStartsWith(Contact.Field.PhoneNormalizedNumber, "+972");
List<Contact> contacts = q.find();
```

Find a Contact by phone Number

```java
Query q = Contacts.getQuery();
q.whereEqualTo(Contact.Field.PhoneNumber, "Some phone Number");
List<Contact> contacts = q.find();
```

Get all Contacts that their name begins with a specific string OR their phone begings with a specific prefix.
```java
Query mainQuery = Contacts.getQuery();
Query q1 = Contacts.getQuery();
q1.whereStartsWith(Contact.Field.DisplayName, "Some String");
Query q2 = Contacts.getQuery();
q2.whereStartsWith(Contact.Field.PhoneNormalizedNumber, "+972");
List<Query> qs = new ArrayList<>();
qs.add(q1);
qs.add(q2);
mainQuery.or(qs);
List<Contact> contacts = mainQuery.find();

```

## Installation

Published to JCenter

```java

 compile 'com.github.tamir7.contacts:contacts:1.1.7'
```

## License

    Copyright 2016 Tamir Shomer

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
