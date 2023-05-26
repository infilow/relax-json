package com.infilos.relax.flat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.text.translate.CharSequenceTranslator;

/**
 * Enganced ArrayList with an override jsonify toString method.
 */
public class JsonifyArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    private CharSequenceTranslator translator =
        StringEscapePolicy.DEFAULT.getTranslator();

    public JsonifyArrayList() {
    }

    public JsonifyArrayList(Collection<E> coll) {
        super(coll);
    }

    public void setTranslator(CharSequenceTranslator translator) {
        this.translator = translator;
    }

    public String toString(PrintMode printMode) {
        if (printMode == PrintMode.PRETTY) {
            return JsonPrinter.printPretty(toString());
        }
        
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (E e : this) {
            if (e instanceof String) {
                sb.append('"');
                sb.append(translator.translate((String) e));
                sb.append('"');
            } else if (e instanceof Collection) {
                sb.append(new JsonifyArrayList<>((Collection<?>) e));
            } else if (e instanceof Map) {
                sb.append(new JsonifyLinkedHashMap<>((Map<?, ?>) e));
            } else {
                sb.append(e);
            }
            sb.append(',');
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        sb.append(']');

        return sb.toString();
    }
}
