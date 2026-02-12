package com.codeshare.airline.ssim.source.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UploadResponse {
    private String fileId;
    private String loadId;
    private String status;

    public static UploadResponse accepted(String fileId) {
        return  UploadResponse.builder()
                .fileId(fileId)
                .build() ;
    }
}
