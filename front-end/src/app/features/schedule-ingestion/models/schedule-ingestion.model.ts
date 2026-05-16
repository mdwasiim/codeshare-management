export type ScheduleMessageType = 'SSIM' | 'ASM' | 'SSM';
export type ScheduleAction = 'validate' | 'parse' | 'ingest';
export type ProcessingStatus = 'RECEIVED' | 'VALIDATED' | 'PARSED' | 'LOADED' | 'FAILED' | 'PARTIALLY_LOADED' | string;

export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}

export interface ScheduleFileMetaData {
    fileId: string;
    loadId?: string;
    fileName?: string;
    messageType?: ScheduleMessageType;
    sourceType?: string;
    scheduleProfile?: string;
    processingStatus?: ProcessingStatus;
    timeMode?: string;
    airlineCode?: string;
    fileSizeBytes?: number;
    checksum?: string;
    receivedAt?: string;
    processedAt?: string;
    failedTimestamp?: string;
    errorCode?: string;
    sequenceReference?: string;
    creatorReference?: string;
    totalRecordCount?: number;
}

export interface LoadedScheduleSummary {
    file: ScheduleFileMetaData;
    messageCount?: number;
    flightCount: number;
}

export interface LoadedScheduleDetail {
    file: ScheduleFileMetaData;
    schedule?: unknown;
    messages?: unknown[];
    messageCount?: number;
    flightCount?: number;
}

export interface ScheduleValidationMessage {
    code?: string;
    message?: string;
    severity?: string;
    lineNumber?: number;
    field?: string;
    [key: string]: unknown;
}

export interface ScheduleValidationResponse {
    messageType: ScheduleMessageType;
    fileName?: string;
    airlineCode?: string;
    blockCount: number;
    parsedBlockCount: number;
    valid: boolean;
    messages?: ScheduleValidationMessage[];
    parsedMessages?: unknown[];
}

export interface ScheduleIngestionResponse {
    fileId: string;
    loadId?: string;
    fileName?: string;
    airlineCode?: string;
    messageType?: ScheduleMessageType;
    status?: ProcessingStatus;
}

export interface UploadResponse {
    fileId?: string;
    loadId?: string;
    status?: string;
}

export interface SsimFlight {
    airlineCode?: string;
    flightNumber?: string;
    departureStation?: string;
    arrivalStation?: string;
    aircraftType?: string;
    serviceType?: string;
    operatingDays?: string;
    operatingPeriodStartRaw?: string;
    operatingPeriodEndRaw?: string;
    passengerStd?: string;
    passengerSta?: string;
    deis?: SsimDataElement[];
}

export interface ScheduleFlight {
    airlineDesignator?: string;
    flightNumber?: string;
    operationDate?: string;
    boardPoint?: string;
    offPoint?: string;
    aircraftType?: string;
    serviceType?: string;
    flightSequenceNumber?: number;
    operationalSuffix?: string;
    aircraftConfiguration?: string;
    bookingDesignator?: string;
    periods?: SchedulePeriod[];
    legs?: ScheduleLeg[];
    deis?: ScheduleDataElement[];
    supplementaryInfo?: string[];
}

export type AnyScheduleFlight = SsimFlight & ScheduleFlight;

export interface SchedulePeriod {
    startDate?: string;
    endDate?: string;
    daysOfOperation?: string;
    frequencyRate?: number;
}

export interface ScheduleLeg {
    legSequenceNumber?: number;
    boardPoint?: string;
    offPoint?: string;
    departureTime?: string;
    arrivalTime?: string;
    departureDayOffset?: number;
    arrivalDayOffset?: number;
    aircraftType?: string;
    aircraftConfiguration?: string;
    serviceType?: string;
    deis?: ScheduleDataElement[];
}

export interface ScheduleDataElement {
    deiCode?: number;
    value?: string;
    scope?: string;
    sequenceOrder?: number;
    legSequenceNumber?: number;
    boardPoint?: string;
    offPoint?: string;
    rawLine?: string;
    [key: string]: unknown;
}

export interface SsimDataElement {
    flightLegId?: string;
    recordType?: string;
    operationalSuffix?: string;
    airlineCode?: string;
    flightNumber?: string;
    itineraryVariationIdentifier?: string;
    legSequenceNumber?: string;
    serviceType?: string;
    spare15To27?: string;
    itineraryVariationOverflow?: string;
    boardPointIndicator?: string;
    offPointIndicator?: string;
    dataElementIdentifier?: string;
    boardPoint?: string;
    offPoint?: string;
    deiData?: string;
    recordSerialNumber?: string;
    [key: string]: unknown;
}
