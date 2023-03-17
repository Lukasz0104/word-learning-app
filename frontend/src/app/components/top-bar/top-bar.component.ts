import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    selector: 'app-top-bar',
    templateUrl: './top-bar.component.html'
})
export class TopBarComponent {
    protected isMenuCollapsed = true;

    constructor(public router: Router, protected authService: AuthService) {}

    onLogout() {
        this.authService.logout();
        this.router.navigate(['']);
    }
}
