package com.sean.zkid.annotation;

import com.sean.zkid.autoconfig.ZkIdWorkerAuthConfigure;
import com.sean.zkid.registrar.ZkIdWorkerBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tengdj
 * @date 2020/8/1 15:48
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ZkIdWorkerAuthConfigure.class, ZkIdWorkerBeanDefinitionRegistrar.class})
public @interface EnableZkIdWorker {
}
