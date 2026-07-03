/**
 * Schedule ingestion bounded context.
 *
 * <p>The service reads schedule input from configured sources, extracts message
 * blocks, parses ASM/SSM/SSIM content, validates it, and persists accepted data.
 */
package com.codeshare.airline.schedule.ingestion;
