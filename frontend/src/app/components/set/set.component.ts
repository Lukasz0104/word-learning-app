import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { LearningSetDetails } from '../../models/learning-set-details';
import { LearningSetItem } from '../../models/learning-set-item';
import { LearningSetItemService } from '../../services/learning-set-item.service';
import { LearningSetService } from '../../services/learning-set.service';

@Component({
    selector: 'app-set',
    templateUrl: './set.component.html'
})
export class SetComponent {
    protected id: number | null = null;
    protected set$: Observable<LearningSetDetails>;
    protected items$: Observable<LearningSetItem[]>;

    constructor(
        private route: ActivatedRoute,
        private setService: LearningSetService,
        private itemService: LearningSetItemService
    ) {
        this.id = +this.route.snapshot.params['id'];
        this.set$ = this.setService.fetchOne(this.id);
        this.items$ = this.itemService.fetchItems(this.id);
    }
}
