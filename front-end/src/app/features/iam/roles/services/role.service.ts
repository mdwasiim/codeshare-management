import {CSMApiService} from "@core/config/csm-api.service";
import {inject, Injectable} from "@angular/core";

@Injectable({ providedIn: 'root' })
export class RoleService {

    private api = inject(CSMApiService);

    getAll() {
        return this.api.get<any[]>('roles.base'); // define in API config
    }
}
