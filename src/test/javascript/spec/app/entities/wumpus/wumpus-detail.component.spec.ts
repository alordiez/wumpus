/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WumpusJhTestModule } from '../../../test.module';
import { WumpusDetailComponent } from 'app/entities/wumpus/wumpus-detail.component';
import { Wumpus } from 'app/shared/model/wumpus.model';

describe('Component Tests', () => {
    describe('Wumpus Management Detail Component', () => {
        let comp: WumpusDetailComponent;
        let fixture: ComponentFixture<WumpusDetailComponent>;
        const route = ({ data: of({ wumpus: new Wumpus(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [WumpusDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WumpusDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WumpusDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.wumpus).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
