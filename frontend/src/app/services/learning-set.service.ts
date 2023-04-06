import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, timeout } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { LearningSetDetails } from '../models/learning-set-details';

@Injectable({
    providedIn: 'root'
})
export class LearningSetService {
    private readonly BASE_URL = `${environment.apiUrl}/sets`;

    constructor(private http: HttpClient) {}

    fetchSets(): Observable<LearningSetDetails[]> {
        return this.http.get<LearningSetDetails[]>(this.BASE_URL).pipe(
            timeout({
                first: 3000,
                with: () => []
            })
        );
    }

    fetchOne(id: number) {
        return this.http.get<LearningSetDetails>(`${this.BASE_URL}/${id}`);
    }
}
