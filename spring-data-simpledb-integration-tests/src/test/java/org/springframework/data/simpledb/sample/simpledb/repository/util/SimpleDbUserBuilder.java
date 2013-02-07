package org.springframework.data.simpledb.sample.simpledb.repository.util;

import org.springframework.data.simpledb.sample.simpledb.domain.JSONCompatibleClass;
import org.springframework.data.simpledb.sample.simpledb.domain.SimpleDbUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleDbUserBuilder {

    public static SimpleDbUser createUserWithSampleAttributes(String itemName) {
        SimpleDbUser user = new SimpleDbUser();
        {
            user.setItemName(itemName);
            
            user.setPrimitiveField(0.01f);

            user.setCoreField("tes_string$");

            List<Integer> list = new ArrayList<>();
            list.add(Integer.valueOf(123));
            list.add(Integer.valueOf(23));
            user.setCoreTypeList(list);

            user.setPrimitiveArrayField(new long[]{1234L});

            final SimpleDbUser.NestedEntity nestedEntity = new SimpleDbUser.NestedEntity();
            {
            	nestedEntity.setNestedPrimitiveField(11);
            }

            user.setNestedEntity(nestedEntity);


            List<String> sampleJSONList = new LinkedList<>();
            sampleJSONList.add("JSON");

            user.setObjectField(sampleJSONList);

            user.setObjectList( buildListOfObjects());
        }
        return user;
    }

    private static ArrayList<JSONCompatibleClass> buildListOfObjects() {
        ArrayList<JSONCompatibleClass> listOfObjects = new ArrayList<JSONCompatibleClass>();
        JSONCompatibleClass sampleElement = new JSONCompatibleClass();
        sampleElement.setName("SAMPLE");
        listOfObjects.add(sampleElement);
        return listOfObjects;
    }

    public static List<SimpleDbUser> createListOfItems(int length) {
        List<SimpleDbUser> list = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            String itemName = "Item_" + i;
            SimpleDbUser user = createUserWithSampleAttributes(itemName);
            list.add(user);
        }
        return list;
    }
}
