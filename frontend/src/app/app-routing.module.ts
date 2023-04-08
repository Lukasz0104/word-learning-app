import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { SetComponent } from './components/set/set.component';
import { permissionResolver } from './resolvers/permission.resolver';

const routes: Routes = [
    { path: '', component: DashboardComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    {
        path: 'sets/:id',
        component: SetComponent,
        resolve: { permissions: permissionResolver },
        children: []
    },
    { path: '**', redirectTo: '' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
