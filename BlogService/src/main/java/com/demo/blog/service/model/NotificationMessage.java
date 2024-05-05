package com.demo.blog.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage {
    private Long authorId;
    private String title;
}
