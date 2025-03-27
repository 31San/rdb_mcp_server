# rdb-mcp-server

Experimental MCP server based on Spring AI for low-level SQL operations using JDBC. 

Uses stdio as the transport mechanism.

Project prototype is AI generated.



## Supported Databases
- MySQL
- PostgreSQL
- SQLite

## Requirements

- Java 17 or later

## How to Use

### Installation
Download the JAR from the GitHub releases page.

### Configuration

Available environment variables:
- `DB_URL`: Database connection URL (default: `jdbc:sqlite:./sqlite.db`)
- `DB_USER`: Database username 
- `DB_PASSWORD`: Database password
- `API_PASSWORD`: Password for non-read-only operations (optional)
- `LOG_FILE`: Log file path (optional, default: `./rdb-mcp-server.log`)


### Running Standalone
```bash
java -jar x.x.x.jar
```

### MCP Client Integration

#### Cline
```json
{
  "mcpServers": {
    "rdb_mcp_server": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/jar"
      ],
      "env": {
        "DB_URL": "jdbc:mysql://localhost:3306/your_database",
        "DB_USER": "your_username",
        "DB_PASSWORD": "your_password",
        "API_PASSWORD": "your_password_here",
        "LOG_FILE": "./rdb-mcp-server.log"
      },
      "disabled": false,
      "autoApprove": []
    }
  }
}
```

#### Cursor
```json
{
  "mcpServers": {
    "rdb_mcp_server": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/jar"
      ],
      "env": {
        "DB_URL": "jdbc:mysql://localhost:3306/your_database",
        "DB_USER": "your_username",
        "DB_PASSWORD": "your_password",
        "API_PASSWORD": "your_password_here",
        "LOG_FILE": "./rdb-mcp-server.log"
      }
    }
  }
}
```

## Available Tools

### executeQuery
Executes read-only SQL queries. No authentication required.

Vulnerable to SQL injection。


### executeRawSql
Executes raw SQL statements. Requires password for non-read-only operations.


### executePreparedSql
Executes parameterized SQL operations using PreparedStatement.


## Adding Other Relational Databases
Add the appropriate database driver to the pom.xml file and rebuild. 