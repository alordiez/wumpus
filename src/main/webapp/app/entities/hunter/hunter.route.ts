import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Hunter } from 'app/shared/model/hunter.model';
import { HunterService } from './hunter.service';
import { HunterComponent } from './hunter.component';
import { HunterDetailComponent } from './hunter-detail.component';
import { HunterUpdateComponent } from './hunter-update.component';
import { HunterDeletePopupComponent } from './hunter-delete-dialog.component';
import { IHunter } from 'app/shared/model/hunter.model';

@Injectable({ providedIn: 'root' })
export class HunterResolve implements Resolve<IHunter> {
    constructor(private service: HunterService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((hunter: HttpResponse<Hunter>) => hunter.body));
        }
        return of(new Hunter());
    }
}

export const hunterRoute: Routes = [
    {
        path: 'hunter',
        component: HunterComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.hunter.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hunter/:id/view',
        component: HunterDetailComponent,
        resolve: {
            hunter: HunterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.hunter.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hunter/new',
        component: HunterUpdateComponent,
        resolve: {
            hunter: HunterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.hunter.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hunter/:id/edit',
        component: HunterUpdateComponent,
        resolve: {
            hunter: HunterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.hunter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hunterPopupRoute: Routes = [
    {
        path: 'hunter/:id/delete',
        component: HunterDeletePopupComponent,
        resolve: {
            hunter: HunterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.hunter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
