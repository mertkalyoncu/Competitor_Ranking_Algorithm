import { Component, OnInit } from '@angular/core';
import { Competitor } from '../file-uploader/competitor.model';
import { HttpClient } from '@angular/common/http';
import { observable } from 'rxjs';
import { AllCommunityModules } from '@ag-grid-community/all-modules';
import { CommunicationService } from './communication.service'
import * as utilFunctions from '../file-uploader/utilFunctions'

@Component({
  selector: 'app-result-search',
  templateUrl: './result-search.component.html',
  styleUrls: ['./result-search.component.css']
})
export class ResultSearchComponent implements OnInit {


  constructor(private http: HttpClient, private communicationService: CommunicationService) { this.queryButtonDisabled = true; }

  ngOnInit() {
    this.communicationService.customObservable.subscribe((res) => {
      this.closeOptionalParts();
    }
    );
  }


  columnDefs = [
    { headerName: 'Rank', field: 'rank' },
    { headerName: 'Name', field: 'name' },
    { headerName: 'Score', field: 'score' }
  ];

  rowData: any;
  modules = AllCommunityModules;
  printingMessage: string = '';
  isDivVisible: boolean = false;
  isGridVisible: boolean = false;
  queryButtonDisabled: boolean;
  competitorName: string;
  keyup(event: string) {
    if (event.length == 0) {
      this.queryButtonDisabled = true;
    }
    else if (this.queryButtonDisabled && event.length > 0) {
      this.queryButtonDisabled = false;
    }
    this.competitorName = event;
  }

  clickButtonAction() {
    this.closeOptionalParts();
    let url: string = "http://localhost:8080/competitors/" + this.competitorName;
    this.http.get(url).toPromise().then((data: Competitor[]) => {
      if (utilFunctions.nullAndSizeCheck(data, 0)) {
        this.isGridVisible = true;
        this.rowData = data;
      } else {
        this.isDivVisible = true;
        this.printingMessage = 'No data for this user is found !!!'
      }

    });
  }

  closeOptionalParts() {
    this.isGridVisible = false;
    this.isDivVisible = false;
  }

}
