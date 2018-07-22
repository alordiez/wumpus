/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WumpusJhTestModule } from '../../../test.module';
import { HunterComponent } from 'app/entities/hunter/hunter.component';
import { HunterService } from 'app/entities/hunter/hunter.service';
import { Hunter } from 'app/shared/model/hunter.model';

describe('Component Tests', () => {
    describe('Hunter Management Component', () => {
        let comp: HunterComponent;
        let fixture: ComponentFixture<HunterComponent>;
        let service: HunterService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [HunterComponent],
                providers: []
            })
                .overrideTemplate(HunterComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HunterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HunterService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Hunter(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.hunters[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
