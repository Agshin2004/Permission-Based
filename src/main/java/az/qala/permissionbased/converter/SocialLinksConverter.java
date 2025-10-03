package az.qala.permissionbased.converter;

import az.qala.permissionbased.model.enums.Socials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter
public class SocialLinksConverter implements AttributeConverter<Map<Socials, String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<Socials, String> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Map<Socials, String> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<Socials, String>>() {
            }); // instantiating anonymous inner class
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
