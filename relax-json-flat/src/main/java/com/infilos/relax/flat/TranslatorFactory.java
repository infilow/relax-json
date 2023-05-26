package com.infilos.relax.flat;

import org.apache.commons.text.translate.CharSequenceTranslator;

/**
 * Anyone can provide their own {@link StringEscapePolicy} by implementing a {@link TranslatorFactory}.
 */
public interface TranslatorFactory {

    /**
     * Returns a {@link CharSequenceTranslator}
     *
     * @return {@link CharSequenceTranslator}
     */
    CharSequenceTranslator getTranslator();
}
