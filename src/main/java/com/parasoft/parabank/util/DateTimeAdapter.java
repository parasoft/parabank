/*
 * (C) Copyright Parasoft Corporation 2017.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Parasoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package com.parasoft.parabank.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateTimeAdapter extends XmlAdapter<String, Date> {
    public static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .withZone(ZoneOffset.UTC);

    @Override
    public String marshal(Date inputDate) throws Exception {
        // use Instant.ofEpochMilli to work around Date being an instance of java.sql.Date
        return OUTPUT_FORMAT.format(Instant.ofEpochMilli(inputDate.getTime()));
    }

    @Override
    public Date unmarshal(String inputDateStr) throws Exception {
        return dateFromString(inputDateStr);
    }

    public static final Date dateFromString(String inputDateStr) {
        final DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        final LocalDateTime localDateTime = LocalDateTime.parse(inputDateStr, dtf);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
