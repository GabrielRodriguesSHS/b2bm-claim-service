# Logging

### Log Levels

- `DEBUG`: Detailed information for debugging (development only)
- `INFO`: Standard operational messages
- `WARN`: Warning situations that might need attention
- `ERROR`: Error situations that need immediate attention

### Configuration Files

The logging system is configured primarily through one main file:

**logback-spring.xml** - Contains the comprehensive configuration:
- Log file locations and rotation policies
- Output patterns and formats
- Appender configurations
- Logger level definitions
- Directory creation directives

## Using the Logging System

### Basic Logging

Use the standard SLF4J logging with Lombok's `@Slf4j` annotation:

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyService {
    public void doSomething() {
        log.info("Standard logging message");
        log.debug("Debug information");
        log.warn("Warning message");
        log.error("Error message", exception);
    }
}
```

### Best Practices for Logging

1. **Choose the Right Log Level**
    - `ERROR`: Use for errors that need immediate attention
    - `WARN`: Use for potentially harmful situations that don't stop the application
    - `INFO`: Use for major operational events (startup, shutdown, configuration changes)
    - `DEBUG`: Use for detailed troubleshooting information (only in development)
    - `TRACE`: Use for very detailed information (rarely needed)

2. **Include Contextual Information**
    - Always include relevant identifiers (order ID, user ID, etc.)
    - For error logs, include exception details with stack traces
    - Use parameterized logging to avoid string concatenation: `log.info("Processing order: {}", orderId)`

3. **Performance Considerations**
    - Check log level before expensive logging operations: `if (log.isDebugEnabled()) { ... }`
    - Avoid logging sensitive information like passwords or personal data
    - Keep log messages concise but informative

### Request Tracking

For tracking requests across components, use the request ID tracking in controllers:

The service includes utility methods for tracking requests across components:

```java
import com.shs.b2bm_service_order.configs.LoggingConfig;
import java.util.UUID;

@RestController
@Slf4j
public class MyController {
    
    @GetMapping("/api/resource/{id}")
    public ResponseEntity<?> getResource(@PathVariable String id) {
        String requestId = UUID.randomUUID().toString();
        LoggingConfig.setRequestId(requestId);
        
        try {
            log.info("Processing request for resource: {}", id);
            // Process request
            return ResponseEntity.ok().build();
        } finally {
            LoggingConfig.clearRequestId();
        }
    }
}
```

## Log Output Format

Logs include:
- Timestamp
- Thread name
- Request ID (if available)
- Log level
- Logger name
- Message

Example:
```
2023-07-01 12:34:56.789 [http-nio-8080-exec-1] [abcd1234] INFO  c.s.b.controllers.MyController - Processing request for resource: 123
```

## Log Rotation and Retention

The logging system automatically manages log files to prevent disk space issues:

- Log files are rotated when they reach 10MB in size
- Log files are also rotated daily
- Up to 30 days of logs are retained
- Total log storage is capped at 1GB
- Older logs are automatically deleted