/*
 * Copyright 2021 Kato Shinya.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.thinkit.bot.filater.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mongodb.lang.NonNull;

import org.thinkit.bot.filater.catalog.DateFormat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * It provides common functions for date operation.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    public static Date now() {
        return new Date();
    }

    public static Date getDateAfter(@NonNull final DateFormat dateFormat, final long timeMs, final int days) {
        final Calendar baseDate = Calendar.getInstance();
        baseDate.setTime(toDate(dateFormat, timeMs));
        baseDate.add(Calendar.DATE, days);

        return toDate(dateFormat, baseDate.getTime().getTime());
    }

    public static Date getDateAfter(@NonNull final DateFormat dateFormat, @NonNull final Date date, final int days) {
        final Calendar baseDate = Calendar.getInstance();
        baseDate.setTime(date);
        baseDate.add(Calendar.DATE, days);

        return toDate(dateFormat, baseDate.getTime().getTime());
    }

    public static Date toDate(@NonNull final DateFormat dateFormat, final long timeMs) {
        try {
            return new SimpleDateFormat(DateFormat.YYYY_MM_DD_HH_MM_SS.getTag()).parse(toString(dateFormat, timeMs));
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String toString(@NonNull final DateFormat dateFormat, final Date date) {
        return toString(dateFormat, date.getTime());
    }

    private static String toString(@NonNull final DateFormat dateFormat, final long timeMs) {
        return new SimpleDateFormat(dateFormat.getTag()).format(timeMs);
    }
}
