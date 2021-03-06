package org.springframework.data.simpledb.core.entity;

import org.springframework.data.simpledb.reflection.FieldType;
import org.springframework.data.simpledb.reflection.FieldTypeIdentifier;

import java.io.Serializable;
import java.lang.reflect.Field;

public final class FieldWrapperFactory {

	private FieldWrapperFactory() {
		/* utility class */
	}

	public static <T, ID extends Serializable> AbstractFieldWrapper<T, ID> createFieldWrapper(final Field field,
			final EntityWrapper<T, ID> parent, final boolean isNewParent) {
		if(FieldTypeIdentifier.isOfType(field, FieldType.PRIMITIVE)) {
			return createSimpleFieldWrapper(field, parent, isNewParent);
		} else if(FieldTypeIdentifier.isOfType(field, FieldType.CORE_TYPE)) {
			return createSimpleFieldWrapper(field, parent, isNewParent);
		} else if(FieldTypeIdentifier.isOfType(field, FieldType.COLLECTION)) {
			return createCollectionFieldWrapper(field, parent, isNewParent);
		} else if(FieldTypeIdentifier.isOfType(field, FieldType.ARRAY)) {
			return createArrayFieldWrapper(field, parent, isNewParent);
		} else if(FieldTypeIdentifier.isOfType(field, FieldType.NESTED_ENTITY)) {
			return createNestedEntityFieldWrapper(field, parent, isNewParent);
		} else if(FieldTypeIdentifier.isOfType(field, FieldType.REFERENCE_ENTITY)) {
			return createReferenceEntityFieldWrapper(field, parent, isNewParent);
		} else if(FieldTypeIdentifier.isOfType(field, FieldType.MAP)) {
			return createMapFieldWrapper(field, parent, isNewParent);
		}

		return createObjectFieldWrapper(field, parent, isNewParent);
	}

	private static <T, ID extends Serializable> AbstractFieldWrapper<T, ID> createSimpleFieldWrapper(
			final Field field, final EntityWrapper<T, ID> parent, final boolean isNewParent) {
		return new SimpleFieldWrapper<T, ID>(field, parent, isNewParent);
	}


	private static <T, ID extends Serializable> AbstractFieldWrapper<T, ID> createArrayFieldWrapper(
			final Field field, final EntityWrapper<T, ID> parent, final boolean isNewParent) {
		return new JSONFieldWrapper<T, ID>(field, parent, isNewParent);
	}

	private static <T, ID extends Serializable> AbstractFieldWrapper<T, ID> createCollectionFieldWrapper(
			final Field field, final EntityWrapper<T, ID> parent, final boolean isNewParent) {
		return new JSONFieldWrapper<T, ID>(field, parent, isNewParent);
	}

	private static <T, ID extends Serializable> AbstractFieldWrapper<T, ID> createNestedEntityFieldWrapper(
			final Field field, final EntityWrapper<T, ID> parent, final boolean isNewParent) {
		return new NestedEntityFieldWrapper<T, ID>(field, parent, isNewParent);
	}
	
	private static <T, ID extends Serializable> AbstractFieldWrapper<T, ID> createReferenceEntityFieldWrapper(
			final Field field, final EntityWrapper<T, ID> parent, final boolean isNewParent) {
		return new ReferenceEntityFieldWrapper<T, ID>(field, parent, isNewParent);
	}

	private static <T, ID extends Serializable> AbstractFieldWrapper<T, ID> createMapFieldWrapper(final Field field,
			final EntityWrapper<T, ID> parent, final boolean isNewParent) {
		return new JSONFieldWrapper<T, ID>(field, parent, isNewParent);
	}

	private static <T, ID extends Serializable> AbstractFieldWrapper<T, ID> createObjectFieldWrapper(
			final Field field, final EntityWrapper<T, ID> parent, final boolean isNewParent) {
		return new JSONFieldWrapper<T, ID>(field, parent, isNewParent);
	}

}
