package com.infilos.relax.flat;

import static java.util.Collections.unmodifiableMap;

import java.util.Collections;
import java.util.HashMap;

import com.infilos.relax.JsonFlattener;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.JavaUnicodeEscaper;
import org.apache.commons.text.translate.LookupTranslator;

/**
 * JSON string escape policy of the {@link JsonFlattener}.
 */
public enum StringEscapePolicy implements TranslatorFactory {
    /**
     * Escapes all JSON special characters and Unicode.
     */
    ALL(StringEscapeUtils.ESCAPE_JSON),

    /**
     * Escapes all JSON special characters and Unicode but slash('/').
     */
    ALL_BUT_SLASH(new AggregateTranslator(new LookupTranslator(
        unmodifiableMap(new HashMap<CharSequence, CharSequence>() {
            private static final long serialVersionUID = 1L;

            {
                put("\"", "\\\"");
                put("\\", "\\\\");
            }
        })), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE),
        JavaUnicodeEscaper.outsideOf(32, 0x7f))),

    /**
     * Escapes all JSON special characters but Unicode.
     */
    ALL_BUT_UNICODE(new AggregateTranslator(new LookupTranslator(
        unmodifiableMap(new HashMap<CharSequence, CharSequence>() {
            private static final long serialVersionUID = 1L;

            {
                put("\"", "\\\"");
                put("\\", "\\\\");
                put("/", "\\/");
            }
        })), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE))),

    /**
     * Escapes all JSON special characters but slash('/') and Unicode.
     */
    ALL_BUT_SLASH_AND_UNICODE(new AggregateTranslator(new LookupTranslator(
        Collections.unmodifiableMap(new HashMap<CharSequence, CharSequence>() {
            private static final long serialVersionUID = 1L;

            {
                put("\"", "\\\"");
                put("\\", "\\\\");
            }
        })), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE))),

    /**
     * Escapes all JSON special characters but slash('/') and Unicode.
     */
    DEFAULT(new AggregateTranslator(new LookupTranslator(
        unmodifiableMap(new HashMap<CharSequence, CharSequence>() {
            private static final long serialVersionUID = 1L;

            {
                put("\"", "\\\"");
                put("\\", "\\\\");
            }
        })), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE)));

    private final CharSequenceTranslator translator;

    StringEscapePolicy(CharSequenceTranslator translator) {
        this.translator = translator;
    }

    @Override
    public CharSequenceTranslator getTranslator() {
        return translator;
    }
}
