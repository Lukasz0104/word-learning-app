import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { LearningSetDetails } from 'src/app/models/learning-set-details';
import { LearningSetService } from 'src/app/services/learning-set.service';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
    protected readonly sets$ = new BehaviorSubject<LearningSetDetails[]>([]);

    constructor(private learningSetService: LearningSetService) {}

    ngOnInit(): void {
        this.learningSetService
            .fetchSets()
            .subscribe((data) => this.sets$.next(data));
    }
}
