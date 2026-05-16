import { AsyncPipe, NgIf } from '@angular/common';
import { Component, inject } from '@angular/core';
import { CsmSpinnerService } from '@services/spinner/csm-spinner.service';

@Component({
    selector: 'csm-spinner-overlay',
    standalone: true,
    imports: [NgIf, AsyncPipe],
    template: `
        <div class="csm-spinner-overlay" *ngIf="spinner.loading$ | async" aria-live="polite" aria-busy="true">
            <div class="csm-spinner-overlay__backdrop"></div>
            <div class="csm-spinner-overlay__content">
                <div class="csm-spinner-overlay__panel">
                    <div class="csm-spinner-overlay__ring"></div>
                    <span>Loading</span>
                </div>
            </div>
        </div>
    `,
    styles: [
        `
            .csm-spinner-overlay {
                position: fixed;
                inset: 0;
                z-index: 1200;
                pointer-events: auto;
            }

            .csm-spinner-overlay__backdrop {
                position: absolute;
                inset: 0;
                background: rgba(248, 250, 252, 0.58);
                backdrop-filter: blur(1.5px);
            }

            .csm-spinner-overlay__content {
                position: absolute;
                inset: 0;
                display: grid;
                place-items: center;
            }

            .csm-spinner-overlay__panel {
                display: flex;
                align-items: center;
                gap: 0.75rem;
                padding: 0.85rem 1rem;
                border: 1px solid rgba(203, 213, 225, 0.9);
                border-radius: 8px;
                background: rgba(255, 255, 255, 0.94);
                box-shadow: 0 14px 38px rgba(15, 23, 42, 0.14);
                color: #0f172a;
                font-size: 0.9rem;
                font-weight: 700;
            }

            .csm-spinner-overlay__ring {
                width: 28px;
                height: 28px;
                border: 3px solid rgba(30, 41, 59, 0.2);
                border-top-color: #2563eb;
                border-radius: 9999px;
                animation: csm-spin 0.7s linear infinite;
            }

            @keyframes csm-spin {
                to {
                    transform: rotate(360deg);
                }
            }
        `
    ]
})
export class CsmSpinnerOverlayComponent {
    readonly spinner = inject(CsmSpinnerService);
}
