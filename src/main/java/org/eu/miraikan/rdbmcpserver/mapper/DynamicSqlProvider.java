package org.eu.miraikan.rdbmcpserver.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.List;
import java.util.Map;

public class DynamicSqlProvider {
    public String createSQL( String sqlTemplate,
                            @Param("params") Map<String, Object> params) {
        // 这里可以对传入的SQL模板进行处理
        // 参数会通过MyBatis的#{paramName}方式绑定
        return sqlTemplate;
    }
}