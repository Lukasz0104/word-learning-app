import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { LearningSetItem } from '../models/learning-set-item';

@Injectable({
    providedIn: 'root'
})
export class LearningSetItemService {
    private readonly BASE_URL = `${environment.apiUrl}/sets`;
    constructor(private http: HttpClient) {}

    fetchItems(id: number) {
        return this.http.get<LearningSetItem[]>(`${this.BASE_URL}/${id}/items`);
    }
}
