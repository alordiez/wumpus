import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WumpusJhSharedModule } from 'app/shared';
import {
    HunterComponent,
    HunterDetailComponent,
    HunterUpdateComponent,
    HunterDeletePopupComponent,
    HunterDeleteDialogComponent,
    hunterRoute,
    hunterPopupRoute
} from './';

const ENTITY_STATES = [...hunterRoute, ...hunterPopupRoute];

@NgModule({
    imports: [WumpusJhSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [HunterComponent, HunterDetailComponent, HunterUpdateComponent, HunterDeleteDialogComponent, HunterDeletePopupComponent],
    entryComponents: [HunterComponent, HunterUpdateComponent, HunterDeleteDialogComponent, HunterDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WumpusJhHunterModule {}
