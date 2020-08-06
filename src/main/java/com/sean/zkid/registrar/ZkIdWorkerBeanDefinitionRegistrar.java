package com.sean.zkid.registrar;

import com.sean.zkid.annotation.ZkIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * @author tengdj
 * @date 2020/8/1 15:44
 **/
@Slf4j
@ConditionalOnBean(ZkClient.class)
public class ZkIdWorkerBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        ClassPathWokerScanner scanner = new ClassPathWokerScanner(beanDefinitionRegistry);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ZkIdWorker.class));
        Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(ClassUtils.getPackageName(annotationMetadata.getClassName()));
        if (beanDefinitions.isEmpty()) {
            log.warn("No ZkIdWorker was found in '" + ClassUtils.getPackageName(annotationMetadata.getClassName()) + "' package. Please check your configuration.");
        } else {
            Iterator i$ = beanDefinitions.iterator();

            while(i$.hasNext()) {
                BeanDefinitionHolder holder = (BeanDefinitionHolder)i$.next();
                GenericBeanDefinition definition = (GenericBeanDefinition)holder.getBeanDefinition();
                if (log.isDebugEnabled()) {
                    log.debug("Creating ZkIdWorker with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' interface");
                }

                definition.getPropertyValues().add("workerInterface", definition.getBeanClassName());

                // RuntimeBeanReference 先占坑，后填充
                definition.getPropertyValues().add("zkClient", new RuntimeBeanReference("zkClient"));

                // 这个是关键，里面的getObject方法返回了代理类的实例
                definition.setBeanClass(ZkIdWorkerFactoryBean.class);
            }
        }
    }

}
