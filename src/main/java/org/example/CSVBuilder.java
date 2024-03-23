package org.example;

public final class CSVBuilder {
    private String str = "";

    public CSVBuilder endRow() {
        if (str.charAt(str.length() - 1) != '\n') {
            if (str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }
            str += '\n';
        }

        return this;
    }

    public CSVBuilder addColumn(String data) {
        str += data + ',';

        return this;
    }

    public String toString() {
        return str;
    }
}
