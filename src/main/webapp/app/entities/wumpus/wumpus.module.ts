import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WumpusJhSharedModule } from 'app/shared';
import {
    WumpusComponent,
    WumpusDetailComponent,
    WumpusUpdateComponent,
    WumpusDeletePopupComponent,
    WumpusDeleteDialogComponent,
    wumpusRoute,
    wumpusPopupRoute
} from './';

const ENTITY_STATES = [...wumpusRoute, ...wumpusPopupRoute];

@NgModule({
    imports: [WumpusJhSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [WumpusComponent, WumpusDetailComponent, WumpusUpdateComponent, WumpusDeleteDialogComponent, WumpusDeletePopupComponent],
    entryComponents: [WumpusComponent, WumpusUpdateComponent, WumpusDeleteDialogComponent, WumpusDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WumpusJhWumpusModule {}
