package ru.wwerlosh.converter;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.wwerlosh.converter.DomainJsonConverter;
import ru.wwerlosh.converter.JsonConverter;

import static org.junit.jupiter.api.Assertions.*;

public class DomainJsonConverterTest {

    private JsonConverter<String> jsonConverter;
    private Set<String> data;

    @BeforeEach
    public void init() {
        jsonConverter = new DomainJsonConverter<>();
        data = new HashSet<>();
        data.add("ex.com");
        data.add("xe.com");
        data.add("de.eu");
    }

    @Test
    public void domainJsonConverter_test() {
        String expected = "{\n" + "\"sites\": " + "[\"de.eu\", \"ex.com\", \"xe.com\"]\n" + "}";
        String json = jsonConverter.convert(data);

        assertEquals(expected, json);
    }
}
