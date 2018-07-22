import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WumpusJhWumpusModule } from './wumpus/wumpus.module';
import { WumpusJhHunterModule } from './hunter/hunter.module';
import { WumpusJhGamePitsModule } from './game-pits/game-pits.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        WumpusJhWumpusModule,
        WumpusJhHunterModule,
        WumpusJhGamePitsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WumpusJhEntityModule {}
