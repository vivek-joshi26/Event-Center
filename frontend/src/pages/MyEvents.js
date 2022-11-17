import React, { useEffect, useState } from "react";
import axios from "../common/axiosInstance";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import Accordion from '@mui/material/Accordion';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import moment from "moment";
import Paper from '@mui/material/Paper';
import ThumbDownOffAltIcon from '@mui/icons-material/ThumbDownOffAlt';
import ThumbUpOffAltIcon from '@mui/icons-material/ThumbUpOffAlt';
import PersonOutlineIcon from '@mui/icons-material/PersonOutline';
import AccessTimeFilledIcon from '@mui/icons-material/AccessTimeFilled';
import AlertModal from "../common/Alert";
import Divider from '@mui/material/Divider';
import EventIcon from '@mui/icons-material/Event';
import { Review } from "../components/Review";
import { AllReviews } from "../components/AllReviews";
import Alert from '@mui/material/Alert';
import Tooltip from "@mui/material/Tooltip";


export const MyEvents = () => {
  const userId = localStorage.getItem('userId') || null;
  const [eventHostedData, setEventHostedData] = useState([])
  const [expanded, setExpanded] = useState(false);
  const [attendees, setAttendees] = useState([])
  const [eventAttendedDataApproved, setEventAttendedDataApproved] = useState([])
  const [eventAttendedDataRejected, setEventAttendedDataRejected] = useState([])
  const [eventAttendedDataPending, setEventAttendedDataPending] = useState([])
  const [eventAttendedDataCancelled, setEventAttendedDataCancelled] = useState([])

  const [isPopUp, setPopUp] = useState(false);
  const [errMsg, setErrMsg] = useState("");
  const handleChange = (panel, id) => (event, isExpanded) => {
    setAttendees([])
    setExpanded(isExpanded ? panel : false);
    if (isNaN(parseInt(id))) return;
    if (isExpanded) loadAttendees(id)


  };

  useEffect(() => {
    loadMyOrganizedEvents()
    loadMyAttendedEventsApproved()
    loadMyAttendedEventsPending()
    loadMyAttendedEventsRejected()
    loadMyAttendedEventsCancelled()

  }, []);
  const setPopUpVal = () => {
    setPopUp(!isPopUp);
  };
  const showPopUp = (msg) => {
    setPopUpVal();
    setErrMsg(msg);
  };
  const loadMyOrganizedEvents = () => {
    axios
      .get(`/event/${userId}`)
      .then((res) => {
        if (res.status == 200) {
          if (res.data.length > 0) setEventHostedData(res.data)
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        showPopUp(err.response.data);
      })
  }
  const loadMyAttendedEventsApproved = () => {
    axios
      .get(`/event-participated/${userId}/?approvalStatus=APPROVED`)
      .then((res) => {
        console.log(res.data)
        if (res.status == 200) {
          if (res.data.length > 0) setEventAttendedDataApproved(res.data)
        }

      })
      .catch((err) => {
        console.log("in catch", err);
        // showPopUp(err.response.data);
      })
  }
  const loadMyAttendedEventsPending = () => {
    axios
      .get(`/event-participated/${userId}/?approvalStatus=PENDING`)
      .then((res) => {
        console.log(res.data)
        if (res.status == 200) {
          if (res.data.length > 0) setEventAttendedDataPending(res.data)
        }

      })
      .catch((err) => {
        console.log("in catch", err);
        // showPopUp(err.response.data);
      })
  }
  const loadMyAttendedEventsRejected = () => {
    axios
      .get(`/event-participated/${userId}/?approvalStatus=REJECTED`)
      .then((res) => {
        console.log(res.data)
        if (res.status == 200) {
          if (res.data.length > 0) setEventAttendedDataRejected(res.data)
        }

      })
      .catch((err) => {
        console.log("in catch", err);
        // showPopUp(err.response.data);
      })
  }

  const loadMyAttendedEventsCancelled = () => {
    axios
      .get(`/event-participated/${userId}/?approvalStatus=CANCELLED`)
      .then((res) => {
        console.log(res.data)
        if (res.status == 200) {
          if (res.data.length > 0) setEventAttendedDataCancelled(res.data)
        }

      })
      .catch((err) => {
        console.log("in catch", err);
        // showPopUp(err.response.data);
      })
  }


  const loadAttendees = (id) => {
    console.log(id)
    axios
      .get(`/participate/status/${id}`)
      .then((res) => {
        console.log(res.data)
        if (res.status == 200) {
          if (res.data.participantList.length > 0) setAttendees(res.data)
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        showPopUp(err.response.data);
      })
  }

  const handleApproveRequest = (id) => {
    const req = {
      "participantId": +id,
      "approvalStatus": "APPROVED",
      "sysDate": localStorage.getItem('sysDate') ? localStorage.getItem('sysDate') : moment(new Date()).format('YYYY-MM-DD HH:mm')
    }
    axios
      .post(`/participate/update-status`, req)
      .then((res) => {
        console.log(res.data)
        if (res.status == 200) {

          window.location.reload();
        }

      })
      .catch((err) => {
        console.log("in catch", err);
        showPopUp(err.response.data);
      })
  }
  const handleRejectRequest = (id) => {
    console.log(id)
    const req = {
      "participantId": +id,
      "approvalStatus": "REJECTED",
      "sysDate": localStorage.getItem('sysDate') ? localStorage.getItem('sysDate') : moment(new Date()).format('YYYY-MM-DD HH:mm')
    }
    axios
      .post(`/participate/update-status`, req)
      .then((res) => {
        if (res.status == 200) {
          window.location.reload();
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        showPopUp(err.response.data);
      })
  }

  const canReview = (startDate, endDate) => {
    let sysDate = localStorage.getItem('sysDate') ? localStorage.getItem('sysDate') : moment(new Date()).format('YYYY-MM-DD HH:mm')
    if (moment(startDate) < moment(sysDate) && moment(endDate).add(7, 'days').format('YYYY-MM-DD HH:mm') >= moment(sysDate).format('YYYY-MM-DD HH:mm'))
      return true;
    else return false;
  }

  return (
    <div>

      <>
        <Alert severity="info">Please click on Accordion to give or see the review</Alert>
        <Typography variant="h5" mt={2} mb={2} ml={2}>Your Events as an Host</Typography>
        {
          eventHostedData.map((data, i) => {
            return (
              <Accordion expanded={expanded === data?.id} onChange={handleChange(data?.id, data?.id)} key={data?.id} sx={{ background: 'beige' }}>
                <AccordionSummary
                  expandIcon={<Tooltip title="View/Give Attendees Review" placement="top-start">
                    <ExpandMoreIcon />
                  </Tooltip>}
                  aria-controls="panel1bh-content"
                  id="panel1bh-header"
                >
                  <div>
                    <Typography variant="h6" component="div" sx={{ textTransform: 'capitalize', width: '23rem' }}>
                      {data?.title}
                      <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
                        background: EVENT_STATUS[data?.status],
                        borderRadius: '1rem',
                        textAlign: 'center',
                        width: '40%',
                        display: 'inline-table'
                      }}>
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                          <EventIcon />{data?.status}
                        </div>
                      </Typography>
                    </Typography>
                    <Typography variant="body2">
                      <br />
                      <div style={{
                        display: 'inline-flex',
                        alignItems: 'center'
                      }}>
                        <AccessTimeFilledIcon />
                        {moment(data?.startDateTime).format('LLL')} - {moment(data?.endDateTime).format('LLL')}
                      </div>
                    </Typography>
                  </div>
                </AccordionSummary>
                <AccordionDetails>
                  <Box
                    sx={{
                      display: 'flex',
                      flexWrap: 'wrap',
                      '& > :not(style)': {
                        m: 1,
                        maxWidth: 400,
                        maxHeight: 300,
                      },
                    }}
                  >
                    {attendees?.participantList?.map((attendee, i) => (
                      <Paper elevation={3} key={i}>
                        <div style={{
                          display: 'flex',
                          flexDirection: 'column',
                          margin: '0.2rem'
                        }}>
                          <Typography variant="subtitle2" component="div" sx={{
                            display: 'inline-flex',
                            alignItems: 'self-end'
                          }}>
                            <PersonOutlineIcon />{attendee?.participantName}
                          </Typography>
                          <Typography variant="subtitle2" mt={1} sx={{
                            background: STATUS[attendee?.approvalStatus],
                            borderRadius: '1rem',
                            textAlign: 'center',
                            width: 150
                          }}>
                            {attendee?.approvalStatus}
                          </Typography>
                          <br />
                          {PARTICIPANT_STATUS[attendee?.approvalStatus]}
                          {PARTICIPANT_STATUS[attendee?.approvalStatus] == true && (
                            <div style={{ display: 'inline-flex' }}>
                              <Button onClick={(e) => { handleApproveRequest(attendee?.id); }}> <ThumbUpOffAltIcon /></Button>
                              <Button onClick={(e) => { handleRejectRequest(attendee?.id); }}><ThumbDownOffAltIcon /></Button>
                              <AllReviews id={attendee?.userId} forOrg={false} />
                            </div>
                          )}

                        </div>
                        {
                          attendee?.approvalStatus === 'APPROVED'
                          && canReview(attendees?.startDateTime, attendees?.endDateTime)
                          && (
                            <div>
                              <Review
                                reviewStar={attendee?.reviewStar}
                                eventId={data?.id}
                                reviewGivenBy={userId}
                                reviewGivenTo={attendee?.userId}
                                reviewText={attendee?.reviewText}
                                forOrg={false}
                              />
                            </div>
                          )

                        }
                      </Paper>
                    ))}
                  </Box>
                </AccordionDetails>
              </Accordion>
            );
          })
        }
        {
          eventHostedData.length === 0 && (
            <Paper elevation={3}><Typography mt={2} ml={1} mb={1}>You have not hosted events</Typography></Paper>
          )
        }



      </>
      <Divider />

      <Typography variant="h5" mt={2} mb={2} ml={2}>Your Events as an Attendee
      </Typography>
      <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
        background: STATUS['APPROVED'],
        borderRadius: '1rem',
        textAlign: 'center',
        width: '10%',
        display: 'inline-table'
      }}>
        APPROVED
      </Typography>
      {
        eventAttendedDataApproved.map((data, i) => {
          return (
            <Accordion key={data?.id} sx={{ background: 'aliceblue' }} mb={1}>
              <AccordionSummary
               expandIcon={<Tooltip title="View/Give Organizer Review" placement="top-start">
               <ExpandMoreIcon />
             </Tooltip>}
                aria-controls="panel1bh-content"
                id="panel1bh-header"
              >
                <div>
                  <Typography variant="h6" component="div">
                    {data?.title}
                    <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
                      background: EVENT_STATUS[data?.status],
                      borderRadius: '1rem',
                      textAlign: 'center',
                      width: '40%',
                      display: 'inline-table'
                    }}>
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <EventIcon />{data?.status}
                      </div>
                    </Typography>
                  </Typography>
                  <Typography variant="body2">
                    {/* Event Status: {data?.status} */}
                    {/* <div>
                      {
                        data?.approvalStatus && (
                          <>
                            Sign Up Request {" "}
                            <Typography variant="subtitle2" mt={1} sx={{
                              background: STATUS[data?.approvalStatus],
                              borderRadius: '1rem',
                              textAlign: 'center',
                              width: '40%',
                              display: 'inline-table'
                            }}>
                              {data?.approvalStatus}
                            </Typography>
                          </>
                        )
                      }

                    </div> */}
                    <br />
                    <div style={{
                      display: 'inline-flex',
                      alignItems: 'center'
                    }}>
                      <AccessTimeFilledIcon />
                      {moment(data?.startDateTime).format('LLL')} - {moment(data?.endDateTime).format('LLL')}
                    </div>
                  </Typography>
                </div>
              </AccordionSummary>
              <AccordionDetails>
                <Box
                  sx={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    '& > :not(style)': {
                      m: 1,
                      width: 400,
                      height: 300,
                    },
                  }}
                >
                  {
                    canReview(data?.startDateTime, data?.endDateTime) && (
                      <div>
                        <Review
                          reviewStar={data?.reviewStar}
                          eventId={data?.id}
                          reviewGivenBy={userId}
                          reviewGivenTo={data?.userId}
                          reviewText={data?.reviewText}
                          forOrg={true}
                        />
                      </div>
                    )

                  }
                </Box>
              </AccordionDetails>
            </Accordion>
          );
        })

      }
      {
        eventAttendedDataApproved.length === 0 && (
          <Paper elevation={3}><Typography mt={2} ml={1} mb={1}>You don't have any events with Approved Sign up Request</Typography></Paper>
        )
      }
      <Divider />
      <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
        background: STATUS['PENDING'],
        borderRadius: '1rem',
        textAlign: 'center',
        width: '10%',
        display: 'inline-table'
      }}>
        PENDING
      </Typography>

      {
        eventAttendedDataPending.map((data, i) => {
          return (
            <Accordion key={data?.id} sx={{ background: 'aliceblue' }} mb={1}>
              <AccordionSummary
                aria-controls="panel1bh-content"
                id="panel1bh-header"
              >
                <div>
                  <Typography variant="h6" component="div">
                    {data?.title}
                    <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
                      background: EVENT_STATUS[data?.status],
                      borderRadius: '1rem',
                      textAlign: 'center',
                      width: '40%',
                      display: 'inline-table'
                    }}>
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <EventIcon />{data?.status}
                      </div>
                    </Typography>
                  </Typography>
                  <Typography variant="body2" >

                    {/* <div>
                      {
                        data?.approvalStatus && (
                          <>
                            Sign Up Request {" "}
                            <Typography variant="subtitle2" mt={1} sx={{
                              background: STATUS[data?.approvalStatus],
                              borderRadius: '1rem',
                              textAlign: 'center',
                              width: '40%',
                              display: 'inline-table'
                            }}>
                              {data?.approvalStatus}
                            </Typography>
                          </>
                        )
                      }

                    </div> */}
                    <br />
                    <div style={{
                      display: 'inline-flex',
                      alignItems: 'center'
                    }}>
                      <AccessTimeFilledIcon />
                      {moment(data?.startDateTime).format('LLL')} - {moment(data?.endDateTime).format('LLL')}
                    </div>
                  </Typography>
                </div>
              </AccordionSummary>
            </Accordion>
          );
        })

      }
      {
        eventAttendedDataPending.length === 0 && (
          <Paper elevation={3}><Typography mt={2} ml={1} mb={1}>You don't have any events with Pending Sign up Request</Typography></Paper>
        )
      }
      <Divider />
      <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
        background: STATUS['REJECTED'],
        borderRadius: '1rem',
        textAlign: 'center',
        width: '10%',
        display: 'inline-table'
      }}>
        REJECTED
      </Typography>
      {
        eventAttendedDataRejected.map((data, i) => {
          return (
            <Accordion key={data?.id} sx={{ background: 'aliceblue' }} mb={1}>
              <AccordionSummary
                aria-controls="panel1bh-content"
                id="panel1bh-header"
              >
                <div>
                  <Typography variant="h6" component="div" >
                    {data?.title}
                    <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
                      background: EVENT_STATUS[data?.status],
                      borderRadius: '1rem',
                      textAlign: 'center',
                      width: '40%',
                      display: 'inline-table'
                    }}>
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <EventIcon />{data?.status}
                      </div>
                    </Typography>
                  </Typography>
                  <Typography variant="body2">

                    {/* <div>
                      {
                        data?.approvalStatus && (
                          <>
                            Sign Up Request {" "}
                            <Typography variant="subtitle2" mt={1} sx={{
                              background: STATUS[data?.approvalStatus],
                              borderRadius: '1rem',
                              textAlign: 'center',
                              width: '40%',
                              display: 'inline-table'
                            }}>
                              {data?.approvalStatus}
                            </Typography>
                          </>
                        )
                      }

                    </div> */}
                    <br />
                    <div style={{
                      display: 'inline-flex',
                      alignItems: 'center'
                    }}>
                      <AccessTimeFilledIcon />
                      {moment(data?.startDateTime).format('LLL')} - {moment(data?.endDateTime).format('LLL')}
                    </div>
                  </Typography>
                </div>
              </AccordionSummary>
            </Accordion>
          );
        })

      }
      {
        eventAttendedDataRejected.length === 0 && (
          <Paper elevation={3}><Typography mt={2} ml={1} mb={1}>You don't have any events with Rejected Sign up Request</Typography></Paper>
        )
      }
      <Divider />
      <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
        background: STATUS['CANCELLED'],
        borderRadius: '1rem',
        textAlign: 'center',
        width: '10%',
        display: 'inline-table'
      }}>
        CANCELLED
      </Typography>
      {
        eventAttendedDataCancelled.map((data, i) => {
          return (
            <Accordion key={data?.id} sx={{ background: 'aliceblue' }} mb={1}>
              <AccordionSummary
                aria-controls="panel1bh-content"
                id="panel1bh-header"
              >
                <div>
                  <Typography variant="h6" component="div" >
                    {data?.title}
                    <Typography variant="subtitle2" mt={2} mb={2} ml={2} sx={{
                      background: EVENT_STATUS[data?.status],
                      borderRadius: '1rem',
                      textAlign: 'center',
                      width: '40%',
                      display: 'inline-table'
                    }}>
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <EventIcon />{data?.status}
                      </div>
                    </Typography>
                  </Typography>
                  <Typography variant="body2">

                    {/* <div>
                      {
                        data?.approvalStatus && (
                          <>
                            Sign Up Request {" "}
                            <Typography variant="subtitle2" mt={1} sx={{
                              background: STATUS[data?.approvalStatus],
                              borderRadius: '1rem',
                              textAlign: 'center',
                              width: '40%',
                              display: 'inline-table'
                            }}>
                              {data?.approvalStatus}
                            </Typography>
                          </>
                        )
                      }

                    </div> */}
                    <br />
                    <div style={{
                      display: 'inline-flex',
                      alignItems: 'center'
                    }}>
                      <AccessTimeFilledIcon />
                      {moment(data?.startDateTime).format('LLL')} - {moment(data?.endDateTime).format('LLL')}
                    </div>
                  </Typography>
                </div>
              </AccordionSummary>
            </Accordion>
          );
        })

      }
      {
        eventAttendedDataRejected.length === 0 && (
          <Paper elevation={3}><Typography mt={2} ml={1} mb={1}>You don't have any events with Cancelled Sign up Request</Typography></Paper>
        )
      }
      {isPopUp && (
        <AlertModal open={isPopUp} msg={errMsg} modal={setPopUpVal} />
      )}
    </div >

  );
};

export const PARTICIPANT_STATUS = {
  "PENDING": true,
  "APPROVED": false,
  "REJECTED": false,
  "CANCELLED": false
}

export const STATUS = {
  "APPROVED": 'aquamarine',
  "REJECTED": '#de3421',
  "CANCELLED": "#349feb",
  "PENDING": "#dede21"
}

export const EVENT_STATUS = {
  "OPEN": "#42f5b3",
  "CLOSED": "#2c36bf",
  "CANCELLED": "#349feb",
  "ONGOING": "#dbd22c",
  "FINISHED": "#368de3"
}