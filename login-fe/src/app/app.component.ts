import { Component } from '@angular/core';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	title = 'login-fe';

	public jwt: string;
	
	updateJWT(jwt: string): void {
		this.jwt = jwt;
	}
	
}


