package org.springframework.data.simpledb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.simpledb.annotation.Attributes;
import org.springframework.data.simpledb.annotation.DomainPrefix;
import org.springframework.data.simpledb.core.SimpleDbConfig;
import org.springframework.data.simpledb.core.entity.field.FieldType;
import org.springframework.data.simpledb.core.entity.field.FieldTypeIdentifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
public final class MetadataParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataParser.class);
    public static final String FIELD_NAME_DEFAULT_ID = "id";

    private MetadataParser() {
        //Utility class
    }

    /**
     * Domain name are computed based on class names: UserJob -> user_job
     *
     * @param clazz
     * @return
     */
    public static String getDomain(Class<?> clazz) {
        StringBuilder ret = new StringBuilder();

        String domainPrefix = getDomainPrefix(clazz);
        if (domainPrefix != null) {
            ret.append(domainPrefix);
            ret.append(".");
        }

        String camelCaseString = clazz.getSimpleName();

        ret.append(StringUtil.toLowerFirstChar(camelCaseString));

        return ret.toString();
    }

    public static String getItemName(Object object) {
        Field idField = getIdField(object);

        if (idField != null) {
            try {
                idField.setAccessible(true);
                return (String) idField.get(object);
            } catch (IllegalAccessException e) {
                throw new MappingException("Could not read simpleDb id field", e);
            }
        }

        return null;
    }

    public static Field getIdField(Object object) {
        return getIdField(object.getClass());
    }

    public static Field getIdField(Class<?> clazz) {
        Field idField = null;

        for (Field f : clazz.getDeclaredFields()) {
            //named id or annotated with Id
            if (f.getName().equals(FIELD_NAME_DEFAULT_ID) || f.getAnnotation(Id.class) != null) {
                if (idField != null) {
                    throw new MappingException("Multiple id fields detected for class " + clazz.getName());
                }
                idField = f;
            }

        }

        return idField;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getAttributes(Object object) {
        Class<?> clazz = object.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            Attributes attributes = f.getAnnotation(Attributes.class);
            if (attributes != null) {
                try {
                    f.setAccessible(true);
                    return (Map<String, String>) f.get(object);
                } catch (IllegalAccessException e) {
                    LOGGER.error("Could not read simpleDb attributes", e);
                }
            }
        }

        return null;
    }

    public static Field getAttributesField(Object object) {
        Class<?> clazz = object.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            //annotated with Attributes
            Attributes attributes = f.getAnnotation(Attributes.class);
            if (attributes != null) {
                return f;
            }
        }

        return null;
    }

    public static List<Field> getSupportedFields(Object object) {
    	return getSupportedFields(object, FieldType.PRIMITIVE, FieldType.CORE_TYPE);
    }

    public static List<Field> getPrimitiveCollectionFields(Object object) {
    	return getSupportedFields(object, FieldType.PRIMITIVE_ARRAY);
    }

    private static List<Field> getSupportedFields(Object object, FieldType... fieldTypes) {
        List<Field> fieldList = new ArrayList<>();
        for(Field field : object.getClass().getDeclaredFields()) {

            // Filter declared fields in this entity bean while serialization
               if( FieldTypeIdentifier.hasDeclaredGetterAndSetter(field, object.getClass())
                    && field.getAnnotation(Attributes.class) == null
                    && field.getAnnotation(Transient.class) == null
                    && !(field.equals(MetadataParser.getIdField(object)))
                    && FieldTypeIdentifier.isOfType(field, fieldTypes)) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    public static List<Field> getNestedDomainFields(Object object) {
        final List<Field> fieldList = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            if (isNestedDomainField(field, object)) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    public static boolean isNestedDomainField(Field field, Object object) {
        return FieldTypeIdentifier.isOfType(field, FieldType.NESTED_ENTITY);
    }

    private static String getDomainPrefix(Class<?> clazz) {
        DomainPrefix domainPrefix = (DomainPrefix) clazz.getAnnotation(DomainPrefix.class);
        if (domainPrefix != null) {
            return domainPrefix.value();
        }

        return SimpleDbConfig.getInstance().getDomainPrefix();
    }
}