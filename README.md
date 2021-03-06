# Simple Site Jwt Login

## Description

This is the login part for simple site project, the module is divided in 2 parts:
 - BE microservice for login and signed JWT generation 
 - FE service, presentation layer, for interact to BE APIs
 
## Technologies
 
### Backend
The backend use Thorntail and Maven as core, for JWT generation the lib is Nimbus-Jose, REST APIs exposed via JAX-RS (RESTEasy Framework).
The backend have a EJB for each mode and load the correct ones according to `login.mode` property

### Frontend
The frontend use Angular Framework as core.
The navigation is very simple, a form that asks the credentials. After the insert and submit, the flow call BE layer and wait the authentication result and, in case of success, the Signed JWT. 

## Deployment

For deployment follow 2 step:
### Backend
Use `mvn clean package` for generate the full runnable JAR inside the target folder

### Frontend
Change the file **environment.prod.ts** adding the property *host* with the BE host endpoint.
Use `ng build --prod` for build the FE application and generate the index.html + js files (deploy in a static HTML server)

## Login Modes

### In Memory
This mode can be use for a fast demo, inside the pre-built data there are these credential:

 | username | password |
 |  :----   |   ----:  |
 | luca     | secret1  |
 | developer| secret1  |
 | tester   | secret1  |

The in-memory password are stored in the LoginInMemory EJB and already hashed with SHA256 algorithm.

### Properties
The code will load from file `cred/login.properties` the users. The key of the property is the username and value is the JSON that contains all user info, hash password included.<br>
The flow will get the user data by username in input and check the password, if all is correct will generated the JWT with user data.

### Database
This mode load from the [database service module](https://github.com/Martins96/simple-site-database-manager) the login credentials by username. The connection is a post method with a security header (API Key), a sort of password for the service authentication.<br>
In the request will be present the username and the hashed password, if the service return the User data the validation is correct, in other cases the credentials are wrong.

If the validation on DB is correct the flow will generated the JWT with user data.