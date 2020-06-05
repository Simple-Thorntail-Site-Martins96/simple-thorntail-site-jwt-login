import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class RestService {

  constructor(private http: HttpClient) { }

  sendPostGetRawText(url: string, body: any, headers: HttpHeaders): Observable<HttpResponse<string>> {
    // call
    return this.http.post(url, body, {
      headers,
      observe: 'response',
      responseType: 'text'
    });
  }
}
