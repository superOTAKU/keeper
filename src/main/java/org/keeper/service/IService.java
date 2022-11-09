package org.keeper.service;

/**
 * 一个带有生命周期的服务实例
 */
public interface IService {

    void start() throws Exception;

    void stop() throws Exception;

}
