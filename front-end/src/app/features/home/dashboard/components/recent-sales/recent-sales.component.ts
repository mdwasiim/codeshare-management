import { Component } from '@angular/core';
import { RippleModule } from 'primeng/ripple';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { Product } from '@features/settings/products/models/product.model';
import { ProductService } from '@features/settings/products/services/product.service';

@Component({
    standalone: true,
    selector: 'dashboard-recent-sales',
    imports: [CommonModule, TableModule, ButtonModule, RippleModule],
    templateUrl: './recent-sales.component.html',
    styleUrls: ['./recent-sales.component.scss'],
    providers: [ProductService]
})
export class RecentSalesWidget {
    products!: Product[];

    constructor(private productService: ProductService) {}

    ngOnInit() {
        this.productService.getProductsSmall().then((data) => (this.products = data));
    }
}
