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

import android.database.DatabaseUtils;

import java.util.List;

final class Where {
    private StringBuilder where;

    private Where(String key, String value, Operator operator) {
        where = new StringBuilder(key).append(operator.toString()).append(value);
    }

    private Where(String key, List<?> objects, Operator operator) {
        where = new StringBuilder(key).append(operator).append("(");
        boolean first = true;
        for (Object o : objects) {
            if (first) {
                first = false;
            } else {
                where.append(", ");
            }
            where.append(toSafeString(o));
        }

        where.append(")");
    }

    static Where in(String key, List<?> objects) {
        return new Where(key, objects, Operator.In);
    }

    static Where in(String key, String statement) {
        return new Where(key, statement, Operator.In);
    }

    static Where notIn(String key, List<?> objects) {
        return new Where(key, objects, Operator.NotIn);
    }

    static Where equalTo(String key, Object value) {
        return new Where(key, toSafeString(value), Operator.Equal);
    }

    static Where startsWith(String key, Object prefix) {
        return new Where(key, String.format("'%s%%'", prefix.toString()), Operator.Like);
    }

    static Where endsWith(String key, Object suffix) {
        return new Where(key, String.format("'%%%s'", suffix.toString()), Operator.Like);
    }

    static Where contains(String key, Object substring) {
        return new Where(key, String.format("'%%%s%%'", substring.toString()), Operator.Like);
    }

    static Where doesNotStartWith(String key, Object prefix) {
        return new Where(key, String.format("'%s%%'", prefix.toString()), Operator.NotLike);
    }

    static Where notEqualTo(String key, Object value) {
        return new Where(key, toSafeString(value), Operator.NotEqual);
    }

    static Where greaterThan(String key, Object value) {
        return new Where(key, toSafeString(value), Operator.GreaterThan);
    }

    static Where greaterThanOrEqual(String key, Object value) {
        return new Where(key, toSafeString(value), Operator.GreaterThanOrEqual);
    }

    static Where lessThan(String key, Object value) {
        return new Where(key, toSafeString(value), Operator.LessThan);
    }

    static Where lessThanOrEqual(String key, Object value) {
        return new Where(key, toSafeString(value), Operator.LessThanOrEqual);
    }

    static Where is(String key, Object value) {
        return new Where(key, toSafeString(value), Operator.Is);
    }

    static Where isNot(String key, Object value) {
        return new Where(key, toSafeString(value), Operator.IsNot);
    }

    Where and(Where andWhere) {
        where = new StringBuilder(String.format("( %s AND %s )", where.toString(),
                andWhere.toString()));
        return this;
    }

    Where or(Where orWhere) {
        where = new StringBuilder(String.format("( %s OR %s )", where.toString(),
                orWhere.toString()));
        return this;
    }

    private static String toSafeString(Object o) {
        return o instanceof String  ? DatabaseUtils.sqlEscapeString(o.toString()) : o.toString();
    }

    @Override
    public String toString() {
        return where.toString();
    }

    private enum Operator {
        Equal("="),
        NotEqual("!="),
        GreaterThan(">"),
        GreaterThanOrEqual(">="),
        LessThan("<"),
        LessThanOrEqual("<="),
        Like(" LIKE "),
        NotLike(" NOT LIKE "),
        Is(" IS "),
        IsNot(" IS NOT "),
        In(" IN "),
        NotIn(" NOT IN ");

        private final String value;

        Operator(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
