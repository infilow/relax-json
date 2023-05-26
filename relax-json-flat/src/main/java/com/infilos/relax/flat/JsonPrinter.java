package com.infilos.relax.flat;

/**
 * Reprints any JSON input into minimal or pretty form.
 */
public class JsonPrinter {
    private JsonPrinter() {
    }

    /**
     * Returns a minimal print JSON of any JSON input.
     */
    public static String printMinimal(String json) {
        if (json == null) {
            throw new NullPointerException();
        }

        StringBuilder minimalPrintBuilder = new StringBuilder();
        boolean inQuote = false;

        for (char jsonChar : json.toCharArray()) {
            if (jsonChar == '"') {
                inQuote = !inQuote;
                minimalPrintBuilder.append(jsonChar);
            } else {
                if (inQuote || !Character.toString(jsonChar).matches("\\s")) {
                    minimalPrintBuilder.append(jsonChar);
                }
            }
        }

        return minimalPrintBuilder.toString();
    }

    /**
     * Returns a pretty print JSON of any JSON input.
     */
    public static String printPretty(String json) {
        return printPretty(json, "  ");
    }

    /**
     * Returns a pretty print JSON of any JSON input.
     */
    public static String printPretty(String json, String indentStr) {
        if (json == null) {
            throw new NullPointerException();
        }
        StringBuilder prettyPrintBuilder = new StringBuilder();

        int indentLevel = 0;
        boolean inQuote = false;
        boolean inBracket = false;
        for (char jsonChar : json.toCharArray()) {
            switch (jsonChar) {
                case '"':
                    inQuote = !inQuote;
                    prettyPrintBuilder.append(jsonChar);
                    break;
                case '{':
                    prettyPrintBuilder.append(jsonChar);
                    if (!inQuote) {
                        indentLevel++;
                        appendNewLine(prettyPrintBuilder, indentLevel, indentStr);
                    }
                    break;
                case '}':
                    if (!inQuote) {
                        indentLevel--;
                        appendNewLine(prettyPrintBuilder, indentLevel, indentStr);
                    }
                    prettyPrintBuilder.append(jsonChar);
                    break;
                case '[':
                    prettyPrintBuilder.append(jsonChar);
                    if (!inQuote) {
                        inBracket = true;
                        prettyPrintBuilder.append(' ');
                    }
                    break;
                case ']':
                    if (!inQuote) {
                        inBracket = false;
                        prettyPrintBuilder.append(' ');
                    }
                    prettyPrintBuilder.append(jsonChar);
                    break;
                case ',':
                    prettyPrintBuilder.append(jsonChar);
                    if (!inQuote) {
                        if (inBracket) {
                            prettyPrintBuilder.append(' ');
                        } else {
                            appendNewLine(prettyPrintBuilder, indentLevel, indentStr);
                        }
                    }
                    break;
                case ':':
                    if (inQuote) {
                        prettyPrintBuilder.append(jsonChar);
                    } else {
                        prettyPrintBuilder.append(' ');
                        prettyPrintBuilder.append(jsonChar);
                        prettyPrintBuilder.append(' ');
                    }
                    break;
                default:
                    if (inQuote || !Character.toString(jsonChar).matches("\\s")) {
                        prettyPrintBuilder.append(jsonChar);
                    }
            }
        }

        return prettyPrintBuilder.toString();
    }

    private static void appendNewLine(StringBuilder stringBuilder, int indentLevel, String indentStr) {
        stringBuilder.append('\n');
        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append(indentStr);
        }
    }
}
