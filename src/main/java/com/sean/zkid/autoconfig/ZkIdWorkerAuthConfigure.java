package com.sean.zkid.autoconfig;

import com.sean.zkid.properties.ZookeeperProperties;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tengdj
 * @date 2020/8/1 16:49
 **/
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZkIdWorkerAuthConfigure {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @Bean
    @ConditionalOnMissingBean(ZkClient.class)
    public ZkClient zkClient(){
        ZkClient zkClient = new ZkClient(zookeeperProperties.getConn());
        return zkClient;
    }

}
