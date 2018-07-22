/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WumpusJhTestModule } from '../../../test.module';
import { HunterUpdateComponent } from 'app/entities/hunter/hunter-update.component';
import { HunterService } from 'app/entities/hunter/hunter.service';
import { Hunter } from 'app/shared/model/hunter.model';

describe('Component Tests', () => {
    describe('Hunter Management Update Component', () => {
        let comp: HunterUpdateComponent;
        let fixture: ComponentFixture<HunterUpdateComponent>;
        let service: HunterService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [HunterUpdateComponent]
            })
                .overrideTemplate(HunterUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HunterUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HunterService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Hunter(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.hunter = entity;
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
                    const entity = new Hunter();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.hunter = entity;
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
