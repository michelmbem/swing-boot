package org.addy.swingboot.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.addy.swingboot.model.FilmLength;

@Converter(autoApply = true)
public class FilmLengthConverter implements AttributeConverter<FilmLength, Integer> {
    @Override
    public Integer convertToDatabaseColumn(FilmLength attribute) {
        return attribute != null ? attribute.minutes() : null;
    }

    @Override
    public FilmLength convertToEntityAttribute(Integer dbData) {
        return dbData != null ? new FilmLength(dbData) : null;
    }
}
