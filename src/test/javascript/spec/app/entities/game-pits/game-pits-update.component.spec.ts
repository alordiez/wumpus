/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WumpusJhTestModule } from '../../../test.module';
import { GamePitsUpdateComponent } from 'app/entities/game-pits/game-pits-update.component';
import { GamePitsService } from 'app/entities/game-pits/game-pits.service';
import { GamePits } from 'app/shared/model/game-pits.model';

describe('Component Tests', () => {
    describe('GamePits Management Update Component', () => {
        let comp: GamePitsUpdateComponent;
        let fixture: ComponentFixture<GamePitsUpdateComponent>;
        let service: GamePitsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [GamePitsUpdateComponent]
            })
                .overrideTemplate(GamePitsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(GamePitsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GamePitsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new GamePits(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.gamePits = entity;
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
                    const entity = new GamePits();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.gamePits = entity;
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
