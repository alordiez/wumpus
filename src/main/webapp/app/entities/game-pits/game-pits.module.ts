import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WumpusJhSharedModule } from 'app/shared';
import {
    GamePitsComponent,
    GamePitsDetailComponent,
    GamePitsUpdateComponent,
    GamePitsDeletePopupComponent,
    GamePitsDeleteDialogComponent,
    gamePitsRoute,
    gamePitsPopupRoute
} from './';

const ENTITY_STATES = [...gamePitsRoute, ...gamePitsPopupRoute];

@NgModule({
    imports: [WumpusJhSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GamePitsComponent,
        GamePitsDetailComponent,
        GamePitsUpdateComponent,
        GamePitsDeleteDialogComponent,
        GamePitsDeletePopupComponent
    ],
    entryComponents: [GamePitsComponent, GamePitsUpdateComponent, GamePitsDeleteDialogComponent, GamePitsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WumpusJhGamePitsModule {}
