package com.exacttarget.fuelsdk.model.converter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.AbstractArrayConverter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;

import com.exacttarget.fuelsdk.ETSdkException;
import com.exacttarget.fuelsdk.annotations.InternalSoapField;
import com.exacttarget.fuelsdk.annotations.InternalSoapType;
import com.exacttarget.fuelsdk.internal.APIObject;
import com.exacttarget.fuelsdk.internal.APIProperty;
import com.exacttarget.fuelsdk.internal.DataExtension;
import com.exacttarget.fuelsdk.internal.DataExtension.Fields;
import com.exacttarget.fuelsdk.internal.AccountTypeEnum;
import com.exacttarget.fuelsdk.internal.DataExtensionField;
import com.exacttarget.fuelsdk.internal.DataExtensionFieldType;
import com.exacttarget.fuelsdk.internal.DataExtensionObject.Keys;
import com.exacttarget.fuelsdk.internal.DataFolder;
import com.exacttarget.fuelsdk.internal.EmailType;
import com.exacttarget.fuelsdk.internal.EventType;
import com.exacttarget.fuelsdk.internal.LayoutType;
import com.exacttarget.fuelsdk.internal.ListClassificationEnum;
import com.exacttarget.fuelsdk.internal.ListTypeEnum;
import com.exacttarget.fuelsdk.internal.ObjectExtension;
import com.exacttarget.fuelsdk.internal.SubscriberStatus;
import com.exacttarget.fuelsdk.model.ETAccountType;
import com.exacttarget.fuelsdk.model.ETDataExtension;
import com.exacttarget.fuelsdk.model.ETDataExtensionColumn;
import com.exacttarget.fuelsdk.model.ETDataExtensionFieldType;
import com.exacttarget.fuelsdk.model.ETEmailType;
import com.exacttarget.fuelsdk.model.ETEventType;
import com.exacttarget.fuelsdk.model.ETFolder;
import com.exacttarget.fuelsdk.model.ETLayoutType;
import com.exacttarget.fuelsdk.model.ETListClassification;
import com.exacttarget.fuelsdk.model.ETListType;
import com.exacttarget.fuelsdk.model.ETObject;
import com.exacttarget.fuelsdk.model.ETSubscriberStatus;

public class ObjectConverter {
    static {
        ConvertUtilsBean convertUtils = BeanUtilsBean.getInstance().getConvertUtils();
        /// Enums
        convertUtils.register(new Converter() {
            public Object convert(Class arg0, Object obj) {
                if (obj == null) return null;
                if (arg0 == XMLGregorianCalendar.class) {
                    return ((XMLGregorianCalendar) obj).toGregorianCalendar().getTime();
                }
                return null;
            }
        }, Date.class);

        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == EmailType.class) {
                    return ETEmailType.valueOf((value).toString());
                }
                return null;
            }
        }, ETEmailType.class);

        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == SubscriberStatus.class) {
                    return ETSubscriberStatus.valueOf((value).toString());
                }
                return null;
            }
        }, ETSubscriberStatus.class);

        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == ListClassificationEnum.class) {
                    return ETListClassification.valueOf((value).toString());
                }
                return null;
            }
        }, ETListClassification.class);

        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == ListTypeEnum.class) {
                    return ETListType.valueOf((value).toString());
                }
                return null;
            }
        }, ETListType.class);

        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == LayoutType.class) {
                    return ETLayoutType.valueOf((value).toString());
                }
                return null;
            }
        }, ETLayoutType.class);
        
        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == EventType.class) {
                    return ETEventType.valueOf((value).toString());
                }
                return null;
            }
        }, ETEventType.class);
        
        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == AccountTypeEnum.class) {
                    return ETAccountType.valueOf((value).toString());
                }
                return null;
            }
        }, ETAccountType.class);
        
        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == DataExtensionFieldType.class) {
                    return ETDataExtensionFieldType.valueOf((value).toString());
                }
                return null;
            }
        }, ETDataExtensionFieldType.class);
        
        convertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) return null;
                if (type == ETDataExtensionFieldType.class) {
                    return DataExtensionFieldType.valueOf((value).toString());
                }
                return null;
            }
        }, DataExtensionFieldType.class);
        

        // TODO - make this generic instead of specific to DataFolder
        convertUtils.register(new Converter(){
            public Object convert(Class type, Object value) {
                try {
                    return convertFromEtObject((ETObject) value, type, false);
                }
                catch(Exception e) {
                    return null;
                }
            }
        }, DataFolder.class);

        // TODO - make this generic ET enum -> Internal enum instead of specific to SubscriberStatus
        convertUtils.register(new Converter(){
            public Object convert(Class type, Object value) {
                try {
                    return SubscriberStatus.valueOf(value.toString());
                }
                catch(Exception e) {
                    return null;
                }
            }
        }, SubscriberStatus.class);

        // TODO - make this generic instead of specific to ETFolder
        convertUtils.register(new Converter(){
            public Object convert(Class type, Object value) {
                try {
                    return convertToEtObject((APIObject) value, type, false);
                }
                catch(Exception e) {
                    return null;
                }
            }
        }, ETFolder.class);
        
        // TODO - make this generic instead of specific to ETFolder
        convertUtils.register(new Converter(){
            public Object convert(Class type, Object value) {
                try {
                    DataExtension.Fields fields = new Fields();
                    
                    List<ETDataExtensionColumn> columns = (List<ETDataExtensionColumn>) value;
                    
                    for(ETDataExtensionColumn col : columns) {
                    	fields.getField().add(convertFromEtObject(col, DataExtensionField.class, false));
                    }
                    
                    return fields;
                    
                }
                catch(Exception e) {
                    return null;
                }
            }
        }, DataExtension.Fields.class);
        
        // TODO - make this generic instead of specific to ETFolder
        convertUtils.register(new Converter(){
            public Object convert(Class type, Object value) {
                try {
                	Keys keys = new Keys();
                    
                    Map<String, String> columns = (Map<String, String>) value;
                    
                    for(String key : columns.keySet()) {
                    	APIProperty property = new APIProperty();
                    	property.setName(key);
                    	property.setValue(columns.get(key));
                    	
                    	keys.getKey().add(property);
                    }
                    
                    return keys;
                    
                }
                catch(Exception e) {
                    return null;
                }
            }
        }, Keys.class);
        
        // TODO - make this generic instead of specific to ETFolder
        convertUtils.register(new Converter(){
            public Object convert(Class type, Object value) {
                try {
                	ObjectExtension.Properties properties = new ObjectExtension.Properties();
                    
                    Map<String, String> columns = (Map<String, String>) value;
                    
                    for(String key : columns.keySet()) {
                    	APIProperty property = new APIProperty();
                    	property.setName(key);
                    	property.setValue(columns.get(key));
                    	
                    	properties.getProperty().add(property);
                    }
                    
                    return properties;
                    
                }
                catch(Exception e) {
                    return null;
                }
            }
        }, ObjectExtension.Properties.class);
        
        // TODO - make this generic instead of specific to ETFolder
        convertUtils.register(new Converter(){
            public Object convert(Class type, Object value) {
                try {
                	ObjectExtension.Properties properties = (ObjectExtension.Properties) value;
                    
                    Map<String, String> columns = new HashMap<String, String>();
                    
                    for (APIProperty property : properties.getProperty()) {
                    	columns.put(property.getName(), property.getValue());
                    }
                    
                    return columns;
                    
                }
                catch(Exception e) {
                    return null;
                }
            }
        }, Map.class);
        
        // TODO - make this generic instead of specific to ETFolder
        convertUtils.register(new ArrayConverter(ETDataExtensionColumn[].class, new Converter() {
			
			public Object convert(Class type, Object value) {
				try {
                    return convertToEtObject((APIObject) value, type, false);
                }
                catch(Exception e) {
                    return null;
                }
			}
		}), ETDataExtensionColumn[].class);
        
        // By default, IntegerConverter sets nulls as 0
        convertUtils.register(new IntegerConverter(null), Integer.class);
    }

    public static <T extends ETObject> T convertToEtObject(APIObject o, Class<T> toType, boolean isPatch)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Convert o to ETObject type by examining toType's @InternalField annotations
        T out = toType.newInstance();

        for(Map.Entry<String, String> props : createInternalToETPropertyMap(new HashMap<String, String>(), toType, isPatch).entrySet()) {
            BeanUtils.setProperty(out, props.getValue(), PropertyUtils.getProperty(o, resolvePropertyName(props.getKey())));
        }

        return out;
    }

    public static <T extends APIObject> T convertFromEtObject(ETObject o, Class<T> toType, boolean isPatch)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Convert o to APIObject type by examining o's @InternalField annotations
        T out = toType.newInstance();

        for(Map.Entry<String, String> props : createInternalToETPropertyMap(new HashMap<String, String>(), o.getClass(), isPatch).entrySet()) {
            BeanUtils.setProperty(out, resolvePropertyName(props.getKey()), PropertyUtils.getProperty(o, props.getValue()));
        }

        return out;
    }

    public static java.util.List<String> findSerializablePropertyNames(Class<? extends ETObject> type) throws NoSuchFieldException, ETSdkException {
        // This method would be much simpler to write if we assume all fields with @XmlElement are to be transmitted
        // We are under the current assumption that we only want to return those fields which have been explicitly marked
        InternalSoapType classAnnotation = type.getAnnotation(InternalSoapType.class);
        if(classAnnotation == null) {
            throw new ETSdkException("The type specified does not wrap an internal ET APIObject.");
        }
        Class internalType = classAnnotation.type();

        java.util.List<String> names = new java.util.ArrayList<String>();
        
        java.util.List<Field> fields = new ArrayList<Field>(Arrays.asList(type.getDeclaredFields()));
        if (null != type.getSuperclass()) {
        	Class superType = type.getSuperclass();
        	fields.addAll(Arrays.asList(superType.getDeclaredFields()));
        }

        for(Field declared : fields) {
            InternalSoapField propAnnotation = declared.getAnnotation(InternalSoapField.class);
            if(propAnnotation != null) {
                // This field has an @InternalField annotation, let's find the corresponding property in the APIObject class
                Field internalField;
                if (!propAnnotation.serializedName().equals("")) {
                	names.add(propAnnotation.serializedName());
                	continue;
                }
                try {
                	internalField = internalType.getDeclaredField(propAnnotation.name());
                } catch(NoSuchFieldException ex) {
                	internalField = internalType.getSuperclass().getDeclaredField(propAnnotation.name());
                }
                XmlElement element = internalField.getAnnotation(XmlElement.class);
                if(element != null) {
                    // This property is serializable, let's add it
                    names.add(element.name());
                }
                else {
                    // Optional datetimes are annotated with XmlElementRef, so let's check that as well
                    XmlElementRef elementRef = internalField.getAnnotation(XmlElementRef.class);
                    if(elementRef != null) {
                        // This property is serializable, let's add it
                        names.add(elementRef.name());
                    }
                }
            }
        }
        names.removeAll(Arrays.asList(classAnnotation.ignoredFields()));
        return names;
    }

    protected static Map<String, String> createInternalToETPropertyMap(Map<String, String> properties, Class<?> type, boolean isPatch) {
        for(Field f : type.getDeclaredFields()) {
            InternalSoapField fld = f.getAnnotation(InternalSoapField.class);
            if(fld != null && (!isPatch || !(isPatch && fld.ignoreOnPatch()))) {
                properties.put(fld.name(), f.getName());
            }
        }
        return type.getSuperclass() == null ? properties : createInternalToETPropertyMap(properties, type.getSuperclass(), isPatch);
    }

    protected static String resolvePropertyName(String beanPropertyName) {
    	
    	if (beanPropertyName.equals("id")) {
    		return "ID";
    	} else if (beanPropertyName.indexOf('.') > -1) {
    		return beanPropertyName.substring(0, beanPropertyName.indexOf('.'));
    	}
    	return beanPropertyName;
    }
}