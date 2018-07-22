import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWumpus } from 'app/shared/model/wumpus.model';

type EntityResponseType = HttpResponse<IWumpus>;
type EntityArrayResponseType = HttpResponse<IWumpus[]>;

@Injectable({ providedIn: 'root' })
export class WumpusService {
    private resourceUrl = SERVER_API_URL + 'api/wumpuses';

    constructor(private http: HttpClient) {}

    create(wumpus: IWumpus): Observable<EntityResponseType> {
        return this.http.post<IWumpus>(this.resourceUrl, wumpus, { observe: 'response' });
    }

    update(wumpus: IWumpus): Observable<EntityResponseType> {
        return this.http.put<IWumpus>(this.resourceUrl, wumpus, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IWumpus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IWumpus[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
