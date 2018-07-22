/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WumpusJhTestModule } from '../../../test.module';
import { WumpusDeleteDialogComponent } from 'app/entities/wumpus/wumpus-delete-dialog.component';
import { WumpusService } from 'app/entities/wumpus/wumpus.service';

describe('Component Tests', () => {
    describe('Wumpus Management Delete Component', () => {
        let comp: WumpusDeleteDialogComponent;
        let fixture: ComponentFixture<WumpusDeleteDialogComponent>;
        let service: WumpusService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WumpusJhTestModule],
                declarations: [WumpusDeleteDialogComponent]
            })
                .overrideTemplate(WumpusDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WumpusDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WumpusService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
