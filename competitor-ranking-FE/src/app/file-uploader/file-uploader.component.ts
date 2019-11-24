import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Competitor } from './competitor.model'
import { HttpClient } from '@angular/common/http';
import { AllCommunityModules } from '@ag-grid-community/all-modules';
import { CommunicationService } from '../result-search/communication.service';
import * as utilFunctions from './utilFunctions';

@Component({
  selector: 'app-file-uploader',
  templateUrl: './file-uploader.component.html',
  styleUrls: ['./file-uploader.component.css']
})
export class FileUploaderComponent implements OnInit {

  constructor(private http: HttpClient, private communicationService: CommunicationService) {
    this.sendButtonDisabled = true;
  }

  ngOnInit() {

  }

  columnDefs = [
    { headerName: 'Rank', field: 'rank' },
    { headerName: 'Name', field: 'name' },
    { headerName: 'Score', field: 'score' }
  ];

  @ViewChild('fileUploader', { static: false }) fileUploader: ElementRef;
  rowData: any;
  modules = AllCommunityModules;
  URL: string = 'http://localhost:8080/competitors';

  sendButtonDisabled: boolean;
  fileContent: string = '';
  file: any;
  isDivVisible: boolean = false;
  isGridVisible: boolean = false;
  printingMessage: string = '';

  clickButtonAction() {
    let file = this.file;
    let fileReader: FileReader = new FileReader();
    let self = this;
    var splittedLine: any;
    fileReader.onloadend = function (x) {
      self.fileContent = fileReader.result as string;
      if (utilFunctions.nullAndSizeCheck(self.fileContent, 0)) {
        let splitted = self.fileContent.split("\n");
        if (utilFunctions.nullAndSizeCheck(splitted, 0)) {
          let competitorArray: Competitor[] = new Array();
          let errorFound = false;
          for (let i = 0; i < splitted.length; i++) {
            splittedLine = splitted[i].trim().lastIndexOf(" ");
            if (splittedLine > 0) {
              let competitorLine = new Competitor(splitted[i].trim().substring(0, splittedLine).trim(), parseInt(splitted[i].trim().substring(splittedLine + 1)), 0);
              competitorArray.push(competitorLine);
            } else if (splitted[i] != null && splitted[i].trim().length == 0) {
              continue;
            }
            else {
              errorFound = true;
              self.fillMessage('Line Error on the ' + (i + 1) + '. line of In Input File', true);
              break;
            }
          }
          if (!errorFound) {
            self.makeRestCall(self, competitorArray);
          }
        } else {
          self.fillMessage('Input File Is Empty', true);
        }
      }
      else {
        self.fillMessage('Input File Is Empty!', true);
      }
    }
    fileReader.readAsText(file);
  }

  pageAdjustmentsAfterRestCall(self: any) {
    self.isGridVisible = true;
    self.fillMessage('File saved to the system successfully. You can check updated values below.', true);
    self.sendButtonDisabled = true;
    self.fileUploader.nativeElement.value = null;
    self.communicationService.callComponentMethod();
  }

  makeRestCall(self: any, competitorArray: Competitor[]) {
    self.http.post(self.URL, competitorArray).toPromise().then((data: any) => {
      self.rowData = data;
      self.pageAdjustmentsAfterRestCall(self);
    });
  }

  fillMessage(message: string, isDivVisible: boolean) {
    this.printingMessage = message;
    this.isDivVisible = isDivVisible;
  }

  changeStatusOfOptionalParts(decision: boolean) {
    this.isGridVisible = decision;
    this.isDivVisible = decision;
  }

  onChange(fileList: FileList): void {
    this.file = fileList[0];
    this.fillMessage('', false);
    this.changeStatusOfOptionalParts(false);
    this.sendButtonDisabled = true;
    if (this.file) {
      this.sendButtonDisabled = false;
    }
  }
}
