package com.sean.zkid.registrar;

import com.sean.zkid.proxy.IdWorkProxy;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tengdj
 * @date 2020/8/1 16:16
 **/
public class ZkIdWorkerFactoryBean<T> implements FactoryBean<T> {

    public void setWorkerInterface(Class<T> workerInterface) {
        this.workerInterface = workerInterface;
    }

    private Class<T> workerInterface;

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    private ZkClient zkClient;

    @Override
    public T getObject() throws Exception {
        return new IdWorkProxy<>(workerInterface, zkClient).getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return workerInterface;
    }

}
