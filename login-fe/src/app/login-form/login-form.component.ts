import { Component, EventEmitter, Output } from '@angular/core';
import { RestService } from '../rest-service';
import { host } from 'src/environments/environment';
import { HttpHeaders } from '@angular/common/http';

const loginUrl: string = 'http://' + host + '/login';

@Component({
	selector: 'app-login-form',
	templateUrl: './login-form.component.html',
	styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
	
	@Output()
	jwt = new EventEmitter<string>();
	
	userInput: string;
	pwdInput: string;

	constructor(private rest: RestService) { }

	public login(): void {

		this.rest.sendPostGetRawText(loginUrl, {
			username: this.userInput,
			password: this.pwdInput
		},
			new HttpHeaders({ 'content-type': 'application/json' }))
			.subscribe(response => {
				if (response.status == 200)
					this.jwt.emit(response.body);
				else
					console.log("Error in call, status ", response.status);
			});
	}

}
