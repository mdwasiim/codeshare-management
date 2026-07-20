import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of, shareReplay } from 'rxjs';

import { AppApiService } from '@core/api/config/app-api.service';

type LookupValue = string | number;

export interface MasterLookupOption {
    label: string;
    value: LookupValue;
}

interface MasterLookupConfig {
    endpoint: string;
    labelFields: string[];
    valueField?: string;
}

@Injectable({ providedIn: 'root' })
export class MasterReferenceLookupService {
    private api = inject(AppApiService);
    private cache = new Map<string, Observable<MasterLookupOption[]>>();

    private readonly lookupConfig: Record<string, MasterLookupConfig> = {
        regionId: { endpoint: '/master/regions', labelFields: ['regionCode', 'regionName'] },
        region: { endpoint: '/master/regions', labelFields: ['regionCode', 'regionName'], valueField: 'regionCode' },
        countryId: { endpoint: '/master/countries', labelFields: ['iso2Code', 'countryName'] },
        stateId: { endpoint: '/master/states', labelFields: ['code', 'name'] },
        cityId: { endpoint: '/master/cities', labelFields: ['iataCode', 'name'] },
        airportId: { endpoint: '/master/airports', labelFields: ['iataCode', 'icaoCode', 'airportName'] },
        tenantId: { endpoint: '/tenant/tenants', labelFields: ['tenantCode', 'name'] },
        airlineId: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'] },
        airlineCode: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'], valueField: 'iataCode' },
        tenantCode: { endpoint: '/tenant/tenants', labelFields: ['tenantCode', 'name'], valueField: 'tenantCode' },
        homeAirlineId: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'] },
        partnerAirlineId: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'] },
        operatorAirlineId: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'] },
        allianceId: { endpoint: '/master/alliances', labelFields: ['allianceCode', 'allianceName'] },
        timezoneId: { endpoint: '/master/timezones', labelFields: ['zoneId', 'utcOffset'] },
        timeZoneId: { endpoint: '/master/timezones', labelFields: ['zoneId', 'utcOffset'] },
        aircraftTypeId: { endpoint: '/master/aircraft-types', labelFields: ['iataCode', 'icaoCode', 'modelCode'] },
        manufacturerId: { endpoint: '/master/aircraft-manufacturers', labelFields: ['manufacturerCode', 'manufacturerName'] },
        ownerId: { endpoint: '/master/aircraft-owners', labelFields: ['ownerCode', 'ownerName'] },
        aircraftOwnerId: { endpoint: '/master/aircraft-owners', labelFields: ['ownerCode', 'ownerName'] },
        configurationId: { endpoint: '/master/aircraft-configurations', labelFields: ['configurationCode', 'configurationName'] },
        aircraftConfigurationId: { endpoint: '/master/aircraft-configurations', labelFields: ['configurationCode', 'configurationName'] },
        actionIdentifierId: { endpoint: '/master/action-identifiers', labelFields: ['actionCode', 'actionName'] },
        standardMessageIdentifierId: { endpoint: '/master/standard-message-identifiers', labelFields: ['messageIdentifier', 'messageIdentifierName'] }
    };

    getOptions(fieldName: string): Observable<MasterLookupOption[]> {
        const config = this.lookupConfig[fieldName];
        if (!config) {
            return of([]);
        }

        const cached = this.cache.get(fieldName);
        if (cached) {
            return cached;
        }

        const request = this.api.get<unknown>(config.endpoint).pipe(
            map((payload) => this.extractItems(payload).map((item) => this.toOption(item, config)).filter((option): option is MasterLookupOption => !!option)),
            catchError(() => of([])),
            shareReplay(1)
        );

        this.cache.set(fieldName, request);
        return request;
    }

    getReferenceOptions<T extends LookupValue = string>(categoryCode: string): Observable<Array<MasterLookupOption & { value: T }>> {
        const cacheKey = `reference:${categoryCode}`;
        const cached = this.cache.get(cacheKey);
        if (cached) {
            return cached as Observable<Array<MasterLookupOption & { value: T }>>;
        }

        const request = this.api.get<unknown>(`/master/common/reference-options/${encodeURIComponent(categoryCode)}/options`).pipe(
            map((payload) => this.extractItems(payload)
                .map((item) => this.toReferenceOption<T>(item))
                .filter((option): option is MasterLookupOption & { value: T } => !!option)
            ),
            catchError(() => of([])),
            shareReplay(1)
        );

        this.cache.set(cacheKey, request);
        return request;
    }

    private toOption(item: Record<string, unknown>, config: MasterLookupConfig): MasterLookupOption | null {
        const value = this.optionValue(item[config.valueField ?? 'id']);
        if (value === undefined) {
            return null;
        }

        const labelParts = config.labelFields.map((field) => this.displayValue(item[field])).filter(Boolean);
        return {
            value,
            label: labelParts.length ? labelParts.join(' - ') : String(value)
        };
    }

    private extractItems(payload: unknown): Record<string, unknown>[] {
        if (Array.isArray(payload)) {
            return payload.filter((item): item is Record<string, unknown> => !!item && typeof item === 'object');
        }

        if (payload && typeof payload === 'object' && Array.isArray((payload as { content?: unknown[] }).content)) {
            return (payload as { content: unknown[] }).content.filter((item): item is Record<string, unknown> => !!item && typeof item === 'object');
        }

        return [];
    }

    private toReferenceOption<T extends LookupValue>(item: Record<string, unknown>): (MasterLookupOption & { value: T }) | null {
        const value = this.optionValue(item['value']);
        const label = this.displayValue(item['label']);
        if (value === undefined || !label) {
            return null;
        }

        return {
            value: value as T,
            label
        };
    }

    private optionValue(value: unknown): LookupValue | undefined {
        if (typeof value === 'number' && Number.isFinite(value)) {
            return value;
        }

        if (typeof value === 'string') {
            const trimmed = value.trim();
            return trimmed ? trimmed : undefined;
        }

        return undefined;
    }

    private displayValue(value: unknown): string {
        if (typeof value === 'string') {
            return value.trim();
        }

        if (typeof value === 'number' && Number.isFinite(value)) {
            return String(value);
        }

        return '';
    }
}
