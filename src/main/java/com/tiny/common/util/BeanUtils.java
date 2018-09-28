package com.tiny.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author  by zhangzhaojun on 2017/9/25.
 */
public class BeanUtils {
    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    public static <S, D> D copyPropertys(S s, D d) {
        if(s == null || d == null){
            return d;
        }
        Field[] sfields = s.getClass().getDeclaredFields();
        Field[] dfields = d.getClass().getDeclaredFields();
        Class scls = s.getClass();
        Class dcls = d.getClass();
        try {
            for (Field sfield : sfields) {
                String sName = sfield.getName();
                Class sType = sfield.getType();
                if("serialVersionUID".equals(sName)){
                    continue;
                }
                String sfieldName = sName.substring(0, 1).toUpperCase() + sName.substring(1);
                Method sGetMethod = scls.getMethod("get" + sfieldName);
                Object value = sGetMethod.invoke(s);
                for (Field dfield : dfields) {
                    String dName = dfield.getName();
                    Class dType = dfield.getType();
                    if (dName.equals(sName)) {
                        Method dSetMethod = dcls.getMethod("set" + sfieldName,dType);
                        try{
                            dSetMethod.invoke(d, value);
                        }catch(Exception e){
                            logger.error("Property:"+sType.toString() + ",type:" + dType.toString()+",error:"+e.getMessage(),e);
                        }
                        break;
                    }
                }
            }
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return d;
    }
}
