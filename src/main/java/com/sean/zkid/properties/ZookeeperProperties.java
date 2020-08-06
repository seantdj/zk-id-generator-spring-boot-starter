package com.sean.zkid.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tengdj
 * @date 2020/8/1 16:48
 **/
@Data
@ConfigurationProperties("zk-id-worker")
public class ZookeeperProperties {

    private String conn;

    private Long timeout;

}
