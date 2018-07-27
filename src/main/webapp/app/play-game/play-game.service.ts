import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGame } from '../shared/model/game.model';

type EntityResponseType = HttpResponse<IGame>;
type EntityArrayResponseType = HttpResponse<IGame[]>;

@Injectable({ providedIn: 'root' })
export class PlayGameService {
    private resourceUrl = SERVER_API_URL + 'api/games';

    constructor(private http: HttpClient) {}

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGame>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGame[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    startGame(id: number, restart: boolean): Observable<EntityResponseType> {
        const args = restart ? '?restart=true' : '';
        return this.http.get<IGame>(`${this.resourceUrl}/${id}/start-game${args}`, { observe: 'response' });
    }

    endGame(id: number): Observable<EntityResponseType> {
        return this.http.get<IGame>(`${this.resourceUrl}/${id}/end-game`, { observe: 'response' });
    }

    move(id: number, direction: string): Observable<EntityResponseType> {
        return this.http.patch<IGame>(`${this.resourceUrl}/${id}/move`, direction, { observe: 'response' });
    }

    shoot(id: number, direction: string): Observable<EntityResponseType> {
        return this.http.patch<IGame>(`${this.resourceUrl}/${id}/shoot`, direction, { observe: 'response' });
    }
}
