import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGame } from '../shared/model/game.model';
import { IField } from '../shared/model/field.model';
import { IEnum } from '../shared/model/enum.model';
import { Direction } from '../shared/model/directions.model';
import { Principal } from 'app/core';
import { PlayGameService } from './play-game.service';

@Component({
    selector: 'jhi-play-game',
    templateUrl: './play-game.component.html',
    styleUrls: ['play-game.css']
})
export class PlayGameComponent implements OnInit, OnDestroy {
    games: IGame[];
    game: IGame;
    currentAccount: any;
    eventSubscriber: Subscription;
    gameStarted: boolean;
    showSolution: boolean;

    constructor(
        private playGameService: PlayGameService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.playGameService.query().subscribe(
            (res: HttpResponse<IGame[]>) => {
                this.games = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.gameStarted = false;
        this.showSolution = false;
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerRestartGame();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IGame) {
        return item.id;
    }

    getKeys(map) {
      if (map) {
        const tmpMap = new Map(Object.entries(map));
        return Array.from(tmpMap.keys());
      }
      return null;
    }

    printElements(field) {
      if (field) {
        let str = '';
        field.elements.forEach(function(item) {
          str = item ?
            str ? str + ',' + item : item
            : str;
        });
        return str;
      }
      return '';
    }

    printPerceptions(field) {
      if (field) {
        let str = '';
        field.perceptions.forEach(function(item) {
          str = item ?
            str ? str + ',' + item : item
            : str;
        });
        return str;
      }
      return '';
    }

    registerRestartGame() {
        this.eventSubscriber = this.eventManager.subscribe('restartGame', response => this.restart());
    }

    getDescription(game: IGame) {
      return game.id + '/' + game.width + 'x' + game.height;
    }

    startGame() {
      this.doStartGame(false);
    }

    doStartGame(restart: boolean) {
      this.playGameService.startGame(this.game.id, restart).subscribe(
          (res: HttpResponse<IGame>) => {
              this.gameStarted = true;
              this.game = res.body;
          },
          (res: HttpErrorResponse) => this.onError(res.message)
      );
    }

    endGame() {
      this.playGameService.endGame(this.game.id).subscribe(
          (res: HttpResponse<IGame>) => {
              this.gameStarted = false;
              this.game = null;
          },
          (res: HttpErrorResponse) => this.onError(res.message)
      );
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    moveUp() {
      this.move(Direction.NORTH);
    }

    moveDown() {
      this.move(Direction.SOUTH);
    }

    moveLeft() {
      this.move(Direction.WEST);
    }

    moveRight() {
      this.move(Direction.EAST);
    }

    shootUp() {
      this.shoot(Direction.NORTH);
    }

    shootDown() {
      this.shoot(Direction.SOUTH);
    }

    shootLeft() {
      this.shoot(Direction.WEST);
    }

    shootRight() {
      this.shoot(Direction.EAST);
    }

    shoot(direction: string) {
      this.playGameService.shoot(this.game.id, direction).subscribe(
          (res: HttpResponse<IGame>) => {
              this.game = res.body;
              this.evaluateShoot();
          },
          (res: HttpErrorResponse) => this.onError(res.message)
      );
    }

    move(direction: string) {
      this.playGameService.move(this.game.id, direction).subscribe(
          (res: HttpResponse<IGame>) => {
              this.game = res.body;
              this.evaluateMovement();
          },
          (res: HttpErrorResponse) => this.onError(res.message)
      );
    }

    evaluateShoot() {
      if (this.game.wumpusAlive) {

      }
    }

    evaluateMovement() {
      if (this.game.gameover) {

      }
    }

    restart() {
      this.doStartGame(true);
    }

    doShowSolution() {
      this.showSolution = !this.showSolution;
    }
}
