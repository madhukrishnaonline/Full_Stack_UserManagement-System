import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { RegisterPageComponent } from "../components/register-page/register-page.component";
import { PageNotfoundComponent } from "../components/page-notfound/page-notfound.component";
import { LoginPageComponent } from "../components/login-page/login-page.component";
import { HomePageComponent } from "../components/home-page/home-page.component";
import { DashboardPageComponent } from "../components/dashboard-page/dashboard-page.component";
import { TodoListComponent } from "../components/todo-list/todo-list.component";
import { TodoManagerComponent } from "../components/todo-manager/todo-manager.component";
import { SecurityConfigGuard } from "./app.routing.guard";
import { UserListComponent } from "../components/user-list/user-list.component";

const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: HomePageComponent },
    { path: 'register', component: RegisterPageComponent },
    { path: 'login', component: LoginPageComponent },
    { path: 'user/dashboard/:name', component: DashboardPageComponent, canActivate: [SecurityConfigGuard] },
    { path: 'user/dashboard/todo_list/:name', component: TodoListComponent, canActivate: [SecurityConfigGuard] },
    { path: 'user/todo_manage/:name', component: TodoManagerComponent, canActivate: [SecurityConfigGuard] },
    { path: 'user/list_of_users', component: UserListComponent, canActivate: [SecurityConfigGuard] },
    //wild card
    { path: "**", component: PageNotfoundComponent }
]
@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class UserAppRoutingModule { }