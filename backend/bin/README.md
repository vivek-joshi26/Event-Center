

For Google oauth, when clicking on signup/login with google it should go to backend api-<br>
http://localhost:8080/oauth2/authorization/google

In UI the signup URL should be-<br>
/signupGoogle/vivek.jsh2@gmail.com  <br>
and for google user password field should not be there


to create the user through google the backend API is- <br>
/registerGoogle

and to create through local username and password the API is- <br>
/register

and the UI Dashboard URL should be-<br>
/user/dashboard/vivek.jsh2@gmail.com <br>
and on load it should call backend API- <br>
/user/vivek.jsh2@gmail.com