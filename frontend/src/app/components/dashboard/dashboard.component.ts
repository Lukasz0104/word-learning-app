import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { LearningSetDetails } from 'src/app/models/learning-set-details';
import { LearningSetService } from 'src/app/services/learning-set.service';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html'
})
export class DashboardComponent {
    protected readonly sets$: Observable<LearningSetDetails[]>;

    constructor(private learningSetService: LearningSetService) {
        this.sets$ = this.learningSetService.fetchSets();
    }
}
