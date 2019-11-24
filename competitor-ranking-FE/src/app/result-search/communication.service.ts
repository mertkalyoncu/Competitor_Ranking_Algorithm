import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CommunicationService {

  private customSubject = new Subject<any>();
  customObservable = this.customSubject.asObservable();


  // Service message commands
  callComponentMethod() {
    this.customSubject.next();
  }
}