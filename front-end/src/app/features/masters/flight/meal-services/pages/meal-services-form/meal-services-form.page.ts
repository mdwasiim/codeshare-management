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
import { MealService } from '@features/masters/flight/meal-services/models/meal-services.model';
import { MealServiceService } from '@features/masters/flight/meal-services/services/meal-services.service';

@Component({ selector: 'meal-services-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './meal-services-form.page.html' })
export class MealServiceFormPage extends BaseCrudForm<MealService> {
    private fb = inject(FormBuilder);
    private service = inject(MealServiceService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            mealCode: ['', [Validators.required]],
            mealName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: MealService): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: MealService) { return this.service.create(payload); }
    update(id: string, payload: MealService) { return this.service.update(id, payload); }
}
