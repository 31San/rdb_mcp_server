package org.eu.miraikan.rdbmcpserver;

import org.eu.miraikan.rdbmcpserver.service.SqlService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("org.eu.miraikan.rdbmcpserver")
@SpringBootApplication
public class SqlMcpServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(SqlMcpServerApplication.class, args);
    }
    @Bean
	public ToolCallbackProvider sqlTools(SqlService sqlService) {
		return MethodToolCallbackProvider.builder().toolObjects(sqlService).build();
	}
} 