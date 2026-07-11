import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of, shareReplay } from 'rxjs';

import { AppApiService } from '@core/api/config/app-api.service';

export interface MasterLookupOption {
    label: string;
    value: string;
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
        stateId: { endpoint: '/master/states', labelFields: ['stateCode', 'stateName'] },
        cityId: { endpoint: '/master/cities', labelFields: ['cityCode', 'cityName'] },
        airportId: { endpoint: '/master/airports', labelFields: ['iataCode', 'icaoCode', 'airportName'] },
        airlineId: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'] },
        airlineCode: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'], valueField: 'iataCode' },
        tenantCode: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'], valueField: 'iataCode' },
        homeAirlineId: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'] },
        partnerAirlineId: { endpoint: '/master/airline-carriers', labelFields: ['iataCode', 'icaoCode', 'displayName'] },
        allianceId: { endpoint: '/master/alliances', labelFields: ['allianceCode', 'allianceName'] },
        timezoneId: { endpoint: '/master/timezones', labelFields: ['zoneId', 'utcOffset'] },
        aircraftTypeId: { endpoint: '/master/aircraft-types', labelFields: ['aircraftTypeCode', 'aircraftTypeName'] },
        ownerId: { endpoint: '/master/aircraft-owners', labelFields: ['ownerCode', 'ownerName'] },
        configurationId: { endpoint: '/master/aircraft-configurations', labelFields: ['configurationCode', 'configurationName'] }
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

        const request = this.api.get<Record<string, unknown>[]>(config.endpoint).pipe(
            map((items) => (Array.isArray(items) ? items : []).map((item) => this.toOption(item, config)).filter((option): option is MasterLookupOption => !!option)),
            catchError(() => of([])),
            shareReplay(1)
        );

        this.cache.set(fieldName, request);
        return request;
    }

    private toOption(item: Record<string, unknown>, config: MasterLookupConfig): MasterLookupOption | null {
        const value = this.stringValue(item[config.valueField ?? 'id']);
        if (!value) {
            return null;
        }

        const labelParts = config.labelFields.map((field) => this.stringValue(item[field])).filter(Boolean);
        return {
            value,
            label: labelParts.length ? labelParts.join(' - ') : value
        };
    }

    private stringValue(value: unknown): string {
        return typeof value === 'string' ? value.trim() : '';
    }
}
