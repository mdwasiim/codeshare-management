param(
    [string]$IdentityUrl = $env:IDENTITY_SERVICE_URL,
    [string]$MasterDataUrl = $env:MASTER_DATA_SERVICE_URL,
    [string]$IngestionUrl = $env:SCHEDULE_INGESTION_SERVICE_URL,
    [string]$ProcessingUrl = $env:SCHEDULE_PROCESSING_SERVICE_URL,
    [string]$CompareUrl = $env:SCHEDULE_COMPARE_SERVICE_URL,
    [string]$ScheduleUrl = $env:SCHEDULE_SERVICE_URL,
    [string]$MessageUrl = $env:SCHEDULE_MESSAGE_SERVICE_URL,
    [string]$DistributionUrl = $env:DISTRIBUTION_ENGINE_SERVICE_URL,
    [string]$ClientId = $env:INTERNAL_S2S_CLIENT_ID,
    [string]$ClientSecret = $env:INTERNAL_S2S_CLIENT_SECRET,
    [string]$SampleFile = $env:SCHEDULE_WORKFLOW_SAMPLE_FILE,
    [string]$SampleType = $env:SCHEDULE_WORKFLOW_SAMPLE_TYPE,
    [string]$SampleAirlineCode = $env:SCHEDULE_WORKFLOW_SAMPLE_AIRLINE_CODE,
    [switch]$SkipSecuredChecks
)

$ErrorActionPreference = "Stop"

if ([string]::IsNullOrWhiteSpace($IdentityUrl)) { $IdentityUrl = "http://localhost:8081" }
if ([string]::IsNullOrWhiteSpace($MasterDataUrl)) { $MasterDataUrl = "http://localhost:8084" }
if ([string]::IsNullOrWhiteSpace($IngestionUrl)) { $IngestionUrl = "http://localhost:8082" }
if ([string]::IsNullOrWhiteSpace($ProcessingUrl)) { $ProcessingUrl = "http://localhost:8085" }
if ([string]::IsNullOrWhiteSpace($CompareUrl)) { $CompareUrl = "http://localhost:8089" }
if ([string]::IsNullOrWhiteSpace($ScheduleUrl)) { $ScheduleUrl = "http://localhost:8087" }
if ([string]::IsNullOrWhiteSpace($MessageUrl)) { $MessageUrl = "http://localhost:8090" }
if ([string]::IsNullOrWhiteSpace($DistributionUrl)) { $DistributionUrl = "http://localhost:8088" }
if ([string]::IsNullOrWhiteSpace($ClientId)) { $ClientId = "internal-s2s-client" }
if ([string]::IsNullOrWhiteSpace($ClientSecret)) { $ClientSecret = "admin123" }
if ([string]::IsNullOrWhiteSpace($SampleType)) { $SampleType = "SSM" }
if ([string]::IsNullOrWhiteSpace($SampleAirlineCode)) { $SampleAirlineCode = "QR" }

function Invoke-Json {
    param(
        [string]$Method,
        [string]$Uri,
        [object]$Body = $null,
        [hashtable]$Headers = @{}
    )

    $parameters = @{
        Method = $Method
        Uri = $Uri
        Headers = $Headers
        TimeoutSec = 15
    }
    if ($null -ne $Body) {
        $parameters.ContentType = "application/json"
        $parameters.Body = ($Body | ConvertTo-Json -Depth 20)
    }
    Invoke-RestMethod @parameters
}

function Get-ResponseValue {
    param(
        [object]$Response,
        [string]$Name
    )

    if ($null -eq $Response) {
        return $null
    }
    if ($Response.PSObject.Properties.Name -contains $Name) {
        return $Response.$Name
    }
    if ($Response.PSObject.Properties.Name -contains "data" -and $null -ne $Response.data) {
        if ($Response.data.PSObject.Properties.Name -contains $Name) {
            return $Response.data.$Name
        }
    }
    return $null
}

function Assert-Health {
    param(
        [string]$Name,
        [string]$BaseUrl
    )

    $health = Invoke-Json -Method "GET" -Uri "$BaseUrl/actuator/health"
    $status = Get-ResponseValue -Response $health -Name "status"
    if ($status -ne "UP") {
        throw "$Name health is not UP. Actual response: $($health | ConvertTo-Json -Compress -Depth 10)"
    }
    Write-Host "[OK] $Name health UP"
}

function Get-ServiceToken {
    $pair = "$ClientId`:$ClientSecret"
    $encoded = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($pair))
    $response = Invoke-Json -Method "POST" -Uri "$IdentityUrl/auth/service-token" -Headers @{
        Authorization = "Basic $encoded"
    }

    $token = Get-ResponseValue -Response $response -Name "accessToken"
    if ([string]::IsNullOrWhiteSpace($token)) {
        throw "Identity service did not return an access token. Actual response: $($response | ConvertTo-Json -Compress -Depth 10)"
    }
    Write-Host "[OK] service token issued"
    return $token
}

function Assert-CodeListValidation {
    param([string]$Token)

    $body = @{
        operatingAirlineCode = $null
        messageIdentifiers = @("SSM", "ASM")
        actionCodes = @("NEW", "CNL", "TIM", "EQT", "RIN", "REV", "COD", "FLT")
        airlineCodes = @()
        airportCodes = @()
        aircraftTypeCodes = @()
        aircraftConfigurationCodes = @()
        serviceTypeCodes = @()
        mealServiceCodes = @()
        secureFlightIndicatorCodes = @()
        reservationBookingDesignators = @()
        reservationBookingModifiers = @()
        operationalSuffixCodes = @()
        flightSuffixCodes = @()
        terminalCodes = @()
        deiNumbers = @("001", "002", "009", "101", "125", "127", "170", "210")
        trafficRestrictionCodes = @()
        trafficRestrictionQualifiers = @()
    }
    $response = Invoke-Json -Method "POST" -Uri "$MasterDataUrl/internal/schedule-code-lists/validate" -Headers @{
        Authorization = "Bearer $Token"
    } -Body $body

    $valid = Get-ResponseValue -Response $response -Name "valid"
    if ($valid -ne $true) {
        throw "Code-list validation failed. Actual response: $($response | ConvertTo-Json -Compress -Depth 20)"
    }
    Write-Host "[OK] outbound schedule code-list validation"
}

function Assert-TimeValidation {
    param([string]$Token)

    $body = @{
        legs = @()
    }
    $response = Invoke-Json -Method "POST" -Uri "$MasterDataUrl/internal/schedule-time/validate" -Headers @{
        Authorization = "Bearer $Token"
    } -Body $body

    $valid = Get-ResponseValue -Response $response -Name "valid"
    if ($valid -ne $true) {
        throw "Time validation empty-request contract failed. Actual response: $($response | ConvertTo-Json -Compress -Depth 20)"
    }
    Write-Host "[OK] outbound schedule time-validation endpoint"
}

function Assert-ReferenceCompleteness {
    param([string]$Token)

    $response = Invoke-Json -Method "GET" -Uri "$MasterDataUrl/internal/reference-data-completeness/outbound-schedule" -Headers @{
        Authorization = "Bearer $Token"
    }
    $complete = Get-ResponseValue -Response $response -Name "complete"
    if ($complete -ne $true) {
        throw "Reference data is incomplete. Actual response: $($response | ConvertTo-Json -Compress -Depth 20)"
    }
    Write-Host "[OK] outbound schedule reference data complete"
}

function Invoke-SampleIngestion {
    param([string]$Token)

    if ([string]::IsNullOrWhiteSpace($SampleFile)) {
        Write-Host "[SKIP] sample ingestion; set SCHEDULE_WORKFLOW_SAMPLE_FILE or pass -SampleFile"
        return
    }
    Write-Host "[SKIP] sample ingestion HTTP API removed; submit files through configured ingestion channels"
}

function Show-WorkflowTopics {
    $topics = @(
        "schedule.workflow.processing-requested",
        "schedule.workflow.compare-requested",
        "schedule.workflow.change-set-created",
        "schedule.workflow.schedule-updated",
        "schedule.workflow.distribution-requested"
    )

    Write-Host "[INFO] expected workflow topics:"
    foreach ($topic in $topics) {
        Write-Host "       $topic"
    }
}

Assert-Health -Name "identity-service" -BaseUrl $IdentityUrl
Assert-Health -Name "master-data-service" -BaseUrl $MasterDataUrl
Assert-Health -Name "schedule-ingestion-service" -BaseUrl $IngestionUrl
Assert-Health -Name "schedule-processing-service" -BaseUrl $ProcessingUrl
Assert-Health -Name "schedule-compare-service" -BaseUrl $CompareUrl
Assert-Health -Name "schedule-service" -BaseUrl $ScheduleUrl
Assert-Health -Name "schedule-message-service" -BaseUrl $MessageUrl
Assert-Health -Name "distribution-engine-service" -BaseUrl $DistributionUrl
Show-WorkflowTopics

if ($SkipSecuredChecks) {
    Write-Host "[SKIP] secured master-data checks"
    exit 0
}

$token = Get-ServiceToken
Assert-CodeListValidation -Token $token
Assert-TimeValidation -Token $token
Assert-ReferenceCompleteness -Token $token
Invoke-SampleIngestion -Token $token
Write-Host "[OK] schedule workflow smoke verification completed"
