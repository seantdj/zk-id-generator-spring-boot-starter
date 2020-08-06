package com.sean.zkid.annotation;

import org.springframework.beans.factory.annotation.Lookup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tengdj
 * @date 2020/8/1 15:18
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface ZkIdWorker {
}
