import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHunter } from 'app/shared/model/hunter.model';

type EntityResponseType = HttpResponse<IHunter>;
type EntityArrayResponseType = HttpResponse<IHunter[]>;

@Injectable({ providedIn: 'root' })
export class HunterService {
    private resourceUrl = SERVER_API_URL + 'api/hunters';

    constructor(private http: HttpClient) {}

    create(hunter: IHunter): Observable<EntityResponseType> {
        return this.http.post<IHunter>(this.resourceUrl, hunter, { observe: 'response' });
    }

    update(hunter: IHunter): Observable<EntityResponseType> {
        return this.http.put<IHunter>(this.resourceUrl, hunter, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IHunter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHunter[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
