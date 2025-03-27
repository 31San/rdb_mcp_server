package org.eu.miraikan.rdbmcpserver.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SqlMapper {




    // Execute a query with parameters
   // List<Map<String, Object>> executeQuery(@Param("sql") String sql, @Param("params") Object[] params);

    // Execute update operations (INSERT, UPDATE, DELETE)
    //int executeUpdate(@Param("sql") String sql, @Param("params") Object[] params);


//    @SelectProvider(type = DynamicSqlProvider.class, method = "createSQL")
//    List<Map<String, Object>> executeDynamicSql(@Param("sqlTemplate") String sqlTemplate,
//                                                @Param("params") Map<String, Object> params);

    @Select("SELECT ${columns} FROM ${tableName}  ${clause}")
    List<Map<String, Object>> selectOnly(
            @Param("columns") String columns,
            @Param("tableName") String tableName,
            @Param("clause") String clause);
} 