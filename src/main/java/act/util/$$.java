package act.util;

/*-
 * #%L
 * ACT Framework
 * %%
 * Copyright (C) 2014 - 2018 ActFramework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import act.Act;
import act.data.*;
import com.alibaba.fastjson.JSON;
import org.joda.time.*;
import org.osgl.$;
import org.osgl.util.*;

import java.sql.Time;
import java.util.*;

/**
 * An extension to osgl $ class
 */
public class $$ {

    private static Set<Class> dateTimeTypes = $.cast(C.set(Date.class, java.sql.Date.class, DateTime.class, LocalDate.class, LocalTime.class, LocalDateTime.class, Time.class));

    private static Map<Class, ValueObject.Codec> codecs = $.cast(C.Map(
            DateTime.class, Act.getInstance(JodaDateTimeCodec.class),
            LocalDateTime.class, Act.getInstance(JodaLocalDateTimeCodec.class),
            LocalDate.class, Act.getInstance(JodaLocalDateCodec.class),
            LocalTime.class, Act.getInstance(JodaLocalTimeCodec.class)
    ));

    public static boolean isDateTimeType(Class<?> type) {
        return dateTimeTypes.contains(type);
    }

    public static String toString(Object v, boolean directToString) {
        if (directToString) {
            ValueObject.Codec codec = codecs.get(v.getClass());
            return null == codec ? v.toString() : codec.toString(v);
        }
        return JSON.toJSONString(v);
    }

    public static String toString(Object v) {
        if (null == v) {
            return "";
        }
        Class<?> c = v.getClass();
        return toString(v, shouldUseToString(c));
    }

    public static boolean shouldUseToString(Class<?> type) {
        return ($.isSimpleType(type) && !type.isArray()) || isDateTimeType(type);
    }

    private static $.Transformer<String, String> evaluator = new $.Transformer<String, String>() {
        @Override
        public String transform(String s) {
            return S.string(Act.appConfig().getIgnoreCase(s));
        }
    };

    public static String processStringSubstitution(String s) {
        return processStringSubstitution(s, evaluator);
    }

    public static String processStringSubstitution(String s, $.Func1<String, String> evaluator) {
        if (S.blank(s)) {
            return "";
        }
        int n = s.indexOf("${");
        if (n < 0) {
            return s;
        }
        int a = 0;
        int z = n;
        S.Buffer buf = S.buffer();
        while (true) {
            buf.append(s.substring(a, z));
            n = s.indexOf("}", z);
            a = n;
            String key = s.substring(z + 2, a);
            buf.append(evaluator.apply(key));
            n = s.indexOf("${", a);
            if (n < 0) {
                buf.append(s.substring(a + 1));
                return buf.toString();
            }
            z = n;
        }
    }


}
