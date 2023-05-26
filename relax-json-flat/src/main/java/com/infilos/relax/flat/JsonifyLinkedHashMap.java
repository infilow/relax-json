package com.infilos.relax.flat;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.text.translate.CharSequenceTranslator;

/**
 * Enganced LinkedHashMap with an override jsonify toString method.
 */
public class JsonifyLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;

    private CharSequenceTranslator translator =
        StringEscapePolicy.DEFAULT.getTranslator();

    public JsonifyLinkedHashMap() {
    }

    public JsonifyLinkedHashMap(Map<K, V> map) {
        super(map);
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
        sb.append('{');
        for (Map.Entry<K, V> mem : entrySet()) {
            sb.append('"');
            sb.append(translator.translate((String) mem.getKey()));
            sb.append('"');
            sb.append(':');
            if (mem.getValue() instanceof String) {
                sb.append('"');
                sb.append(translator.translate((String) mem.getValue()));
                sb.append('"');
            } else if (mem.getValue() instanceof Collection) {
                sb.append(new JsonifyArrayList<>((Collection<?>) mem.getValue()));
            } else if (mem.getValue() instanceof Map) {
                sb.append(new JsonifyLinkedHashMap<>((Map<?, ?>) mem.getValue()));
            } else {
                sb.append(mem.getValue());
            }
            sb.append(',');
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        sb.append('}');

        return sb.toString();
    }
}
