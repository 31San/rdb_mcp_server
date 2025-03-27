package org.eu.miraikan.rdbmcpserver;


import org.eu.miraikan.rdbmcpserver.db.DynamicSqlExecutor;
import org.eu.miraikan.rdbmcpserver.mapper.SqlMapper;
import org.eu.miraikan.rdbmcpserver.service.SqlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class SqlConnectionTest {

    @Autowired
    private SqlMapper sqlMapper;

    @Autowired
    SqlService sqlService;

    @Autowired
    DynamicSqlExecutor dynamicSqlExecutor;

//    @Test
    public void testExecuteDdl(){
        var res = sqlService.executeRawSql("123456","show databases;");
        res.forEach((k,v)->System.out.println(k+v));
    }




  //  @Test
    public void testSelectOnly(){
        List<Map<String, Object>> res = sqlMapper.selectOnly(
                "id,name",
                "users",
                "and id = 2");
        res.forEach(System.out::println);
    }

    @Test
    public void testExecuteDynamicSql() throws SQLException {
        dynamicSqlExecutor
                .executeDynamicSql("select * from users where id = ?",List.of(1))
                .forEach(System.out::println);
    }
}