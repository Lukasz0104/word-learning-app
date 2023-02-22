import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TopBarComponent } from './components/top-bar/top-bar.component';

@NgModule({
    declarations: [AppComponent, TopBarComponent, DashboardComponent],
    imports: [BrowserModule, HttpClientModule, AppRoutingModule, NgbModule],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {}
