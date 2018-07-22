import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Wumpus } from 'app/shared/model/wumpus.model';
import { WumpusService } from './wumpus.service';
import { WumpusComponent } from './wumpus.component';
import { WumpusDetailComponent } from './wumpus-detail.component';
import { WumpusUpdateComponent } from './wumpus-update.component';
import { WumpusDeletePopupComponent } from './wumpus-delete-dialog.component';
import { IWumpus } from 'app/shared/model/wumpus.model';

@Injectable({ providedIn: 'root' })
export class WumpusResolve implements Resolve<IWumpus> {
    constructor(private service: WumpusService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((wumpus: HttpResponse<Wumpus>) => wumpus.body));
        }
        return of(new Wumpus());
    }
}

export const wumpusRoute: Routes = [
    {
        path: 'wumpus',
        component: WumpusComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.wumpus.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wumpus/:id/view',
        component: WumpusDetailComponent,
        resolve: {
            wumpus: WumpusResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.wumpus.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wumpus/new',
        component: WumpusUpdateComponent,
        resolve: {
            wumpus: WumpusResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.wumpus.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wumpus/:id/edit',
        component: WumpusUpdateComponent,
        resolve: {
            wumpus: WumpusResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.wumpus.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wumpusPopupRoute: Routes = [
    {
        path: 'wumpus/:id/delete',
        component: WumpusDeletePopupComponent,
        resolve: {
            wumpus: WumpusResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.wumpus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
