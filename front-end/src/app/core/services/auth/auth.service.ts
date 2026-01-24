import { Injectable } from '@angular/core';
import { CSMApiService } from './csm-api.service';
import { LoginResponse } from '@/core/models/auth/login-response.model';
import { RefreshTokenResponse } from '@/core/models/auth/refresh-token-response.model';

@Injectable({ providedIn: 'root' })
export class AuthService {

  constructor(private api: CSMApiService) {}

login(username: string, password: string) {
  return this.api.post<LoginResponse>(
    'login',
    { username, password }
  );
}

  refresh() {
    return this.api.post<RefreshTokenResponse>(
      'refresh',
      {}
    );
  }

  logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }
}



