package com.sean.zkid.proxy;

import com.sean.zkid.annotation.ZkIdInvoker;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author tengdj
 * @date 2020/8/1 15:35
 **/
public class IdWorkProxy<T> implements InvocationHandler {

    private Class<T> proxyInterface;

    private ZkClient zkClient;

    public IdWorkProxy(Class<T> proxyInterface, ZkClient zkClient) {
        this.proxyInterface = proxyInterface;
        this.zkClient = zkClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ZkIdInvoker zkIdInvoker = method.getAnnotation(ZkIdInvoker.class);
        String path = zkIdInvoker.value();
        if(StringUtils.isEmpty(path)){
            path = "/" + method.getDeclaringClass().getSimpleName() + method.getName();
        }
        if(!path.startsWith("/")){
            path = "/" + path;
        }
        path = createSeqNode(path);
        int index;
        String id = "0";
        if (null != path && (index = path.lastIndexOf(zkIdInvoker.value())) >= 0) {
            index += zkIdInvoker.value().length();
            id = index <= path.length() ? path.substring(index) : "";
        }
        if(method.getReturnType().equals(Long.class)){
            return Long.valueOf(id);
        }
        if(method.getReturnType().equals(Integer.class)){
            return Integer.valueOf(id);
        }
        return id;
    }

    public T getProxy() {
        return (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(), new Class[]{proxyInterface}, this);
    }

    private String createSeqNode(String pathPefix) {
        try {
            // 创建一个 ZNode 顺序节点
            String destPath = this.zkClient.create(pathPefix, null, CreateMode.EPHEMERAL_SEQUENTIAL);
            return destPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
