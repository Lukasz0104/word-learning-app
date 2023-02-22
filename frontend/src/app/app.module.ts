import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TopBarComponent } from './components/top-bar/top-bar.component';

@NgModule({
    declarations: [AppComponent, TopBarComponent],
    imports: [BrowserModule, AppRoutingModule, NgbModule],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {}
