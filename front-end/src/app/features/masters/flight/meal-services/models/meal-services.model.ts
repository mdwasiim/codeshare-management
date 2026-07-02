import { AuditableModel } from '@shared/models/auditable.model';

export interface MealService extends AuditableModel {
    id?: string;
    mealCode?: string;
    mealName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
