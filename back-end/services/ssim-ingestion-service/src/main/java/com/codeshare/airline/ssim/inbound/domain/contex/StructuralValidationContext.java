package com.codeshare.airline.ssim.inbound.domain.contex;

import com.codeshare.airline.ssim.inbound.validation.model.ValidationMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructuralValidationContext {

    // =============================
    // File Tracking
    // =============================

    private int lineNumber = 0;
    private int recordsRead = 0;

    // =============================
    // Record State
    // =============================

    private boolean t1Seen = false;
    private boolean t2Seen = false;
    private boolean t3Seen = false;
    private boolean t5Seen = false;

    private boolean insideFlightBlock = false;

    private char  previousRecordType;

    // =============================
    // Counters
    // =============================

    private int t3Count = 0;
    private int t4Count = 0;

    private Integer trailerDeclaredRecordCount;

    // =============================
    // Validation Messages
    // =============================

    private final List<ValidationMessage> messages = new ArrayList<>();

    // ==========================================================
    // Line Tracking
    // ==========================================================

    public void nextLine() {
        lineNumber++;
        recordsRead++;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getRecordsRead() {
        return recordsRead;
    }

    // ==========================================================
    // State Tracking
    // ==========================================================

    public boolean isT1Seen() {
        return t1Seen;
    }

    public void setT1Seen(boolean value) {
        this.t1Seen = value;
    }

    public boolean isT2Seen() {
        return t2Seen;
    }

    public void setT2Seen(boolean value) {
        this.t2Seen = value;
    }

    public boolean isT3Seen() {
        return t3Seen;
    }

    public boolean isT5Seen() {
        return t5Seen;
    }

    public void setT5Seen(boolean value) {
        this.t5Seen = value;
    }

    public boolean isInsideFlightBlock() {
        return insideFlightBlock;
    }

    public void enterFlightBlock() {
        this.insideFlightBlock = true;
    }

    public void exitFlightBlock() {
        this.insideFlightBlock = false;
    }

    public char getPreviousRecordType() {
        return previousRecordType;
    }

    public void setPreviousRecordType(char previousRecordType) {
        this.previousRecordType = previousRecordType;
    }

    // ==========================================================
    // Counters
    // ==========================================================

    public void incrementT3() {
        t3Count++;
        t3Seen = true;
    }

    public void incrementT4() {
        t4Count++;
    }

    public int getT3Count() {
        return t3Count;
    }

    public int getT4Count() {
        return t4Count;
    }

    // ==========================================================
    // Trailer
    // ==========================================================

    public Integer getTrailerDeclaredRecordCount() {
        return trailerDeclaredRecordCount;
    }

    public void setTrailerDeclaredRecordCount(Integer count) {
        this.trailerDeclaredRecordCount = count;
    }

    // ==========================================================
    // Validation Messages
    // ==========================================================

    public void addMessage(ValidationMessage message) {
        messages.add(message);
    }

    public List<ValidationMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public boolean hasBlockingErrors() {
        return messages.stream().anyMatch(ValidationMessage::isBlocking);
    }

    public boolean hasAnyMessages() {
        return !messages.isEmpty();
    }
}
