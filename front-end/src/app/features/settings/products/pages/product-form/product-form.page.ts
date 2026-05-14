import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

import {InputTextModule} from 'primeng/inputtext';
import {TextareaModule} from 'primeng/textarea';
import {RadioButtonModule} from 'primeng/radiobutton';
import {InputNumberModule} from 'primeng/inputnumber';
import {ButtonModule} from 'primeng/button';

import {Product} from "@features/settings/model/product.model";
import {SelectModule} from "primeng/select";
import { CsmFormSectionComponent } from '@shared/components/form-section/csm-form-section.component';

@Component({
    selector: 'product-form',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        InputTextModule,
        TextareaModule,
        RadioButtonModule,
        InputNumberModule,
        SelectModule,
        ButtonModule,
        CsmFormSectionComponent
    ],
    templateUrl: './product-form.page.html'
})
export class ProductFormPage {

    private _product: Product = this.createEmptyProduct();

    @Input() set product(value: Product | undefined) {
        this._product = value ?? this.createEmptyProduct();
    }

    get product(): Product {
        return this._product;
    }

    @Input() submitted!: boolean;
    @Input() statuses!: any[];
    @Input() showActions = true;

    @Output() save = new EventEmitter<void>();
    @Output() cancel = new EventEmitter<void>();

    createEmptyProduct(): Product {
        return {
            name: '',
            image: '',
            description: '',
            category: '',
            price: 0,
            quantity: 0,
            inventoryStatus: 'INSTOCK',
            active: true
        };
    }
}
