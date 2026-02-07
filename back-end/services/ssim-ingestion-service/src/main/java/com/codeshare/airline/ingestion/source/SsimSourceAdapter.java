package com.codeshare.airline.ingestion.source;

import com.codeshare.airline.ingestion.model.SsimRawFile;

import java.util.List;

public interface SsimSourceAdapter {

    SourceType sourceType();

    boolean isEnabled();

    List<SsimRawFile> poll();

    void onSuccess(SsimRawFile file);
    void onFailure(SsimRawFile file, Exception e);
}
