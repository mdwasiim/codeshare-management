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
                <div class="csm-spinner-overlay__ring"></div>
            </div>
        </div>
    `,
    styles: [
        `
            .csm-spinner-overlay {
                position: fixed;
                inset: 0;
                z-index: 1200;
                pointer-events: none;
            }

            .csm-spinner-overlay__backdrop {
                position: absolute;
                inset: 0;
                background: rgba(255, 255, 255, 0.55);
                backdrop-filter: blur(1px);
            }

            .csm-spinner-overlay__content {
                position: absolute;
                inset: 0;
                display: grid;
                place-items: center;
            }

            .csm-spinner-overlay__ring {
                width: 42px;
                height: 42px;
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
