export interface Product {
    id?: string;

    code?: string;
    name?: string;
    description?: string;

    image?: string;
    category?: string;

    price?: number;
    quantity?: number;

    inventoryStatus?: 'INSTOCK' | 'LOWSTOCK' | 'OUTOFSTOCK';

    rating?: number;

    active?: boolean;
    createdAt?: string;
    createdBy?: string;
    updatedAt?: string;
    updatedBy?: string;
    orders?: any[];
}
