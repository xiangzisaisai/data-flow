package com.github.dataflow.node.model.instance;


import com.github.dataflow.common.model.DataFlowLifeCycle;
import com.github.dataflow.node.model.alarm.AlarmService;
import com.github.dataflow.node.model.store.DataStore;

/**
 * @author kevin
 * @date 2017-05-28 3:14 PM.
 */
public interface Instance extends DataFlowLifeCycle {
    /**
     * 启动实例
     */
    void start();

    /**
     * 关停实例
     */
    void stop();

    /**
     * 初始化
     */
    void init();

    /**
     * 设置告警service
     *
     * @param alarmService
     */
    void setAlarmService(AlarmService alarmService);

    /**
     * 设置数据处理中心
     *
     * @param dataStore
     */
    void setDataStore(DataStore dataStore);

    /**
     * 获取Instance名称
     *
     * @return
     */
    String getName();

    /**
     * 获取最后一次同步的位置信息
     * @return
     */
    String getPosition();
}
