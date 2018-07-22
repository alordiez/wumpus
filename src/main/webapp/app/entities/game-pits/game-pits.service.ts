import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGamePits } from 'app/shared/model/game-pits.model';

type EntityResponseType = HttpResponse<IGamePits>;
type EntityArrayResponseType = HttpResponse<IGamePits[]>;

@Injectable({ providedIn: 'root' })
export class GamePitsService {
    private resourceUrl = SERVER_API_URL + 'api/game-pits';

    constructor(private http: HttpClient) {}

    create(gamePits: IGamePits): Observable<EntityResponseType> {
        return this.http.post<IGamePits>(this.resourceUrl, gamePits, { observe: 'response' });
    }

    update(gamePits: IGamePits): Observable<EntityResponseType> {
        return this.http.put<IGamePits>(this.resourceUrl, gamePits, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGamePits>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGamePits[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
