# DB-Crawler 🗄️

A Spring Boot REST API that automatically crawls your MySQL database schema and generates Java model classes on-the-fly.

## What It Does

**DB-Crawler** connects to your database and extracts metadata about tables, columns, foreign keys, and indexes. It then generates clean, well-annotated Java model classes with Lombok, ready to use in your project.

### Features

✨ **Schema Introspection** – Reads table structures, column types, and constraints  
🔗 **Foreign Key Detection** – Identifies relationships between tables  
📊 **Index Tracking** – Captures index definitions and uniqueness  
🚀 **Auto Code Generation** – Generates Java POJOs with Lombok annotations  
💾 **Fallback Demo Data** – Works without a database connection for testing  
🌐 **REST API** – Simple HTTP endpoints for schema and model access

## Quick Start

### Prerequisites

- Java 17+
- Maven
- MySQL (optional—app includes demo data fallback)

### Build

```bash
cd db-crawler
./mvnw clean package
```

### Run

```bash
java -jar target/db-crawler-0.0.1-SNAPSHOT.jar
```

The app starts on **http://localhost:9090**

## API Endpoints

### 1. Get Database Schema

```bash
curl http://localhost:9090/api/schema
```

**Response:**
```json
[
  {
    "tableName": "users",
    "columns": [
      {
        "columnName": "id",
        "columnType": "INT",
        "nullable": false,
        "primaryKey": true
      },
      {
        "columnName": "username",
        "columnType": "VARCHAR",
        "nullable": false,
        "primaryKey": false
      }
    ],
    "foreignKeys": [],
    "indexes": []
  }
]
```

### 2. Get Generated Java Models

```bash
curl http://localhost:9090/api/models
```

**Response:**
```json
{
  "Users": "package com.aryan.models;\n\nimport lombok.AllArgsConstructor;\nimport lombok.Data;\n...\npublic class Users { ... }"
}
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=db-crawler
server.port=9090

# Database Connection
db.url=jdbc:mysql://localhost:3306/your_database
db.username=root
db.password=your_password
db.database=your_database
```

**Default values (if connection fails):** Demo data is served with sample tables (`users`, `products`)

## Project Structure

```
src/main/java/com/aryan/dbcrawler/
├── DbCrawlerApplication.java         # Spring Boot entry point
├── controller/
│   └── SchemaController.java          # REST endpoints
├── model/
│   ├── ColumnMeta.java                # Column metadata
│   ├── TableMeta.java                 # Table metadata
│   ├── DbConfig.java                  # Database config
│   ├── ForeignKeyMeta.java            # Foreign key metadata
│   └── IndexMeta.java                 # Index metadata
└── service/
    ├── ConfigService.java             # Config loading
    ├── DatabaseCrawlerService.java    # JDBC metadata extraction
    └── ModelGeneratorService.java     # Java code generation
```

## How It Works

1. **Schema Crawling** – Connects to MySQL and uses JDBC `DatabaseMetaData` to extract table/column info
2. **Metadata Collection** – Gathers primary keys, foreign keys, and indexes
3. **Model Generation** – Converts SQL types to Java types and creates POJO classes
4. **REST Response** – Returns JSON with schema or Java source code

## Type Mapping

| SQL Type | Java Type |
|----------|-----------|
| INT | Integer |
| BIGINT | Long |
| DECIMAL / FLOAT / DOUBLE | BigDecimal |
| VARCHAR / TEXT | String |
| DATE | LocalDate |
| TIMESTAMP / DATETIME | LocalDateTime |
| BOOLEAN | Boolean |
| BLOB / BINARY | byte[] |

## Generated Model Example

For a `users` table with columns `id`, `username`, `email`, `created_at`:

```java
package com.aryan.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Integer id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
```

## Technologies Used

- **Spring Boot 4.0** – Web framework
- **Lombok** – Boilerplate reduction
- **JDBC** – Database metadata access
- **Maven** – Build tool
- **Java 17** – Language version

## License

MIT

## Author

Aryan Parida (@aryanparida21)
