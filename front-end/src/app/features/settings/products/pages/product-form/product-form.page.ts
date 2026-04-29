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
        ButtonModule
    ],
    templateUrl: './product-form.page.html'
})
export class ProductFormPage {

    @Input() product: Product = {} as Product;
    @Input() submitted!: boolean;
    @Input() statuses!: any[];

    @Output() save = new EventEmitter<void>();
    @Output() cancel = new EventEmitter<void>();
}
