

For Google oauth, when clicking on signup/login with google it should go to backend api-<br>
http://localhost:8080/oauth2/authorization/google

In UI the, signup URL for google user should be-<br>
localhost:3000/signupGoogle/vivek.jsh2@gmail.com  <br>
and for google user password field should not be there

to create the user through google the backend API is- <br>
localhost:8000/registerGoogle

and to create through local username and password the API is- <br>
localhost:8000/register

and the UI Dashboard URL should be-<br>
localhost:3000/dashboard/vivek.jsh2@gmail.com <br>
and on load it should call backend API- <br>
localhost:8000/user/vivek.jsh2@gmail.com


#End Points
# USERS
## 1- Register a local user - POST - http://localhost:8080/register
```yaml
RequestBody- 
{
  "email":"ceccmpe275@gmail.com",
  "password":"abc",
  "name":"Vivek Joshi",
  "role":"PEOPLE",
  "screenName":"Vivek Joshi1",
  "gender":"male",
  "description":"test",
  "address":{
  "street":"first",
  "city":"San Jose",
  "state":"CA",
  "zip":"95110"
  }
}
  
Response- 201 Created
{
    "id": 17,
    "name": "Vivek Joshi",
    "email": "ceccmpe275@gmail.com",
    "password": "$2a$11$Xdx7OM6X2VJxTk2IxC/WOeWTw7g61WY63uYQrSUFB5HliWfF5YvbC",
    "role": "PEOPLE",
    "enabled": false,
    "screenName": "Vivek Joshi1",
    "gender": "male",
    "description": "test",
    "address": {
      "street": "first",
      "city": "San Jose",
      "state": "CA",
      "zip": "95110"
    },
    "eventss": null,
    "authProvider": "LOCAL"
  }

```
```yaml
When Screen Name is not unique
RequestBody-
{
  "email":"ceccmpe275@gmail.com",
  "password":"abc",
  "name":"Vivek Joshi",
  "role":"PEOPLE",
  "screenName":"Vivek Joshi",
  "gender":"male",
  "description":"test",
  "address":{
  "street":"first",
  "city":"San Jose",
  "state":"CA",
  "zip":"95110"
  }
}
  
Response-  400 Bad Request
Please provide a unique screen name, try by adding some suffix to the name.
```

## 2- Register a Google user - POST - http://localhost:8080/registerGoogle
```yaml
RequestBody-
{
  "email":"ceccmpe275@gmail.com",
  "password":"abc",
  "name":"Vivek Joshi",
  "role":"PEOPLE",
  "screenName":"Vivek Joshi2",
  "gender":"male",
  "description":"test",
  "address":{
  "street":"first",
  "city":"San Jose",
  "state":"CA",
  "zip":"95110"
  }
}
  
Response- 201 Created
{
    "id": 18,
    "name": "Vivek Joshi",
    "email": "ceccmpe275@gmail.com",
    "password": "$2a$11$4kFDUv3bCviAXmlKvFlagOR5JIPqy5e8wlPpOnpMKZIzN3NMwDENu",
    "role": "PEOPLE",
    "enabled": false,
    "screenName": "Vivek Joshi2",
    "gender": "male",
    "description": "test",
    "address": {
      "street": "first",
      "city": "San Jose",
      "state": "CA",
      "zip": "95110"
    },
    "eventss": null,
    "authProvider": "GOOGLE"
  }

```
```yaml
When Screen Name is not unique
RequestBody-
{
  "email":"ceccmpe275@gmail.com",
  "password":"abc",
  "name":"Vivek Joshi",
  "role":"PEOPLE",
  "screenName":"Vivek Joshi2",
  "gender":"male",
  "description":"test",
  "address":{
  "street":"first",
  "city":"San Jose",
  "state":"CA",
  "zip":"95110"
  }
}
  
Response-  400 Bad Request
Please provide a unique screen name, try by adding some suffix to the name.
```

## 3- Login a LOCAL user - POST - http://localhost:8080/login
```yaml
RequestBody-
{
"email":"ceccmpe275@gmail.com",
"password":"abc"
}
Responses-
1- When Account is not verified     401 Unauthorized
  Please verify the account before login!
2- When password is incorrect       400 Bad Request
  Password did not match!
3- When signup is not done, and user is trying to login    404 Not Found
  Please signup before login
4- When Google user is trying to login using local login        403 Forbidden
  Please login with google
5- When account is verified, login is successful      200 OK
{
    "user": {
      "id": 17,
      "name": "Vivek Joshi",
      "email": "ceccmpe275@gmail.com",
      "password": "$2a$11$Xdx7OM6X2VJxTk2IxC/WOeWTw7g61WY63uYQrSUFB5HliWfF5YvbC",
      "role": "PEOPLE",
      "enabled": true,
      "screenName": "Vivek Joshi1",
      "gender": "male",
      "description": "test",
      "address": {
        "street": "first",
        "city": "San Jose",
        "state": "CA",
        "zip": "95110"
      },
      "eventss": [],
      "authProvider": "LOCAL"
    },
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjZWNjbXBlMjc1QGdtYWlsLmNvbSIsImV4cCI6MTY1MTczNTk5NywiaWF0IjoxNjUxNzE3OTk3fQ.rw8KRAFaG3UyafY5Wj9PvEOsLwQTdaK94AsSXBnQZaLR_jXMtXjWAaQGNSJxq0P-WesFny8pOu0nMSr9bgvMbA"
  }
```

## 4- Login a GOOGLE user - GET - http://localhost:8080/user/ceccmpe275
```yaml
Responses-
1- When Account is not verified     401 Unauthorized
Please verify the account before login!
2- When LOCAL user is trying to login using Google login        403 Forbidden
Please login with Local id and password
3- When account is verified, login is successful      200 OK
{
    "user": {
      "id": 17,
      "name": "Vivek Joshi",
      "email": "ceccmpe275@gmail.com",
      "password": "$2a$11$Xdx7OM6X2VJxTk2IxC/WOeWTw7g61WY63uYQrSUFB5HliWfF5YvbC",
      "role": "PEOPLE",
      "enabled": true,
      "screenName": "Vivek Joshi1",
      "gender": "male",
      "description": "test",
      "address": {
        "street": "first",
        "city": "San Jose",
        "state": "CA",
        "zip": "95110"
      },
      "eventss": [],
      "authProvider": "GOOGLE"
    },
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjZWNjbXBlMjc1QGdtYWlsLmNvbSIsImV4cCI6MTY1MTczNjM5MCwiaWF0IjoxNjUxNzE4MzkwfQ.1MMRsY3slJdAT8JF3jDrwxLzEeqpBMjvp6wkKDnDjfxlF6KUTJtdTIu08pjdXyUxagHfgznRkLtt6Nic9j755Q"
  }
```

#EVENTS
## 1- Create an Event - POST - http://localhost:8080/createEvent    
```yaml
NOTE - Need to do some work for startDateTime,endDateTime,deadline- will make changes later
Request-
  {
    "title" : "Testing Event Title",
    "description" : "Testing Event Description",
    "minParticipants" : 20,
    "maxParticipants"   : 40,
    "address": {
      "street": "first",
      "city": "San Jose",
      "state": "CA",
      "zip": "95110"
    },
    "admissionPolicy": "first-come-first-served",
    "startDateTime": "2022-05-10T14:00:00",
    "endDateTime": "2022-05-10T15:00:00",
    "deadline": "2022-05-09T15:00:00",
    "userId": 17,
    "sysDate":"2022-05-29 11:43"
  }
Responses-
1- When Succesfully created -   200 OK
  event created
2- When start date is after end date  -   400 Bad Request
  start time cannot be later than end time
3- When start date is before current date -   400 Bad Request
  start time cannot be pastdated
4- When deadline is after start date -    400 Bad Request
  deadline cannot be later than start time
```
## 2- Get all the Events created by a user - GET - http://localhost:8080/event/17   -   200 OK
```yaml
[
  {
    "id": 2,
    "title": "Badminton Tournament",
    "description": "San Jose Badminton Championship",
    "minParticipants": 2,
    "maxParticipants": 8,
    "totalParticipants": 2,
    "address": {
      "street": "101 E San Fernando, SJSU",
      "city": "San Jose",
      "state": "California",
      "zip": "95192"
    },
    "admissionPolicy": "first-come-first-served",
    "status": "FINISHED",
    "signupForumStatus": "CLOSED",
    "participantForumStatus": "OPEN",
    "fee": null,
    "startDateTime": "2022-05-10 19:30",
    "endDateTime": "2022-05-10 20:00",
    "deadline": "2022-05-10 19:15",
    "userId": 1,
    "userName": "Vivek Joshi"
  },
  {
    "id": 23,
    "title": "Yoga Bootcamp",
    "description": "Beginner Yoga Workshop",
    "minParticipants": 3,
    "maxParticipants": 100,
    "totalParticipants": 1,
    "address": {
      "street": "101 E San Fernando",
      "city": "San Jose",
      "state": "California",
      "zip": "95192"
    },
    "admissionPolicy": "first-come-first-served",
    "status": "CANCELLED",
    "signupForumStatus": "CLOSED",
    "participantForumStatus": "CANCELLED_NOT_ENOUGH_PARTICIPANTS",
    "fee": null,
    "startDateTime": "2022-05-10 20:40",
    "endDateTime": "2022-05-11 08:00",
    "deadline": "2022-05-10 20:30",
    "userId": 1,
    "userName": "Vivek Joshi"
  },
  {
    "id": 24,
    "title": "Painting Bootcamp",
    "description": "Learn Painting in 1 day",
    "minParticipants": 3,
    "maxParticipants": 100,
    "totalParticipants": 1,
    "address": {
      "street": "101 E San Fernando",
      "city": "San Jose",
      "state": "California",
      "zip": "95192"
    },
    "admissionPolicy": "first-come-first-served",
    "status": "CANCELLED",
    "signupForumStatus": "CLOSED",
    "participantForumStatus": "CANCELLED_NOT_ENOUGH_PARTICIPANTS",
    "fee": null,
    "startDateTime": "2022-05-10 20:40",
    "endDateTime": "2022-05-11 08:00",
    "deadline": "2022-05-10 20:20",
    "userId": 1,
    "userName": "Vivek Joshi"
  },
  {
    "id": 25,
    "title": "Meditation",
    "description": "Introduction to Meditation",
    "minParticipants": 3,
    "maxParticipants": 100,
    "totalParticipants": 0,
    "address": {
      "street": "101 E San Fernando",
      "city": "San Jose",
      "state": "California",
      "zip": "95192"
    },
    "admissionPolicy": "auto-approved",
    "status": "CANCELLED",
    "signupForumStatus": "CLOSED",
    "participantForumStatus": "CANCELLED_NOT_ENOUGH_PARTICIPANTS",
    "fee": null,
    "startDateTime": "2022-05-10 20:45",
    "endDateTime": "2022-05-11 08:14",
    "deadline": "2022-05-10 20:25",
    "userId": 1,
    "userName": "Vivek Joshi"
  },
  {
    "id": 28,
    "title": "Karate",
    "description": "Karate for beginners",
    "minParticipants": 3,
    "maxParticipants": 100,
    "totalParticipants": 0,
    "address": {
      "street": "101 E San Fernando",
      "city": "San Jose",
      "state": "California",
      "zip": "95192"
    },
    "admissionPolicy": "auto-approved",
    "status": "CANCELLED",
    "signupForumStatus": "CLOSED",
    "participantForumStatus": "CANCELLED_NOT_ENOUGH_PARTICIPANTS",
    "fee": null,
    "startDateTime": "2022-05-10 20:50",
    "endDateTime": "2022-05-11 08:00",
    "deadline": "2022-05-10 20:30",
    "userId": 1,
    "userName": "Vivek Joshi"
  }
]
```

## 3- When the event owner wants to update the ParticipantForumStatus - 
GET - http://localhost:8080/event/participant-forum-status/99?participantForumStatus=CLOSED_BY_ORGANIZER   -   200 OK
possible participantForumStatus values -> OPEN, CLOSED_BY_ORGANIZER, CANCELLED_NOT_ENOUGH_PARTICIPANTS, FINISHED
```yaml
{
  "id": 2,
  "title": "Badminton Tournament",
  "description": "San Jose Badminton Championship",
  "minParticipants": 2,
  "maxParticipants": 8,
  "totalParticipants": 2,
  "address": {
    "street": "101 E San Fernando, SJSU",
    "city": "San Jose",
    "state": "California",
    "zip": "95192"
  },
  "admissionPolicy": "first-come-first-served",
  "status": "FINISHED",
  "signupForumStatus": "CLOSED",
  "participantForumStatus": "FINISHED",
  "fee": null,
  "startDateTime": "2022-05-10 19:30",
  "endDateTime": "2022-05-10 20:00",
  "deadline": "2022-05-10 19:15",
  "userId": 1,
  "userName": "Vivek Joshi"
}
```
## 4- To get all the events in which a user tried to register -
GET - http://localhost:8080/event-participated/4
```yaml
[
  {
    "id": 2,
    "title": "Badminton Tournament",
    "description": "San Jose Badminton Championship",
    "minParticipants": 1,
    "maxParticipants": 8,
    "totalParticipants": 1,
    "address": {
      "street": "101 E San Fernando, SJSU",
      "city": "San Jose",
      "state": "California",
      "zip": "95192"
    },
    "admissionPolicy": "first-come-first-served",
    "status": "FINISHED",
    "signupForumStatus": "CLOSED",
    "participantForumStatus": "OPEN",
    "fee": null,
    "startDateTime": "2022-05-10 23:15",
    "endDateTime": "2022-05-10 23:18",
    "deadline": "2022-05-10 23:12",
    "userId": 1,
    "userName": "Vivek Joshi",
    "approvalStatus": "APPROVED"
  }
]
```

### get all the events in which the request was approved-
GET - http://localhost:8080/event-participated/4?approvalStatus=APPROVED
```yaml
req - http://localhost:8080/event-participated/7

[
  {
    "id": 15,
    "title": "coding bootcamp",
    "description": "code",
    "minParticipants": 5,
    "maxParticipants": 10,
    "totalParticipants": 2,
    "address": {
      "street": "328 N Market St",
      "city": "San Jose",
      "state": "California",
      "zip": "95110"
    },
    "admissionPolicy": "approval-required",
    "status": "FINISHED",
    "signupForumStatus": "OPEN",
    "participantForumStatus": "CLOSED_TILL_REGISTRATION_DEADLINE",
    "fee": 55,
    "startDateTime": "2022-05-25 21:55",
    "endDateTime": "2022-05-27 21:23",
    "deadline": "2022-05-24 21:55",
    "userId": 14,
    "userName": "amazon",
    "approvalStatus": "APPROVED",
    "reviewStar": 4.4,
    "reviewText": "GREAT coaching"
  }
]
```



# PARTICIPATE in EVENTS, Approve Participant's request
## 1- Register for an Event - POST - http://localhost:8080/participate
Provide userId for the user who is trying to participate and eventId for the event, to which they are trying to register for
```yaml


1- When event is registered as Approval Required  
  REQ-
  {
    "userId": 12,
    "eventId": 50,
    "sysDate":"2022-05-29 11:43"
    }
  RESPONSES-
    1- 200 OK
  {
    "id": 51,
    "approvalStatus": "PENDING",
    "participantName": "Vivek Joshi"
  }
    2- When user is trying to register again    400 Bad Request
          You have already signed up for this event
    3- When the user who created the event is trying to signup for the same event   400 Bad Request
         You can not signup for your own event
    4- When maxParticipants limit is reached      400 Bad Request
          You can not signup for this event, the event is full
    5- When the event has been closed or cancelled    400 Bad Request
          You can not signup for this event, this event has been closed or cancelled
2- When event is registered as first-come-first-serve, i.e. auto-approved   200 OK
  REQ-
  {
    "userId": 12,
    "eventId": 52,
    "sysDate":"2022-05-29 11:43"
  }
  RESPONSES-
  1- 200 OK
  {
    "id": 53,
    "approvalStatus": "APPROVED",
    "participantName": "Vivek Joshi"
  }
  2- When user is trying to register again    400 Bad Request
        You have already signed up for this event
  3- When the user who created the event is trying to signup for the same event   400 Bad Request
        You can not signup for your own event
  4- When maxParticipants limit is reached      400 Bad Request
        You can not signup for this event, the event is full
  5- When the event has been closed or cancelled    400 Bad Request
        You can not signup for this event, this event has been closed or cancelled

```

## 2- Get the details for the participants registered in an event
### 1 - GET - http://localhost:8080/participate/status/15
Provide the eventID for which the details are needed, this will return all the participants, irrespective of the approvalStatus.
```yaml
Response-
 {
    "participantList": [
        {
            "id": 14,
            "userId": 4,
            "approvalStatus": "APPROVED",
            "participantName": "Alice",
            "reviewStar": null,
            "reviewText": null
        }
    ],
    "startDateTime": "2022-05-25 10:00",
    "endDateTime": "2022-05-25 11:00"
}
```
### 2 - GET - http://localhost:8080/participate/status/50?approvalStatus=CANCELLED
```yaml
Response-
  [
    {
      "id": 51,
      "approvalStatus": "CANCELLED",
      "participantName": "Vivek Joshi"
    },
    {
      "id": 55,
      "approvalStatus": "CANCELLED",
      "participantName": "Tanya"
    }
  ]
```
GET - http://localhost:8080/participate/status/50?approvalStatus=PENDING
<br> 50 is the eventId, for which we are trying to see the pending approval requests
```yaml
[
    {
        "id": 54,
        "approvalStatus": "PENDING",
        "participantName": "paradise"
    },
    {
        "id": 55,
        "approvalStatus": "PENDING",
        "participantName": "Tanya"
    }
]
```


## 3- Approve/Reject participant's request for event registration
## POST - http://localhost:8080/participate/update-status
```yaml
1- When the participant is successfully approved for the event    200 OK
Request-
  {
    "participantId": 54,
    "approvalStatus": "APPROVED",
    "sysDate":"2022-05-29 11:43"
  }
Reponse-
  {
    "id": 54,
    "approvalStatus": "APPROVED",
    "participantName": "paradise"
  }

Request- for cancel
  {
    "participantId": 54,
    "approvalStatus": "CANCELLED"
  }
Response-
  {
    "id": 54,
    "approvalStatus": "CANCELLED",
    "participantName": "paradise"
  }

2- When the event is CANCELLED or CLOSED, and event owner tries to approve a request{this will also update the approval status to CANCELLED, in participant table for this participantId}    400 Bad Request
      You can not signup this user for this event, this event has been closed or cancelled
3- When event is full{this will also update the approval status to CANCELLED, in participant table for this participantId}           400 Bad Request
      You can not signup this user for this event, the event is full
```

```yaml
filter events  - 
## POST - http://localhost:8080/eventsByFilter

possible values of status for FILTERING to be accepted from FRONTEND - "ACTIVE","OPEN","ALL"
Request- for filter.
 {
    "city": "Cupertino",
    "keyword": "second",
    "status":"OPEN",
    "screenName":"Vivek Joshi1",
    "startDateTime":"2022-05-21 13:00",
    "endDateTime":"2022-05-24 15:00"
  }
  
 Response - 
[
    {
        "id": 20,
        "title": "second event",
        "description": "Testing Event Description",
        "minParticipants": 20,
        "maxParticipants": 40,
        "totalParticipants": 1,
        "address": {
            "street": "first",
            "city": "Cupertino",
            "state": "CA",
            "zip": "95110"
        },
        "admissionPolicy": "first-come-first-served",
        "status": "CLOSED",
        "fee": null,
        "startDateTime": "2022-05-21 14:00",
        "endDateTime": "2022-05-24 15:00",
        "deadline": "2022-05-08 11:00",
        "userId": 17,
        "userName": "Vivek Joshi1"
    }
]
```

# Signup Forum
## 1- Send a message - POST - http://localhost:8080/signup-forum
```yaml
In headers-
userId  12
eventId 87
sysDate 2022-05-10 14:00
message HI Testing through headers without image

TO send an image along with message
Send form-data , file -> image
RESPONSE--      200 OK    eventOwner would be true if msg is posted by the eventOwner
  {
    "id": 93,
    "sentBy": "Vivek Joshi",
    "message": "HI Testing through headers without image",
    "eventOwner": false,
    "imageURL": "https://cmpe275cec.s3.us-east-2.amazonaws.com/signup-93?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220509T014506Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518399&X-Amz-Credential=AKIAU6YG2LF3BXGJFFGI%2F20220509%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=518afe31f32a3f4dba2cd022784ed64a38b6914ea5a625f7db6a43d38d98a6d1",
    "messageDateTime": "2022-05-10T21:00:00.000+00:00"
  }
Without Image, no need to send anything in the form data, only send headers
  {
    "id": 94,
    "sentBy": "Vivek Joshi",
    "message": "HI Testing through headers without image",
    "eventOwner": false,
    "imageURL": null,
    "messageDateTime": "2022-05-10T21:00:00.000+00:00"
  }

When the event registration deadline has passed   -     400 Bad Request
      Closed for posting of new messages

```
## 2- To get all the signup forum msgs for an event, GET- http://localhost:8080/signup-forum/87
```yaml
Response -  200 OK
  [
    {
      "id": 94,
      "sentBy": "Vivek Joshi",
      "message": "HI Testing through headers without image",
      "eventOwner": false,
      "imageURL": null,
      "messageDateTime": "2022-05-10T21:00:00.000+00:00"
    },
    {
      "id": 93,
      
      "message": "HI Testing through headers without image",
      "eventOwner": false,
      "imageURL": "https://cmpe275cec.s3.us-east-2.amazonaws.com/signup-93?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220509T014506Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518399&X-Amz-Credential=AKIAU6YG2LF3BXGJFFGI%2F20220509%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=518afe31f32a3f4dba2cd022784ed64a38b6914ea5a625f7db6a43d38d98a6d1",
      "messageDateTime": "2022-05-10T21:00:00.000+00:00"
    },
    {
      "id": 89,
      "sentBy": "Vivek Joshi",
      "message": "HI Testing through headers without image",
      "eventOwner": false,
      "imageURL": null,
      "messageDateTime": "2022-05-10T21:00:00.000+00:00"
    },
    {
      "id": 88,
      "sentBy": "Vivek Joshi",
      "message": "HI Testing through headers without image",
      "eventOwner": false,
      "imageURL": "https://cmpe275cec.s3.us-east-2.amazonaws.com/signup-88?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220509T013853Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518399&X-Amz-Credential=AKIAU6YG2LF3BXGJFFGI%2F20220509%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=e2be6aada0381af0d7c1fd1ee660e5de4982ddee50e0ac868c6c5f638768fff2",
      "messageDateTime": "2022-05-10T21:00:00.000+00:00"
    }
  ]
```

# Participant Forum
## 1- Send a message - POST - http://localhost:8080/participant-forum
```yaml
In headers-
userId    12
eventId   87
sysDate   2022-05-10 14:00
message   HI Testing through headers without image

TO send an image along with message
Send form-data , file -> image

1 - When the participant is not approved for the event and tries to post a message to participant forum-
    400 Bad Request
      Only participants and the organizer can view and post in the participant forum

2 - When an owner sends a msg to participant forum      200 OK
  {
    "id": 102,
    "sentBy": "Vivek Joshi",
    "message": "Testing for an approved participant",
    "eventOwner": true,
    "imageURL": null,
    "messageDateTime": "2022-05-10T21:00:00.000+00:00"
  }
3 - When an approved participant sends a msg to participant forum       200 OK
  {
    "id": 103,
    "sentBy": "Vivek Joshi",
    "message": "Testing for an approved participant",
    "eventOwner": false,
    "imageURL": "https://cmpe275cec.s3.us-east-2.amazonaws.com/participantforum-103?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220509T025559Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518399&X-Amz-Credential=AKIAU6YG2LF3BXGJFFGI%2F20220509%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=1d4da497411a565d65826261c2d2747ed68e241f1eabca762fa2960e547a8a8c",
    "messageDateTime": "2022-05-10T21:00:00.000+00:00"
  }

4 - When participantForumStatus=CLOSED_BY_ORGANIZER for event 99
        400 Bad Request
        Participant Forum has been closed by the event owner

5 - When participantForumStatus=CANCELLED_NOT_ENOUGH_PARTICIPANTS for event 99
        400 Bad Request
        Event has been cancelled because not enough participants registered

6 - When participantForumStatus=FINISHED for event 99
        400 Bad Request
        You can not post new messages in this participant Forum, because the event has finished
  

```

## 2- To get all the participant forum msgs for an event, GET- http://localhost:8080/participant-forum/99/12
```yaml
[
  {
    "id": 103,
    "sentBy": "Vivek Joshi",
    "message": "Testing for an approved participant",
    "eventOwner": false,
    "imageURL": "https://cmpe275cec.s3.us-east-2.amazonaws.com/participantforum-103?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220509T025559Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518399&X-Amz-Credential=AKIAU6YG2LF3BXGJFFGI%2F20220509%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=1d4da497411a565d65826261c2d2747ed68e241f1eabca762fa2960e547a8a8c",
    "messageDateTime": "2022-05-10T21:00:00.000+00:00"
  },
  {
    "id": 102,
    "sentBy": "Vivek Joshi",
    "message": "Testing for an approved participant",
    "eventOwner": true,
    "imageURL": null,
    "messageDateTime": "2022-05-10T21:00:00.000+00:00"
  },
  {
    "id": 101,
    "sentBy": "Vivek Joshi",
    "message": "Testing for an approved participant",
    "eventOwner": false,
    "imageURL": null,
    "messageDateTime": "2022-05-10T21:00:00.000+00:00"
  }
]

1- When someone who is not owner of the event or participant tries to see the participant forum
      400 Bad Request
      Only participants and the organizer can view and post in the participant forum
```


## To update event status in DB permanently based on the system time set on front end
### POST - http://localhost:8080/update-time
REQ BODY ->  2022-05-10 20:01


# REPORTING
## 1- System Reports
POST - http://localhost:8080/report/system
```yaml
REQ->
  {
    "sysDate": "2021-06-18 11:59"
  }
RESPONSE->
  {
    "totalEvents": 4,
    "totalPaidEvents": 2,
    "totalCancelledEvents": 1,
    "totalMinimumParticipantsInCancelledEvents": 5,
    "totalParticipantRequestForCancelledEvents": 0,
    "totalFinishedEvents": 2,
    "totalParticipantsInFinishedEvents": 12
  }
```

## 2 - User Reports
POST - http://localhost:8080/report/user
```yaml
REQ->
{
  "userId" : 14,
  "sysDate": "2021-06-18 11:59"
}
RESPONSE->
  {
    "participationReport": {
      "totalSignupEvents": 0,
      "totalApprovals": 0,
      "totalRejects": 0,
      "totalFinishedEvents": 0
    },
    "organizerReport": {
      "totalEventsCreated": 1,
      "totalPaidEvents": 1,
      "totalCancelledEvents": 0,
      "totalMinimumParticipantsRequiredInCancelledEvents": 0,
      "totalParticipationRequests": 0,
      "totalParticipantsInFinishedEvents": 2,
      "totalFinishedEventsCreatedByThisUser": 1,
      "totalPaidEventThatFinished": 1,
      "totalRevenue": 110
    }
  }
```

##  REVIEWS AND REPUTATION -
1) Review for participants -  
POST - http://localhost:8080/reviewForParticipants
```yaml
Req -
reviewGivenBy corresponds to organizer id, reviewGivenTo corresponds to participant's user id, sysDate is current date.
 {
    "eventId": 15,
    "reviewGivenBy":14,
    "reviewGivenTo":9,
    "reviewText":"nice",
    "reviewStar":3.8,
    "sysDate":"2022-05-26 14:00"
    
  }
Response - 200 OK - successfully posted the review.
            400 Bad Request - date check, 120 length check.
```
2) Review for organizer -  
POST - http://localhost:8080/reviewForOrganizer
```yaml
Req -
reviewGivenBy corresponds to participant's user id, reviewGivenTo corresponds to organizer id, sysDate is current date.
  {
    "eventId": 15,
    "reviewGivenBy":9,
    "reviewGivenTo":14,
    "reviewText":"fairly good",
    "reviewStar":3.5,
    "sysDate":"2022-05-26 14:00"
    
  }
Response - 200 OK - successfully posted the review.
            400 Bad Request - date check, 120 length check.
```
3) Get Avg reputation and all reviews of participants who have requested to enrol in an event - 
id corresponds to participant's user id. 
Get - http://localhost:8080/getParticipantRepAndReviews?id=9
```yaml

Response -
{
    "avgReputation": 3.8,
    "reviews": [
        {
            "reviewerName": "amazon",
            "eventName": "coding bootcamp",
            "reviewStar": 3.8,
            "reviewText": "nice"
        }
    ]
}
```
4) Get My Reviews As a Participant. (all review given by organizers to user(participant))
id corresponds to participant's user id. 
Get - http://localhost:8080/getMyReviewsAsParticipant?id=7
```yaml

Response -
{
    "avgReputation": 5.0,
    "reviews": [
        {
            "reviewerName": "amazon",
            "eventName": "coding bootcamp",
            "reviewStar": 5.0,
            "reviewText": "okAY"
        }
    ]
}
```
5)Get My Reviews As a Organizer. (all review given by participants to organizer)
id corresponds to organizer id
Get - http://localhost:8080/getMyReviewsAsOrganizer?id=14
```yaml

Response -
{
    "avgReputation": 3.95,
    "reviews": [
        {
            "reviewerName": "sameer42",
            "eventName": "coding bootcamp",
            "reviewStar": 4.4,
            "reviewText": "GREAT coaching"
        },
        {
            "reviewerName": "sameer62",
            "eventName": "coding bootcamp",
            "reviewStar": 3.5,
            "reviewText": "fairly good"
        }
    ]
}
```
6) Get My reviews given as a participant. (All review which I have given to organizers).
id corresponds to participant's user id. 
Get - http://localhost:8080/getMyReviewsGivenAsParticipant?id=7
```yaml

Response -
{
    "avgReputation": 5.0,
    "reviews": [
        {
            "reviewerName": "sameer42",
            "eventName": "coding bootcamp",
            "reviewStar": 4.4,
            "reviewText": "GREAT coaching"
        }
    ]
}
```
7) Get my reviews given as a orgnazier. ( All review which i have given to participants of my events)
id corresponds to organizer id.
Get - http://localhost:8080/getMyReviewsGivenAsOrganizer?id=14
```yaml

Response -
{
    "avgReputation": 3.95,
    "reviews": [
        {
            "reviewerName": "amazon",
            "eventName": "coding bootcamp",
            "reviewStar": 5.0,
            "reviewText": "okAY"
        },
        {
            "reviewerName": "amazon",
            "eventName": "coding bootcamp",
            "reviewStar": 3.8,
            "reviewText": "nice"
        }
    ]
}
```
