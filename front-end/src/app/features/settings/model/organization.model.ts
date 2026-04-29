export interface Organization {
    id?: string;

    name: string;
    code: string;
    status: 'ACTIVE' | 'INACTIVE';

    // optional (future-proof)
    createdAt?: string;
    updatedAt?: string;
}
