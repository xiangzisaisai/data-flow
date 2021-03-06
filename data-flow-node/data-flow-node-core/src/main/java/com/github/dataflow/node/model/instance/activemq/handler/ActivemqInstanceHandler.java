package com.github.dataflow.node.model.instance.activemq.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.alarm.AlarmService;
import com.github.dataflow.node.model.instance.Instance;
import com.github.dataflow.node.model.instance.PooledInstanceConfig;
import com.github.dataflow.node.model.instance.activemq.ActivemqInstance;
import com.github.dataflow.node.model.instance.handler.AbstractPooledInstanceHandler;
import com.github.dataflow.node.model.instance.handler.InstanceHandler;
import com.github.dataflow.node.model.store.DataStore;
import com.github.dataflow.node.model.store.DefaultDataStore;
import com.github.dataflow.sender.activemq.config.ActivemqConfig;
import com.github.dataflow.sender.activemq.enums.ActivemqType;
import org.apache.activemq.ActiveMQConnection;
import org.springframework.stereotype.Component;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
@Component
public class ActivemqInstanceHandler extends AbstractPooledInstanceHandler implements InstanceHandler {
    private DataSourceType dataSourceType = DataSourceType.ACTIVEMQ;

    @Override
    protected DataStore doBuildDataStore() {
        return new DefaultDataStore();
    }

    @Override
    public boolean support(int instanceType) {
        return dataSourceType.getType() == instanceType;
    }

    @Override
    public Instance createInstance(DataInstance dataInstance) {
        JSONObject options = JSONObjectUtil.parseJSON(dataInstance.getOptions());
        validateProperties(options, ActivemqConfig.BROKE_URL);
        validateProperties(options, ActivemqConfig.MappingConfig.TYPE);
        int type = JSONObjectUtil.getInt(options, ActivemqConfig.MappingConfig.TYPE);
        if (type == ActivemqType.QUEUE.getType()) {
            validateProperties(options, ActivemqConfig.MappingConfig.QUEUE);
        } else {
            validateProperties(options, ActivemqConfig.MappingConfig.TOPIC);
        }
        // set property
        String username = JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.USERNAME, ActiveMQConnection.DEFAULT_USER);
        String password = JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.PASSWORD, ActiveMQConnection.DEFAULT_PASSWORD);
        Long timeout = JSONObjectUtil.getLong(options, PooledInstanceConfig.POLL_TIMEOUT, DEFAULT_TIMEOUT);
        Long period = JSONObjectUtil.getLong(options, PooledInstanceConfig.POLL_PERIOD, DEFAULT_PERIOD);
        options.put(ActivemqConfig.MappingConfig.USERNAME, username);
        options.put(ActivemqConfig.MappingConfig.PASSWORD, password);
        options.put(PooledInstanceConfig.POLL_TIMEOUT, timeout);
        options.put(PooledInstanceConfig.POLL_PERIOD, period);
        // create instance
        return new ActivemqInstance(options);
    }

    @Override
    protected AlarmService getAlarmService() {
        return dataFlowContext.getAlarmService();
    }
}
