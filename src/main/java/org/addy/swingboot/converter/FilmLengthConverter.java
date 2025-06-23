package org.addy.swingboot.converter;

import org.addy.simpletable.column.converter.CellConverter;

public class FilmLengthConverter implements CellConverter {
    @Override
    public Object model2view(Object modelValue, Object rowItem) {
        if (modelValue == null) return "";

        var length = (int) modelValue;
        var sb = new StringBuilder();
        boolean hasHours = false;

        if (length >= 60) {
            hasHours = true;
            sb.append(length / 60).append(" h");
            length %= 60;
        }

        if (hasHours) sb.append(' ');
        sb.append(String.format("%02d", length)).append(" min");

        return sb.toString();
    }
}
