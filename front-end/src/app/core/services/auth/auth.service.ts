import { Injectable } from '@angular/core';
import { CSMApiService } from './csm-api.service';
import { LoginResponse } from '@/core/models/auth/login-response.model';
import { RefreshTokenResponse } from '@/core/models/auth/refresh-token-response.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  constructor(private apiService: CSMApiService) {}

login(username: string, password: string): Observable<LoginResponse> {
  return this.apiService.post<LoginResponse>('login', { username, password });
}

refresh(): Observable<RefreshTokenResponse> {
  return this.apiService.post<RefreshTokenResponse>('refresh', {});
}

  logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }
}



