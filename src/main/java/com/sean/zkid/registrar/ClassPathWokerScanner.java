package com.sean.zkid.registrar;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.*;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author tengdj
 * @date 2020/8/1 16:22
 **/
public class ClassPathWokerScanner extends ClassPathBeanDefinitionScanner {

    private ScopeMetadataResolver scopeMetadataResolver;

    private BeanNameGenerator beanNameGenerator;

    private BeanDefinitionRegistry registry;

    public ClassPathWokerScanner(BeanDefinitionRegistry registry) {
        super(registry);
        this.registry = registry;
        this.scopeMetadataResolver = new AnnotationScopeMetadataResolver();
        this.beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    /**
     * 5.2.8版本的spring 把该方法定义为了protected,所以只能自己拷贝一份了
     * @param basePackages
     * @return
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Assert.notEmpty(basePackages, "At least one base package must be specified");
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet();

        for(String basePackage : basePackages) {
            Set<BeanDefinition> candidates = this.findCandidateComponents(basePackage);
            Iterator iterator = candidates.iterator();

            while(iterator.hasNext()) {
                BeanDefinition candidate = (BeanDefinition)iterator.next();
                ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
                candidate.setScope(scopeMetadata.getScopeName());
                String beanName = this.beanNameGenerator.generateBeanName(candidate, this.registry);
                if (candidate instanceof AbstractBeanDefinition) {
                    this.postProcessBeanDefinition((AbstractBeanDefinition)candidate, beanName);
                }

                if (candidate instanceof AnnotatedBeanDefinition) {
                    AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)candidate);
                }

                if (this.checkCandidate(beanName, candidate)) {
                    BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
                    definitionHolder = applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
                    beanDefinitions.add(definitionHolder);
                    this.registerBeanDefinition(definitionHolder, this.registry);
                }
            }
        }

        return beanDefinitions;
    }

    static BeanDefinitionHolder applyScopedProxyMode(ScopeMetadata metadata, BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
        ScopedProxyMode scopedProxyMode = metadata.getScopedProxyMode();
        if (scopedProxyMode.equals(ScopedProxyMode.NO)) {
            return definition;
        } else {
            boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
            return ScopedProxyUtils.createScopedProxy(definition, registry, proxyTargetClass);
        }
    }
}
