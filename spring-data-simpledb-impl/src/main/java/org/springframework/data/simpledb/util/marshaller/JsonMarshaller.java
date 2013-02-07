package org.springframework.data.simpledb.util.marshaller;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.mrbean.MrBeanModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Marshall and unmarshall objects, collections and map field wrappers
 */
public class JsonMarshaller {

    private static final Logger log = LoggerFactory.getLogger(JsonMarshaller.class);

    private static JsonMarshaller instance;
    private ObjectMapper jsonMapper;

    private JsonMarshaller() {
        JsonFactory factory = new JsonFactory();
        jsonMapper = new ObjectMapper(factory);
        JsonUnknownPropertyHandler jsonUnknownPropertyHandler = new JsonUnknownPropertyHandler();
        jsonMapper.getDeserializationConfig().addHandler(jsonUnknownPropertyHandler);
        jsonMapper.registerModule(new MrBeanModule());
    }

    public static JsonMarshaller getInstance() {
        if (instance == null) {
            instance = new JsonMarshaller();
        }

        return instance;
    }

    public Object unmarshallWrapperObject(String input) {
        Wrapper unmarshalledWrapper = unmarshall(input, Wrapper.class);

        String className = unmarshalledWrapper.getAttributeClassName();
        try {
            Class objectClass = Class.forName(className);
            String marshalledAttributes = doMarshall(unmarshalledWrapper.getAttributeContent());
            return unmarshall(marshalledAttributes, objectClass);
        } catch (ClassNotFoundException e) {
            throw new MappingException(e.getMessage(), e);
        }
    }

    <T> T unmarshall(String jsonString, Class<T> objectClass) {
        Assert.notNull(jsonString);

        T unmarshalledObject;
        try {
            unmarshalledObject = jsonMapper.readValue(jsonString, objectClass);
        } catch (IOException e) {
            //in case of error, if the required class is string, just return the original input
            if (objectClass.equals(String.class)) {
                return (T) jsonString;
            }

            throw new MappingException("Exception occurred while unmarshalling Object from SimpleDB!", e);
        }

        return unmarshalledObject;
    }

    public Object unmarshallWrapperCollection(String input) {
        Wrapper unmarshalledWrapper = unmarshall(input, Wrapper.class);

        String className = unmarshalledWrapper.getAttributeClassName();
        String genericType = unmarshalledWrapper.getGenericValueClassName();
        try {
            Class<? extends Collection> objectClass = (Class<Collection>) Class.forName(className);
            Class<?> genericTypeClass = Class.forName(genericType);
            String marshalledAttributes = doMarshall(unmarshalledWrapper.getAttributeContent());
            return unmarshallCollection(marshalledAttributes, objectClass, genericTypeClass);
        } catch (ClassNotFoundException e) {
            throw new MappingException(e.getMessage(), e);
        }
    }

    private <T extends Collection> T unmarshallCollection(String jsonString, Class<T> collectionType, Class<?> genericType) {
        Assert.notNull(jsonString);
        try {
            return (T) jsonMapper.readValue(jsonString, jsonMapper.getTypeFactory().constructCollectionType(collectionType, genericType));
        } catch (IOException e) {
            throw new MappingException("Could not unmarshallWrapperObject collection: " + jsonString, e);
        }
    }

    public Object unmarshallWrapperMap(String input) {
        Wrapper unmarshalledWrapper = unmarshall(input, Wrapper.class);

        String className = unmarshalledWrapper.getAttributeClassName();
        String genericType = unmarshalledWrapper.getGenericValueClassName();
        String keyType = unmarshalledWrapper.getGenericKeyClassName();
        try {
            Class<? extends Map> objectClass = (Class<Map>) Class.forName(className);
            Class<?> genericTypeClass = Class.forName(genericType);
            Class<?> genericKeyType = Class.forName(keyType);
            String marshalledAttributes = doMarshall(unmarshalledWrapper.getAttributeContent());
            return unmarshallMap(marshalledAttributes, objectClass, genericKeyType, genericTypeClass);
        } catch (ClassNotFoundException e) {
            throw new MappingException(e.getMessage(), e);
        }
    }

    private <T extends Map> T unmarshallMap(String jsonString, Class<T> collectionType, Class<?> genericType, Class<?> genericKeyType) {
        Assert.notNull(jsonString);
        try {
            return (T) jsonMapper.readValue(jsonString, jsonMapper.getTypeFactory().constructMapType(collectionType, genericKeyType, genericType));
        } catch (IOException e) {
            throw new MappingException("Could not unmarshallWrapperObject collection: " + jsonString, e);
        }
    }

    public <T> String marshall(T input) {
        Wrapper inputWrapper = new Wrapper();
        inputWrapper.setAttributeClassName(input.getClass().getName());
        inputWrapper.setAttributeContent(input);

        return doMarshall(inputWrapper);
    }


    public <T> String marshall(T input, String genericValueClassName) {
        Wrapper inputWrapper = new Wrapper();
        inputWrapper.setAttributeClassName(input.getClass().getName());
        inputWrapper.setAttributeContent(input);
        inputWrapper.setGenericValueClassName(genericValueClassName);

        return doMarshall(inputWrapper);
    }

    public <T> String marshall(T input, String genericValueClassName, String genericKeyClassName) {
        Wrapper inputWrapper = new Wrapper();
        inputWrapper.setAttributeClassName(input.getClass().getName());
        inputWrapper.setAttributeContent(input);
        inputWrapper.setGenericValueClassName(genericValueClassName);
        inputWrapper.setGenericKeyClassName(genericKeyClassName);

        return doMarshall(inputWrapper);
    }


    public <T> String doMarshall(T input) {
        Assert.notNull(input);
        try {
            return jsonMapper.writeValueAsString(input);
        } catch (Exception e) {
            throw new MappingException(e.getMessage(), e);
        }
    }

}
