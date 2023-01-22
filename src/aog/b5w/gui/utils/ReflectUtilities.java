package aog.b5w.gui.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for using Java reflection
 * @author Ed Webb
 * @version 
 * @since
 */
public class ReflectUtilities {

	public static Object getField(Object o, String field) {
		Class<?> clazz;
		Object obj;
		if (o.getClass().equals(Class.class)) {
			clazz = (Class<?>)o;
			obj = null;
		} else {
			clazz = o.getClass();
			obj = o;
		}
		try {
			Field fld = clazz.getField(field);
			if (fld != null) {
				return fld.get(obj);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
     * Returns the value of the object's property using reflection. The property may be
     * a complex property such as order.billing.address. The method will call the correct
     * method on each sub-object in turn.
     * @param o the object
     * @param property the name of the property
     * @return the value of the property
     */
    public static Object getProperty(Object o, String property) {
        String[] properties = property.split("\\.");
        Object cur = o;
        for (int i = 0; i < properties.length; i++) {
            cur = getProperty(cur, properties[i], new Object[] {});
        }
        return cur;
    }

    /**
     * Returns the value of the object's property using reflection. The property may be
     * a complex property such as order.billing.address. The method will call the correct
     * method on each sub-object in turn. The args argument should be the same size as the
     * number of elements in the property argument. It may contain nulls and may be shorter
     * if the corresponding property methods require no arguments.
     * @param o the object
     * @param property the name of the property
     * @param args an array of arrays of objects to be used as arguments for the property method calls.
     * @return the value of the property
     */
    public static Object getProperty(Object o, String property, Object[][] args) {
        String[] properties = property.split("\\.");
        Object cur = o;
        for (int i = 0; i < properties.length; i++) {
            if (args == null || args.length <= i || args[i] == null) {
                cur = getProperty(cur, properties[i], new Object[] {});
            } else {
                cur = getProperty(cur, properties[i], args[i]);
            }
        }
        return cur;
    }
    
    /**
     * Returns the value of the object's property using reflection
     * @param o the object
     * @param property the name of the property
     * @param args the arguments to pass to the property get method
     * @return the value of the property
     */
    public static Object getProperty(Object o, String property, Object args[]) {
        property = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
        Object result = invokeMethod(o,property,args);
        return result;
    }

    
    /**
     * Sets the value of the object's property using reflection
     * @param o the object
     * @param property the name of the property
     * @param args the arguments to pass to the property set method
     */
    public static void setProperty(Object o, String property, Object args[]) {
        property = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
        invokeMethod(o,property,args);
    }

    /**
     * Sets the value of the object's property using reflection
     * @param o the object
     * @param property the name of the property
     * @param args the arguments to pass to the property set method
     * @param classes the classes of the arguments to be passed
     */
    public static void setProperty(Object o, String property, Object[] args, Class<?>[] classes) {
        property = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
        invokeMethod(o,property,args, classes);
    }

    /**
     * Adds an object to the object's property using reflection
     * @param o the object
     * @param property the name of the property
     * @param args the arguments to pass to the property set method
     */
    public static void addProperty(Object o, String property, Object args[]) {
        property = "add" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length()-1);
        if (property.endsWith("ie")) {
            property = property.substring(0,property.length()-2) + "y";
        } else if (property.endsWith("xe")) {
        	property = property.substring(0,property.length()-1);
        }
        invokeMethod(o,property,args);
    }

    /**
     * Removes an object from the object's property using reflection
     * @param o the object
     * @param property the name of the property
     * @param args the arguments to pass to the property set method
     */
    public static void removeProperty(Object o, String property, Object args[]) {
        property = "remove" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length()-1);
        if (property.endsWith("ie")) {
            property = property.substring(0,property.length()-2) + "y";
        } else if (property.endsWith("xe")) {
        	property = property.substring(0,property.length()-1);
        }
        invokeMethod(o,property,args);
    }
    
    /**
     * Returns the value of the object's property's labels using reflection
     * @param o the object
     * @param property the name of the property
     * @param args the arguments to pass to the property get method
     * @return the value of the property
     */
    public static Object getLabels(Object o, String property, Object args[]) {
        property = "label" + property.substring(0, 1).toUpperCase() + property.substring(1);
        Object result = invokeMethod(o,property,args);
        return result;
    }

    /**
     * Returns the value of the object's property's values using reflection
     * @param o the object
     * @param property the name of the property
     * @param args the arguments to pass to the property get method
     * @return the value of the property
     */
    public static Object getValues(Object o, String property, Object args[]) {
        property = "value" + property.substring(0, 1).toUpperCase() + property.substring(1);
        Object result = invokeMethod(o,property,args);
        return result;
    }

    /**
     * Returns the class of the object's property
     * @param o the object
     * @param property the name of the property
     * @param args the arguments to pass to the property get method
     * @return the Class of the property (as returned by the get method)
     */
    public static Class<?> getPropertyClass(Object o, String property, Object args[]) {
        property = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
        Class<?> clazz = getMethodReturn(o, property, args);
        return clazz;
    }

    public static Class<?> getPropertyClass(Object o, String property, Object[][] args) {
        String[] properties = property.split("\\.");
        Object cur = o;
        for (int i = 0; i < properties.length - 1; i++) {
            if (args == null || args.length <= i || args[i] == null) {
                cur = getProperty(cur, properties[i], new Object[] {});
            } else {
                cur = getProperty(cur, properties[i], args[i]);
            }
        }
        int i = properties.length - 1;
        if (args == null || args.length <= i || args[i] == null) {
            return getPropertyClass(cur, properties[i], new Object[] {});
        } else {
            return getPropertyClass(cur, properties[i], args[i]);
        }
    }
    
    /**
     * Invokes the method of the object and returns a value
     * @param o the object
     * @param method the name of the method
     * @param args the arguments to pass to the method
     * @return null if the method is declared void else the object returned from the method
     */
    public static Object invokeMethod(Object o, String method, Object[] args, Class<?>[] classes) {
        try {

            Class<?> c = o.getClass();
            Method m = c.getMethod(method, classes);
            return m.invoke(o, args);
        } catch (IllegalArgumentException e) {
            printError(e, o, method, args);
        } catch (SecurityException e) {
            printError(e, o, method, args);
        } catch (IllegalAccessException e) {
            printError(e, o, method, args);
        } catch (InvocationTargetException e) {
            printError(e, o, method, args);
        } catch (NoSuchMethodException e) {
            printError(e, o, method, args);
        }
        return null;
    }

    /**
     * Invokes the method of the object and returns a value
     * @param o the object
     * @param method the name of the method
     * @param args the arguments to pass to the method
     * @return null if the method is declared void else the object returned from the method
     */
    public static Object invokeMethod(Object o, String method, Object[] args) {
        if (args == null) {
        	args = new Object[0];
        }
        
    	if (args.length == 1) {
        	return invokeMethod(o, method, args[0]);
        }
    	
    	try {
            Class<?>[] types = getArgsClass(args);
            Class<?> c;
            if (!(o instanceof Class)) { 
            	c = o.getClass();
            } else {
            	c = (Class<?>)o;
            }
            Method m = findMethod(c, method, types);
            return m.invoke(o, args);
        } catch (IllegalArgumentException e) {
            printError(e, o, method, args);
        } catch (SecurityException e) {
            printError(e, o, method, args);
        } catch (IllegalAccessException e) {
            printError(e, o, method, args);
        } catch (InvocationTargetException e) {
            printError(e, o, method, args);
        } catch (NullPointerException e) {
            printError(e, o, method, args);
        }
        return null;
    }
    
    private static void printError(Throwable t, Object o, String method, Object arg) {
    	printError(t, o, method, new Object[] {arg});
    }
    
    private static void printError(Throwable t, Object o, String method, Object[] args) {
    	System.err.println("ReflectUtilities.invokeMethod Error: " + t.getClass().getName() + " " + o.getClass().getName() + "." + method + "(" + printArgs(args) + ")");
    }
    
    private static String printArgs(Object[] args) {
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < args.length; i++) {
    		if (i > 0) {
    			sb.append(", ");
    		}
    		if (args[i] instanceof Class) {
    			sb.append(((Class<?>)args[i]).getName());
    		} else {
    			sb.append(args[i].getClass().getName());
    		}
    	}
    	return sb.toString();
    }
    
    /**
     * This is a very basic method finder routine that will search the Object o's Class's 
     * methods for a Method with parameters that can accept the Object arg as a parameter.
     * This allows arg to be a subclass of the parameter the Method is expecting. To improve
     * this algorithm you would need to take into account Interfaces. To expand it you would
     * need to consider multiple parameters and a "best fit" for them all. None of these
     * refinements are required at present so I've been lazy.
     * 
     * @param o the object to invoke the method on
     * @param method the method to find
     * @param arg the parameter the method takes
     * @return the result of the method call
     */
    public static Object invokeMethod(Object o, String method, Object arg) {
        Class<?> type = getArgsClass(new Object[] {arg})[0];

        Class<?> c = null;
        if (o instanceof Class) {
        	c = (Class<?>)o;
        } else {
        	c = o.getClass();
        }
        Method m = findMethod(c, method, new Class[] {type});
        while (m == null) {
    		type = type.getSuperclass();
        	if (type == null) {
        		break;
        	}
        	m = findMethod(c, method, new Class[] {type});
        }
        if (m == null) {
        	type = getArgsClass(new Object[] {arg}, true)[0];
        	m = findMethod(c, method, new Class[] {type});
        }
        try {
			return m.invoke(o, new Object[] {arg});
		} catch (IllegalArgumentException e) {
            printError(e, o, method, arg);
		} catch (IllegalAccessException e) {
            printError(e, o, method, arg);
		} catch (InvocationTargetException e) {
            printError(e, o, method, arg);
		}
    	return null;
    }

    /**
     * Attempts to fins a matching Method in the class. The Class.getMethod()
     * method only returns an exactly matching Method so this method tries
     * to find any method which the arguments can be passed to. NB This 
     * method  only finds the first matching Method and not the most exact 
     * match. If a Class has 3 Methods:
     * method(Component), method(JComponent), method(JTextField) 
     * and you want to call the Method with a JPasswordField none of the 3
     * methods match exactly so this method will find the first matching
     * method which may be any one of the three (as JPasswordField is a 
     * subclass of them all) rather than the closest matching method which
     * would happen for a regular non-reflected method call.    
     * 
     * @param c the class to find the Method in
     * @param method the name of the Method
     * @param types the Classes of the arguments
     * @return a matching Method.
     */
    private static Method findMethod(Class<?> c, String method, Class<?>[] types) {
        Method m;
        
        try {
            m = c.getMethod(method, types);
            return m;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Method[] ms = c.getMethods();
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getName().equals(method)) {
                    Class<?>[] ts = ms[i].getParameterTypes();
                    boolean match = true;
                    if(ts.length == types.length) {
                        for (int j = 0; j < ts.length; j++) {
                            if(null != types[j] && !ts[j].isAssignableFrom(types[j])) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            for(int j = 0; j < types.length; j++) {
                                types[j] = ts[j];
                            }
                            return ms[i];
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Attempts to find a matching Constructor in the class. The 
     * Class.getConstructor() method only returns an exactly matching 
     * Constructor so this method tries to find any Constructor
     * which the arguments can be passed to. NB This  method only finds 
     * the first matching Constructor and not the most exact match. 
     * If a Class has 3 Constructors:
     * Class(Component), Class(JComponent), Class(JTextField) 
     * and you want to call the Constructor with a JPasswordField none of 
     * the 3 constructors match exactly so this method will find the first 
     * matching constructor which may be any one of the three (as 
     * JPasswordField is a subclass of them all) rather than the closest 
     * matching Constructor which would happen for a regular non-reflected 
     * Constructor call.    
     * 
     * @param c the Class to find the Constructor in
     * @param types the Classes of the arguments
     * @return a matching Constructor
     */
    private static  Constructor<?> findConstructor(Class<?> c, Class<?>[] types) {
        Constructor<?> m;
        
        try {
            m = c.getConstructor(types);
            return m;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Constructor<?>[] ms = c.getConstructors();
            for (int i = 0; i < ms.length; i++) {
                Class<?>[] ts = ms[i].getParameterTypes();
                boolean match = true;
                if(ts.length == types.length) {
                    for (int j = 0; j < ts.length; j++) {
                        if(null != types[j] && !ts[j].isAssignableFrom(types[j])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        for(int j = 0; j < types.length; j++) {
                            types[j] = ts[j];
                        }
                        return ms[i];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Creates and returns a new instance of the supplied object
     * @param o the object to create a new instance of
     * @return the newly created instance
     */
    public static Object newInstance(Object o) {
        return newInstance(o.getClass());
    }
    
    public static Object newInstance(Class<?> c) {
        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Object newInstance(Class<?> c, Object arg) {
        return newInstance(c, new Object[] {arg});
    }
    
    public static Object newInstance(Class<?> c, Object[] args) {
        Class<?>[] clazzs = new Class[args.length];
        for (int i = 0; i < clazzs.length; i++) {
            clazzs[i] = args[i].getClass();
        }
        try {
            Class<?>[] types = getArgsClass(args);
            Constructor<?> constructor = findConstructor(c, types);
            return constructor.newInstance(args);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?>[] getArgsClass(Object[] args) {
    	return getArgsClass(args, false);
    }
    
    /**
     * Returns an array of Classes of the contents of the passed 
     * object array
     * @param args an array of objects
     * @return an array of Classes corresponding to the classes of 
     * the objects in the array
     */
    private static Class<?>[] getArgsClass(Object[] args, boolean primitive) {
        Class<?>[] types;
        if (null == args) {
            types = null;
        } else {
            Class<?>[] temp = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                if(null == args[i]) {
                    temp[i] = null;
                } else {
                    if (primitive) {
                    	if (args[i].getClass() == Integer.class) {
                    		temp[i] = Integer.TYPE;
                    	}
                    } else {
                    	temp[i] = args[i].getClass();
                    }
                }
            }
            types = temp;
        }
        return types;
    }

    public static Class<?> getMethodReturn(Object o, String method, Object args[]) {
        Class<?>[] types = getArgsClass(args);
        Class<?> c = o.getClass();
        try {
            Method m = c.getMethod(method, types);
            return m.getReturnType();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } 
        return null;
    }
    
    public static boolean isImplementor(Class<?> clazz, Class<?> iface) {
    	Class<?>[] ifaces = clazz.getInterfaces();
    	for (int i = 0; i < ifaces.length; i++) {
    		if (ifaces[i].equals(iface)) {
    			return true;
    		}
    	}
    	return false;
    }

    public static boolean isSubclass(Class<?> clazz, Class<?> sclass) {
        Class<?> sup;
        sup = clazz;
        while (null != sup) {
            if (sup.equals(sclass)) {
                return true;
            }
            sup = sup.getSuperclass();
        }
        return false;
    }
}
