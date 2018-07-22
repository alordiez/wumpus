/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WumpusJhTestModule } from '../../../test.module';
import { GamePitsDetailComponent } from 'app/entities/game-pits/game-pits-detail.component';
import { GamePits } from 'app/shared/model/game-pits.model';

describe('Component Tests', () => {
    describe('GamePits Management Detail Component', () => {
        let comp: GamePitsDetailComponent;
        let fixture: ComponentFixture<GamePitsDetailComponent>;
        const route = ({ data: of({ gamePits: new GamePits(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [GamePitsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(GamePitsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GamePitsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.gamePits).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
