import { Component } from '@angular/core';
import { RippleModule } from 'primeng/ripple';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { Product } from '@core/models/product.model';
import { ProductService } from '@services/product/product.service';

@Component({
    standalone: true,
    selector: 'recent-sales-widget',
    imports: [CommonModule, TableModule, ButtonModule, RippleModule],
    templateUrl: './recent-sales-widget.component.html',
    styleUrls: ['./recent-sales-widget.component.scss'],
    providers: [ProductService]
})
export class RecentSalesWidget {
    products!: Product[];

    constructor(private productService: ProductService) {}

    ngOnInit() {
        this.productService.getProductsSmall().then((data) => (this.products = data));
    }
}
