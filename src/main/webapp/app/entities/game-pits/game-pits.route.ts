import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GamePits } from 'app/shared/model/game-pits.model';
import { GamePitsService } from './game-pits.service';
import { GamePitsComponent } from './game-pits.component';
import { GamePitsDetailComponent } from './game-pits-detail.component';
import { GamePitsUpdateComponent } from './game-pits-update.component';
import { GamePitsDeletePopupComponent } from './game-pits-delete-dialog.component';
import { IGamePits } from 'app/shared/model/game-pits.model';

@Injectable({ providedIn: 'root' })
export class GamePitsResolve implements Resolve<IGamePits> {
    constructor(private service: GamePitsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((gamePits: HttpResponse<GamePits>) => gamePits.body));
        }
        return of(new GamePits());
    }
}

export const gamePitsRoute: Routes = [
    {
        path: 'game-pits',
        component: GamePitsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.gamePits.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'game-pits/:id/view',
        component: GamePitsDetailComponent,
        resolve: {
            gamePits: GamePitsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.gamePits.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'game-pits/new',
        component: GamePitsUpdateComponent,
        resolve: {
            gamePits: GamePitsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.gamePits.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'game-pits/:id/edit',
        component: GamePitsUpdateComponent,
        resolve: {
            gamePits: GamePitsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.gamePits.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const gamePitsPopupRoute: Routes = [
    {
        path: 'game-pits/:id/delete',
        component: GamePitsDeletePopupComponent,
        resolve: {
            gamePits: GamePitsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.gamePits.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
