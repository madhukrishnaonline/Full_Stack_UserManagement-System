import { UserMgmtServiceService } from './../../services/user-mgmt-service.service';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent implements OnInit {

  formRegister!: FormGroup;

  constructor(private form: FormBuilder, private userService: UserMgmtServiceService) { }
  ngOnInit(): void {
    this.formRegister = this.form.group({
      username: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(25)]],
      fullName: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobile: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]],
      address: ['', Validators.required]
    },
      { Validators: this.passwordValidator.bind(this) }
    );
  }

  get fullName() {
    return this.formRegister.get('fullName');
  }

  get userName() {
    return this.formRegister.get('username');
  }

  get password() {
    return this.formRegister.get('password');
  }

  get confirmPassword() {
    return this.formRegister.get('confirmPassword');
  }

  get email() {
    return this.formRegister.get('email');
  }

  get mobile() {
    return this.formRegister.get('mobile');
  }

  get address() {
    return this.formRegister.get('address');
  }

  passwordValidator(formGroup: AbstractControl): ValidationErrors | null {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  successMsg: string = "";
  getSuccessMsg(): string {
    return this.successMsg;
  }

  isRegistering: boolean = false;
  registerUser() {
    this.isRegistering = true;
    this.userService.registerUser(this.formRegister.value).subscribe({
      next: (response) => {
        this.successMsg = response;
        this.isRegistering = false;
      },
      error: (errorResponse) => {
        this.isRegistering = false;
        console.error(errorResponse);
      },
    })
  }
}