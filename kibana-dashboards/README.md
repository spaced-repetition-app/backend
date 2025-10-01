# Kibana Dashboards

Thư mục này chứa các cấu hình dashboard cho Kibana để hiển thị logs và analytics cho ứng dụng Spaced Repetition.

## Cài đặt Dashboard

1. Truy cập Kibana tại `http://localhost:5601`
2. Vào **Stack Management** > **Saved Objects**
3. Click **Import** và chọn file `spaced-repetition-logs-dashboard.json`

## Các Index Patterns

### 1. spaced_repetition_logs
- **Index**: `spaced_repetition_logs*`
- **Time Field**: `timestamp`
- **Mục đích**: Lưu trữ tất cả application logs (INFO, ERROR, WARN, DEBUG)

### 2. user_activity_logs  
- **Index**: `user_activity_logs*`
- **Time Field**: `timestamp`
- **Mục đích**: Lưu trữ user activities và audit logs

## Sử dụng Dashboard

### Application Logs Dashboard
- Xem logs theo level (ERROR, WARN, INFO, DEBUG)
- Filter theo service, user ID, action
- Timeline view của logs
- Error rate monitoring

### User Activity Dashboard
- Tracking user actions
- Most active users
- Popular features/actions
- Session analysis

## Queries hữu ích

### Top Errors
```
level: "ERROR" AND timestamp: [now-1d TO now]
```

### User Activity
```
userId: 123 AND timestamp: [now-1h TO now]
```

### Service Specific Logs
```
service: "auth-service" AND level: "ERROR"
```

## Alerts

Có thể setup alerts cho:
- High error rates
- Unusual user activity patterns
- Service failures
- Performance issues