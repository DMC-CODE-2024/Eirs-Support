/* eslint-disable @typescript-eslint/no-explicit-any */
import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClrForm } from '@clr/angular';
import { TranslateService } from '@ngx-translate/core';
import * as _ from 'lodash';
import { ConfigService } from 'ng-config-service';
import { NgOtpInputComponent } from 'ng-otp-input';
import { SimpleTimer } from 'ng2-simple-timer';
import { UserModel } from '../../core/models/user.model';
import { ApiUtilService } from '../../core/services/common/api.util.service';
import { MenuTransportService } from '../../core/services/common/menu.transport.service';

@Component({
  selector: 'ceirpanel-email-verification',
  templateUrl: '../html/user-mobile-email-verification.html',
  styles: [``],
})
export class UserMobileEmailVerificationComponent implements OnInit, AfterViewInit {
  @ViewChild('emailOtpInput') emailOtpInput!:NgOtpInputComponent;
  @ViewChild('mobileOtpInput') mobileOtpInput!:NgOtpInputComponent;
  user: UserModel = {} as UserModel;
  @ViewChild(ClrForm, { static: true }) private clrForm!: ClrForm;
  emailotp!: string;
  mobileotp!: string;
  subscribeTimer = 50;
  timeLeft = 50;
  interval!: any;
  timeLeftDisplay = "00";
  alert!:any;
  countryCode = '+855';
  userId!:string;
  email!:string;
  msisdn!:string;
  url!:string;
  allowedOtpLength = 6;
  timerId!: string;
  msgobj: any = {};

  constructor(
    private cdref: ChangeDetectorRef, 
    private route: ActivatedRoute, 
    private translate: TranslateService,
    private apicall: ApiUtilService,
    private router: Router,
    private transport: MenuTransportService,
    public config: ConfigService,
    public st: SimpleTimer) {
    this.allowedOtpLength = this.config.get('allowedOtpLength') || 6;
    this.userId = this.route.snapshot.paramMap.get('id') || '';
    this.email = this.route.snapshot.paramMap.get('email') || '';
    this.msisdn = this.route.snapshot.paramMap.get('msisdn') || '';
    this.route.queryParams.subscribe((queryParams) => {
        this.url = queryParams['url'] || '/user';
        console.log('complete url: ', this.url);
    });
  }
  ngAfterViewInit(): void {
    console.log('Method not implemented.');
  }

  ngOnInit(): void {
    this.countryCode = this.config.get('countryCode') || '+855';
    this.countryCode = _.startsWith(this.countryCode, '+') ? this.countryCode.substring(1, this.countryCode.length) : this.countryCode;
    this.apicall.get(`/user/${this.userId}`).subscribe({
        next: (_data) => {
            this.user = (_data) as UserModel;
            this.msgobj = {countryCode: this.config.get('countryCode') || '+855', mobileNumber: this.user?.profile?.phoneNo, email: this.user?.profile?.email};
            this.sendotp();
        }
    });
  }
  onSubmit(userForm: NgForm) {
    if (userForm.invalid) {
      this.clrForm.markAsTouched();
      return;
    }
    if(this.msisdn!== this.user.profile.phoneNo && this.email !== this.user.profile.email) {
        this.verifyEmailAndMsisdnOtp();
    } else if(this.msisdn=== this.user.profile.phoneNo && this.email !== this.user.profile.email) {
        this.verifyEmailOtp();
    }else if(this.msisdn!== this.user.profile.phoneNo && this.email === this.user.profile.email) {
        this.verifyMsisdnOtp();
    }
    
  }
  verifyEmailOtp(){
    this.apicall.get(`/user/verify-otp/${this.user.profile.email}/${this.emailotp}`).subscribe({
        next: (_email) => {
            if(_.isEqual((_email as any).message, 'verifyOtpSuccess')) {
                this.updateEmailAndMsisdn();
            } else {
                this.alert = {type: 'danger', message: `sms${_.get(_email,'message')}`};
                setTimeout(() => this.alert = null, 10000);
            }
        }
    });
  }
  verifyMsisdnOtp() {
    const msisdn = _.startsWith(this.user.profile.phoneNo, this.countryCode) ? this.user.profile.phoneNo : this.countryCode + this.user.profile.phoneNo;
    this.apicall.get(`/user/verify-otp/${msisdn}/${this.mobileotp}`).subscribe({
        next: (_sms) => {
            if(_.isEqual((_sms as any).message, 'verifyOtpSuccess')) {
                this.updateEmailAndMsisdn();
            } else {
                this.alert = {type: 'danger', message: `sms${_.get(_sms,'message')}`};
                setTimeout(() => this.alert = null, 10000);
            }
        }
    });
  }
  verifyEmailAndMsisdnOtp() {
    this.apicall.get(`/user/verify-otp/${this.user.profile.email}/${this.emailotp}`).subscribe({
        next: (_email) => {
            if(_.isEqual((_email as any).message, 'verifyOtpSuccess')) {
              const msisdn = _.startsWith(this.user.profile.phoneNo, this.countryCode) ? this.user.profile.phoneNo : this.countryCode + this.user.profile.phoneNo;
                this.apicall.get(`/user/verify-otp/${msisdn}/${this.mobileotp}`).subscribe({
                    next: (_sms) => {
                        if(_.isEqual((_sms as any).message, 'verifyOtpSuccess')) {
                            this.updateEmailAndMsisdn();
                        } else {
                            this.alert = {type: 'danger', message: `sms${_.get(_sms,'message')}`};
                            setTimeout(() => this.alert = null, 10000);
                        }
                    }
                });
            } else {
                this.alert = {type: 'danger', message: `email${_.get(_email,'message')}`};
                setTimeout(() => this.alert = null, 10000);
            }
        }
    });
  }
  updateEmailAndMsisdn() {
    this.apicall.get(`/user/updateEmailAndMsisdn/${this.user.id}/${this.email}/${this.msisdn}`).subscribe({
        next: (_data) => {
            if (_.isEqual(_.get(_data, 'body.status'), 'failed')) {
                this.transport.alert = {type: 'danger', message: _.get(_data,'body.message')};
            } else {
                this.transport.alert = {type: 'success', message: _.get(_data,'body.message')};
                setTimeout(() => this.transport.progress = false, 3000);
                setTimeout(() => this.alert = null, 10000);
                console.log('url: ', this.url);
                setTimeout(() => this.router.navigate([this.url]), 2000);
            }
        }
    });
  }
  onEmailOtpChange(event: unknown){
    this.emailotp = event as any;
  }
  onMobileOtpChange(event: unknown){
    this.mobileotp = event as any;
  }
  startTimer() {
    this.timeLeft = this.config.get('otpTimerTime') || 60;
    this.st.newTimer(`1sec`, 1, true);
    this.timerId = this.st.subscribe('1sec', () => this.callback());
  }
  callback() {
    this.timeLeft--;
    if(String(this.timeLeft).length == 1)this.timeLeftDisplay = `0${this.timeLeft}`;
    else this.timeLeftDisplay = `${this.timeLeft}`;
    if(this.timeLeft <= 0) {
      this.st.unsubscribe(this.timerId);
    }
  }
  pauseTimer() {
    clearInterval(this.interval);
  }
  sendotp(){
    if(!_.isEmpty(this.mobileOtpInput))this.mobileOtpInput.setValue('');
    if(!_.isEmpty(this.emailOtpInput))this.emailOtpInput.setValue('');
    
    this.timeLeft = this.subscribeTimer;
    this.startTimer();
    if(this.email !== this.user.profile.email) {
        this.apicall.get(`/user/send-otp/EMAIL/${this.user.profile.email}`).subscribe({
            next: (_email) => {
                if(_.isEqual((_email as any).message, 'sendOtpSuccess')) {
                  this.transport.alert = {
                    message: 'sendOtpSuccess',
                    type: 'info',
                  } as unknown;
                  
                }
            }
        });
    }
    if(this.msisdn !== this.user.profile.phoneNo) {
        const msisdn = _.startsWith(this.user.profile.phoneNo, this.countryCode) ? this.user.profile.phoneNo : this.countryCode + this.user.profile.phoneNo;
        this.apicall.get(`/user/send-otp/SMS/${msisdn}`).subscribe({
            next: (_sms) => {
              if(_.isEqual((_sms as any).message, 'sendOtpSuccess')) {
                this.transport.alert = {
                  message: 'sendOtpSuccess',
                  type: 'info',
                } as unknown;
              }
            }
        });
    }
  }
  
  public navigaterUrl() {
    const menu = JSON.parse(localStorage.getItem('menu') || '/user');
    console.log('cache menu: ', menu, _.sample(menu as any[]).link);
    return _.sample(menu as any[]).link;
  }
}
