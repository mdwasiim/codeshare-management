import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';
import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { Country } from '@features/masters/geography/countries/models/countries.model';
import { CountryService } from '@features/masters/geography/countries/services/countries.service';

@Component({ selector: 'countries-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './countries-form.page.html' })
export class CountryFormPage extends BaseCrudForm<Country> {
    private fb = inject(FormBuilder);
    private service = inject(CountryService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            iso2Code: ['', [Validators.required]],
            iso3Code: ['', [Validators.required]],
            countryName: ['', [Validators.required]],
            regionId: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: Country): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: Country) { return this.service.create(payload); }
    update(id: string, payload: Country) { return this.service.update(id, payload); }
}
