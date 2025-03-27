package org.eu.miraikan.rdbmcpserver.service;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

import org.eu.miraikan.rdbmcpserver.db.DynamicSqlExecutor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import org.eu.miraikan.rdbmcpserver.mapper.SqlMapper;

@Service
public class SqlService {

    private final SqlSessionFactory sqlSessionFactory;
    private final SqlMapper sqlMapper;
    private final DynamicSqlExecutor dynamicSqlExecutor;


    @Value("${apiPassword:}")
    String apiPassword;

    @Autowired
    public SqlService(SqlSessionFactory sqlSessionFactory, SqlMapper mySqlMapper, DynamicSqlExecutor dynamicSqlExecutor) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlMapper = mySqlMapper;
        this.dynamicSqlExecutor = dynamicSqlExecutor;
    }

 
    @Tool(description = "Directly executes raw SQL statements")
    public Map<String, Object> executeRawSql(
            @ToolParam(description = "API password required")
            String password,
            @ToolParam(description = "Complete SQL statement to execute. Example: 'SHOW DATABASES'")
            String sql) {
        
        // Verify password
        if ((apiPassword!=null&& !apiPassword.isEmpty())
                &&!apiPassword.equals(password)) {
            throw new SecurityException("Invalid password");
        }
        
        try (SqlSession session = sqlSessionFactory.openSession();
             Statement statement = session.getConnection().createStatement();) {

            boolean hasResultSet = statement.execute(sql);



            Map<String,Object> results = new HashMap<>();

            if(hasResultSet) {
                ResultSet resultSet = statement.getResultSet();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                int rowCount = 0;
                while (resultSet.next()) {

                    Map<String, Object> row = new TreeMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = resultSet.getObject(i);
                        row.put(columnName, value);
                    }
                    rowCount++;
                    results.put(rowCount + " ", row);
                }
            }else {
                int updateCount = statement.getUpdateCount();
                return Map.of("updateCount",updateCount);
            }

            return results;



           // return Map.of("success", true, "message", "DDL executed successfully");
        } catch (Exception e) {
            return Map.of("success", false, "error", e.getMessage());
        }
    }


    @Tool(description ="Executes read-only SQL queries. No authentication require")
    public Map<String, Object> executeQuery(
            @ToolParam(description ="Target column(s). Example: 'id, name'")
            String column,
            @ToolParam(description ="Target table. Example: 'user_profiles'")
            String tableName,
            @ToolParam(description ="Optional. eg: 'WHERE id=1 GROUP BY class ORDER BY id LIMIT 10' ")
            String clause
           ) {
        
        try {
            List<Map<String, Object>> results = sqlMapper.selectOnly(column,
                    tableName, clause);
            return Map.of("success", true, "results", results);
        } catch (Exception e) {
            return Map.of("success", false, "error", e.getMessage());
        }
    }


    @Tool(description ="Executes parameterized SQL operations using PreparedStatement.")
    public Map<String, Object> executePreparedSql(
            @ToolParam(description = "API password required")
            String password,
            @ToolParam(description = " SQL with ? placeholders. Example: 'SELECT * FROM logs WHERE id = ?'")
            String sql,
            @ToolParam(description = "Ordered parameters matching placeholders. Example: [001]")
            Object[] params) {
        

        if ((apiPassword!=null&& !apiPassword.isEmpty())
                &&!apiPassword.equals(password)) {
            throw new SecurityException("Invalid password");
        }
        
        try {
            List<Map<String, Object>> result  = dynamicSqlExecutor.executeDynamicSql(sql, params != null ? Arrays.asList(params) :
                    new ArrayList<>());
            return Map.of("success", true, "result", result);
        } catch (Exception e) {
            return Map.of("success", false, "error", e.getMessage());
        }
    }
} 