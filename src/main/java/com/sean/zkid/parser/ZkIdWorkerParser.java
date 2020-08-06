package com.sean.zkid.parser;

import com.sean.zkid.annotation.ZkIdInvoker;
import com.sean.zkid.annotation.ZkIdWorker;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author tengdj
 * @date 2020/8/1 15:24
 **/
@Deprecated
public class ZkIdWorkerParser implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ZkIdWorker.class);
        for (String key : beans.keySet()) {
            Object obj = beans.get(key);
            Class<?> clz = AopProxyUtils.ultimateTargetClass(obj);
            Method[] methods = clz.getMethods();
            for(Method method : methods){
                if(!method.isAnnotationPresent(ZkIdInvoker.class)){
                    throw new BeanInitializationException("ZkIdWorker 注解的接口中定义的方法必须被ZkIdInvoker注解！class:" + clz.getName() + " method:" + method.getName());
                }

            }
        }
    }

}
