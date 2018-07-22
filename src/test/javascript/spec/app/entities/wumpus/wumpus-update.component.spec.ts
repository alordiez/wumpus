/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WumpusJhTestModule } from '../../../test.module';
import { WumpusUpdateComponent } from 'app/entities/wumpus/wumpus-update.component';
import { WumpusService } from 'app/entities/wumpus/wumpus.service';
import { Wumpus } from 'app/shared/model/wumpus.model';

describe('Component Tests', () => {
    describe('Wumpus Management Update Component', () => {
        let comp: WumpusUpdateComponent;
        let fixture: ComponentFixture<WumpusUpdateComponent>;
        let service: WumpusService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [WumpusUpdateComponent]
            })
                .overrideTemplate(WumpusUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WumpusUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WumpusService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Wumpus(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.wumpus = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Wumpus();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.wumpus = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
