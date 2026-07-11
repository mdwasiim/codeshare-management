import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthReturnUrlService {
    private readonly storageKey = 'auth:return-url';
    private readonly defaultUrl = '/dashboard';

    remember(url: string | null | undefined): void {
        const normalized = this.normalize(url);
        sessionStorage.setItem(this.storageKey, normalized);
    }

    consume(): string {
        const value = this.peek();
        sessionStorage.removeItem(this.storageKey);
        return value;
    }

    peek(): string {
        return this.normalize(sessionStorage.getItem(this.storageKey));
    }

    clear(): void {
        sessionStorage.removeItem(this.storageKey);
    }

    private normalize(url: string | null | undefined): string {
        if (!url || !url.startsWith('/') || url.startsWith('//')) {
            return this.defaultUrl;
        }

        return url;
    }
}
