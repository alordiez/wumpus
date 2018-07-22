/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WumpusJhTestModule } from '../../../test.module';
import { GamePitsComponent } from 'app/entities/game-pits/game-pits.component';
import { GamePitsService } from 'app/entities/game-pits/game-pits.service';
import { GamePits } from 'app/shared/model/game-pits.model';

describe('Component Tests', () => {
    describe('GamePits Management Component', () => {
        let comp: GamePitsComponent;
        let fixture: ComponentFixture<GamePitsComponent>;
        let service: GamePitsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [GamePitsComponent],
                providers: []
            })
                .overrideTemplate(GamePitsComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(GamePitsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GamePitsService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new GamePits(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.gamePits[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
