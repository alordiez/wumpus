/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WumpusJhTestModule } from '../../../test.module';
import { WumpusComponent } from 'app/entities/wumpus/wumpus.component';
import { WumpusService } from 'app/entities/wumpus/wumpus.service';
import { Wumpus } from 'app/shared/model/wumpus.model';

describe('Component Tests', () => {
    describe('Wumpus Management Component', () => {
        let comp: WumpusComponent;
        let fixture: ComponentFixture<WumpusComponent>;
        let service: WumpusService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [WumpusComponent],
                providers: []
            })
                .overrideTemplate(WumpusComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WumpusComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WumpusService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Wumpus(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.wumpuses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
