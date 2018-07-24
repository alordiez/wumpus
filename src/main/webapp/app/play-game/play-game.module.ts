import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WumpusJhSharedModule } from 'app/shared';
import {
    PlayGameComponent,
    playGameRoute,
} from './';

const ENTITY_STATES = [...playGameRoute, ];

@NgModule({
    imports: [WumpusJhSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PlayGameComponent, ],
    entryComponents: [PlayGameComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WumpusJhPlayGameModule {}
