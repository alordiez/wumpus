/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WumpusJhTestModule } from '../../../test.module';
import { HunterDetailComponent } from 'app/entities/hunter/hunter-detail.component';
import { Hunter } from 'app/shared/model/hunter.model';

describe('Component Tests', () => {
    describe('Hunter Management Detail Component', () => {
        let comp: HunterDetailComponent;
        let fixture: ComponentFixture<HunterDetailComponent>;
        const route = ({ data: of({ hunter: new Hunter(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [HunterDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HunterDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HunterDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.hunter).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
