import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHunter } from 'app/shared/model/hunter.model';

@Component({
    selector: 'jhi-hunter-detail',
    templateUrl: './hunter-detail.component.html'
})
export class HunterDetailComponent implements OnInit {
    hunter: IHunter;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ hunter }) => {
            this.hunter = hunter;
        });
    }

    previousState() {
        window.history.back();
    }
}
