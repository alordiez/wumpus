import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Game } from '../shared/model/game.model';
import { PlayGameService } from './play-game.service';
import { PlayGameComponent } from './play-game.component';
import { PlayGamePopupComponent } from './play-game-dialog.component';
import { IGame } from '../shared/model/game.model';

@Injectable({ providedIn: 'root' })
export class PlayGameResolve implements Resolve<IGame> {
    constructor(private service: PlayGameService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((game: HttpResponse<Game>) => game.body));
        }
        return of(new Game());
    }
}

export const playGameRoute: Routes = [
    {
        path: 'play-game',
        component: PlayGameComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.play-game.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
];

export const playGamePopupRoute: Routes = [
    {
        path: 'play-game/gameover',
        component: PlayGamePopupComponent,
        resolve: {
            game: PlayGameResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wumpusJhApp.game.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
