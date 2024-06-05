import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const accessToken: any = localStorage.getItem('user');
    let decodedItem;
      
    if (req.headers.get('skip')) return next.handle(req);
    console.log(accessToken);
    if (accessToken) {
      decodedItem = JSON.parse(accessToken);
      req = req.clone({
        setHeaders: {
          'Authorization': `Bearer ${decodedItem}`,
        },
      });

      return next.handle(req);
    } else {
      console.log("else");
      return next.handle(req);
    }
  }
}
