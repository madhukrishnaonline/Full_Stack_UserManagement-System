import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { UserModelContract } from '../contracts/user-model.contract';

declare const baseUserUrl: string;
@Injectable({
  providedIn: 'root'
})
export class UserMgmtServiceService {

  constructor(private http: HttpClient) { }

  public getHomePage(): Observable<string> {
    return this.http.get(baseUserUrl, { responseType: 'text' });
  }

  // Register User
  public registerUser(form: UserModelContract): Observable<string> {
    return this.http.post<string>(`${baseUserUrl}register`, form);
  }

  public loginUser(postData: any): Observable<HttpResponse<any>> {
    return (this.http.post<any>(`${baseUserUrl}login`, postData, { observe: 'response' }).pipe(
      map(data => {
        const token = data.headers.get('Authorization');
        if (token) {
          //store the token
          sessionStorage.setItem("Auth_Token", token);
        }
        return data;
      })
    ));
  };

  getUserById(id: string | null): Observable<UserModelContract> {
    return this.http.get<UserModelContract>(`${baseUserUrl}id/${id}`);
  }

  getAllUsers(): Observable<UserModelContract[]> {
    return this.http.get<UserModelContract[]>(`${baseUserUrl}getAllUsers`);
  }
}