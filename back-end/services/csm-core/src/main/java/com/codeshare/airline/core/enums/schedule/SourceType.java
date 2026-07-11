package com.codeshare.airline.core.enums.schedule;


public enum SourceType {
    LOCAL,     // Local file system polling
    SFTP,      // Remote SFTP pickup
    EMAIL,     // Email attachment
    MQ,        // Message queue
    REST,      // Manual HTTP upload
    CLOUD      // Cloud storage (S3, Azure, etc.)
}
