package org.eu.miraikan.rdbmcpserver.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DynamicSqlExecutor {

    @Autowired
    private final DataSource dataSource;

    public DynamicSqlExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Executes a dynamic SQL query using a prepared statement.
     *
     * @param parameters  A list of parameters to set in the prepared statement.
     * @return            A list of maps where each map represents a row with column names and values.
     * @throws SQLException if a SQL error occurs
     */
    public List<Map<String, Object>> executeDynamicSql(String sql, List<Object> parameters) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            if (parameters != null) {
                for (int i = 0; i < parameters.size(); i++) {
                    preparedStatement.setObject(i + 1, parameters.get(i));
                }
            }

            boolean hasResultSet = preparedStatement.execute();
            if(hasResultSet) {
                ResultSet resultSet = preparedStatement.getResultSet();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                    results.add(row);
                }
            }else {
                int updateCount = preparedStatement.getUpdateCount();
                results.add(Map.of("updateCount",updateCount));
            }
        }

        return results;
    }
}