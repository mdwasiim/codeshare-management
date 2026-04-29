import {AppApiService} from "@core/config/app-api.service";
import {AppMenuModel} from "@shared/models/app-menu.model";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {User} from "@features/iam/models/user.model";

@Injectable({ providedIn: 'root' })
export class MenuManagementService {

    constructor(private api: AppApiService) {}

    getAll() {
        return this.api.get<AppMenuModel[]>('menu.get');
    }

    getById(id: string) {
        return this.api.get<AppMenuModel>('menu.byId', {
            pathParams: { id }
        });
    }

    create(menuModel: AppMenuModel) {
        return this.api.post<AppMenuModel>('menu.create', menuModel);
    }

    update(id: string, menuModel: AppMenuModel) {
        return this.api.put<AppMenuModel>('menu.update', menuModel, {
            pathParams: { id }
        });
    }

    delete(id: string) {
        return this.api.delete('menu.delete', {
            pathParams: { id }
        });
    }
}
