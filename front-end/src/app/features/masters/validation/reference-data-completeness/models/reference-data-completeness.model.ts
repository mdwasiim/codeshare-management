export interface ReferenceDataCompletenessIssue {
    category?: string | null;
    code?: string | null;
    message?: string | null;
}

export interface ReferenceDataCompletenessResponse {
    complete: boolean;
    issues: ReferenceDataCompletenessIssue[];
}
