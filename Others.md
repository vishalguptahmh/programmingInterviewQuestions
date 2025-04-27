## Http status code 


| code | Description|
|----|---|
|2xx Worked|
|   200     | `Sucessfull` : everything went well on server side|
|   201     | `Created` : ex. server says got your data, new profile is live|
|   204     | `No Content` :  It worked but no content to send back|
|4xx Client Errors |
|   400     | `Bad Request` :  In our request doesn't make sense  |
|   401     |  `Unauthorized` : Missing right credentials |
|   403     |  `Forbidden` : server says i know you but don't have permission |
|   404     |  `Not Found` : Information is not there . we should check for routes and endpoints |
|   429     | `Too many requests` : Server says slow down, you are sending requests too fast and hitting rate limits  |
5xx Server Errors 
|   500     | `Internal Server Error` : Server crys for help.Dig into logs for clues  |
|   502     | `BAD Gateway` : Issues between servers , like proxy failing to get response . this could be due to server overloads, network glitches or misconfigurations |
|   503     | `Service Unavailable` :  Server cannot handle request right now due to maintance or traffic overload |
3xx Directions
| 300       | `Multiple choices`  |
| 301       | `Moved Permanently` : redirectly to new permanent url |
| 302       |  `Found` : its temporary, it points to new location but original url still works |
| 303       |  `See other` |
| 304       | `Not Modified` : its efficiency play , ex browsers asks "Has this changed since i cached it " 304 says "no, you are good" . this will bandwidth and time by avoiding un-necessary re-downloads |
| 305       | `Use proxy`  |
| 307       | `Tempory Redirect`  |
| 308       |  `Permanent Redirect` |
1xx Directions
| 100       | `Continue` :The server has received the request headers and the client can conitnue furhter sending the request body where required |
| 101       | `Switching protocals` : Client has asked the server to switch the protocl and server agrreed to do so. example switching from http to websocket |



## WsFed apps 

it require Token based authentication.

#### How to setup
- Login to Azure with admin creds
- Navigate to Applications
- App Registrations
- New Registration
- Give application name and Register
- Navigate Api Permissions 
- Add permissions
- Microsoft graph
- There are two types of permission `Deligate permissions` and `Application Permissions`.
- select appropiate permissions like Directory.AccessUser.All , Directory.Read.all or Director.ReadWrite.all
- Click `Grant Admin consent`
- Navigate to clients and secrets 
- New Client Secreat
- Copy secret (this is one time)
- Go to Portal where you are creating this app
- put the Client Secreate, clientID,Directory and Verify
- Don't forget to allow certificate for authentication
- Now app is in Manged state we need to put in fedrated state
- Now click app and download Powershell script
- run command as admin `Set-ExecutionPolicy -scope Process -ExecutionPolicy Bypass`
- Run 0365script.ps1
- autenticate as admin to fedrate the domain
- Now App state changed from `Managed` to `Fedrated`


