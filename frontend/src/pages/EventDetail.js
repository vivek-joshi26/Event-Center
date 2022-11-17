import React, { useEffect, useState } from "react";
import axios from "../common/axiosInstance";
import moment from "moment";
import EventBusyIcon from "@mui/icons-material/EventBusy";
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Tooltip from "@mui/material/Tooltip";
import AlertModal from "../common/Alert";
import { Comments } from "../components/Comments";
import { Footer } from "../components/Footer"
import { LocalDrinkSharp } from "@mui/icons-material";
export const EventDetail = () => {
  const [details, setDetail] = useState(
    JSON.parse(localStorage.getItem("eventDetails")).data || null
  );
  const [isPopUp, setPopUp] = useState(false);
  const [errMsg, setErrMsg] = useState("");
  const [eventId, setEventId] = useState(details.id);
  const [paid, setHasPaid] = useState(false);
  const [disableInputSignUpForum, setDisableInputSignUpForum] = useState(false);
  const [hideInputParticipantForum, setHideInputParticipantForum] = useState(false);
  const [disableInputParticipantForum, setDisableInputparticipantForum] =
    useState(false);
  const [buttonLabel, setButtonLabel] = useState("Sign Up");
  const userId = localStorage.getItem("userId") || null;
  const [signupForumData, setSignupForumData] = useState([]);
  const [participantForumData, setParticipantForumData] = useState([]);
  const [hideFooter, setHideFooter] = useState(false);
  const [orgId, setOrgId] = useState(details.userId);
  const [isOrg, setIsOrg, getOrg] = useState(false)
  const signupForumStatus = details.signupForumStatus;
  const participantForumStatus = details.participantForumStatus;

  /**
  * * fetch forum data on load
  *  ! if event organizer or it's not search page, hide footer
  */
  useEffect(() => {
    canSignUp();
    loadSignUpForum();
    loadParticpantForum();
    //   return () => {
    //     localStorage.removeItem('participant-forum-status')
    // }
  }, []);



  /**
   * * For loading Sign up form
   * ! error - deadline passed, event status === closed/cancel ---> disable forum
   *  
   */
  const loadSignUpForum = () => {
    axios
      .get(`/signup-forum/${eventId}`)
      .then((res) => {
        if (res.status >= 200) {
          setSignupForumData(res.data);
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        showPopUp(err.response.data);
      })
      .finally(() => {
        if (hasDeadlinePassed() || details.status === "CANCELLED" || details.status === "CLOSED"
          || details.signupForumStatus === "CLOSED"
        )
          setDisableInputSignUpForum(true);
      });
  };

  /**
     * * For loading participant up form
     * ! deadline hasn't passed ----> return and hide partipant forum
     * ! on Success - paid user
     * ! if it's been 72 hrs or forum is cancelled, closed etc then disable forum
     * ! error - disable forum 
     *  
     */
  const loadParticpantForum = () => {

    axios
      .get(`/participant-forum/${eventId}/${userId}`)
      .then((res) => {
        if (res.status >= 200) {
          setParticipantForumData(res.data);
          setButtonLabel("You're going!");
          setHasPaid(true);
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        setHideInputParticipantForum(true);
        showPopUp(err.response.data);
      })
      .finally(() => {
        let endDate = moment(details.endDateTime, 'YYYY-MM-DD HH:mm')
        let currDate = moment(new Date(), 'YYYY-MM-DD HH:mm')
        console.log(currDate.diff(endDate, 'hour'));
        if (details.participantForumStatus.includes('CLOSED') 
          || details.participantForumStatus.includes('CANCELLED')) {
          console.log('closed')
          setDisableInputparticipantForum(true);
        }

      })
  };

  /**
   * ! hide footer condition on organizer and page url is not search
   */
  const canSignUp = () => {
    if (parseInt(orgId) === parseInt(userId)) setIsOrg(true);
    const isSearchPage = window.location.pathname.includes("search-event")
      ? true
      : false;
    if (parseInt(orgId) === parseInt(userId) || !isSearchPage) {
     
      setHideFooter(true);
    }
  };

  /**
   * ! user has paid for paid event
   */
  const setPaid = () => {
    setHasPaid(true);
  };

  /**
   * 
   * @returns true, if deadline passed else false
   */
  const hasDeadlinePassed = () => {
    let sysDate= localStorage.getItem('sysDate') ? localStorage.getItem('sysDate') : moment(new Date()).format('YYYY-MM-DD HH:mm')
    console.log(moment(details.deadline).format('YYYY-MM-DD HH:mm') <  moment(sysDate).format('YYYY-MM-DD HH:mm'));
    return  moment(details.deadline).format('YYYY-MM-DD HH:mm') <  moment(sysDate).format('YYYY-MM-DD HH:mm');
  };
  /**
   * 
   * @returns true if event capacity is full
   */
  const isEventNotFull = () => {
    console.log(
      parseInt(details.maxParticipants) +
      " " +
      parseInt(details.totalParticipants)
    );
    return (
      parseInt(details.maxParticipants) > parseInt(details.totalParticipants)
    );
  };
  /**
   * * for error notification pop up
   */
  const setPopUpVal = () => {
    setPopUp(!isPopUp);
  };
  const showPopUp = (msg) => {
    setPopUpVal();
    setErrMsg(msg);
  };

  /**
   * * sign up event if user is has paid
   */
  const handleSubmit = (event) => {

    event.preventDefault();
    console.log("in submit" + paid);
    if (!paid && details.fee && details.fee > 0) {
      showPopUp("You need to approve your willingness to pay first");
      return;
    }
    const obj = {
      userId: parseInt(localStorage.getItem("userId")),
      eventId: eventId,
      "sysDate": localStorage.getItem('sysDate') ? localStorage.getItem('sysDate') : moment(new Date()).format('YYYY-MM-DD HH:mm')
    };

    axios
      .post("/participate", obj)
      .then((res) => {
        if (res.status >= 200) {
          setButtonLabel("Signed Up!");
          showPopUp(
            `Thank you for registering your interest!  Your Approval status is ${res.data.approvalStatus}!`
          );
          setParticipantForumData([])
          loadParticpantForum();
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        showPopUp(err.response.data);
      });
  };

  return (
    <div>

      {!hideFooter && (
        <Footer
          handleSubmit={handleSubmit}
          paid={paid}
          fee={details.fee}
          admPolicy={details.admissionPolicy}
          isEventNotFull={isEventNotFull}
          hasDeadlinePassed={hasDeadlinePassed}
          setPaid={setPaid}
          buttonLabel={buttonLabel}
          orgId={orgId}
          status ={details?.status}
        />
      )}

      <Box
        sx={{
          marginTop: 2,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography variant="h5">
          <b>{details.title}</b>
        </Typography>
        <Typography variant="h7">
          <b>Hosted By: {details.userName}</b>
        </Typography>

        <Card sx={{ minWidth: "90%", borderRadius: "2%" }} variant="outlined">
          <CardContent>
            <Typography
              sx={{
                mb: 1.5,
                display: "flex",
                flexDirection: "row ",
                justifyContent: "space-between",
              }}
              color="text.primary"
              gutterBottom
            >
              <Typography sx={{ fontSize: 14 }} color="text.primary">
                <h4>Details</h4>
                {details.description}
              </Typography>
              {!isEventNotFull() && (
                <Tooltip title="Event is Full" placement="top-start">
                  <EventBusyIcon />
                </Tooltip>
              )}
            </Typography>
            <hr />
            <Typography sx={{ fontSize: 14,fontSize: 14,display: 'flex',
                            justifyContent: 'flex-start', alignItems: 'center' }} component="div">
              <h4>Time</h4>
              <div style={{marginLeft:'1rem'}}>   {moment(details.startDateTime).format("LLL")} -{" "}
              {moment(details.endDateTime).format("LLL")}</div>
           
            </Typography>
            {details.address.city && (
              <Typography sx={{ fontSize: 14,fontSize: 14,display: 'flex',
              justifyContent: 'flex-start', alignItems: 'center' }} color="text.primary">
                <h4>City</h4>
                <div style={{marginLeft:'1rem'}}>   {details.address.city}</div>
              
              </Typography>
            )}
             {details.minParticipants!= null && (
              <Typography variant="body2" sx={{ fontSize: 14,fontSize: 14,display: 'flex',
              justifyContent: 'flex-start', alignItems: 'center' }}>
                <h4>Min. Participants</h4>
                <div style={{marginLeft:'1rem'}}> {details.minParticipants}</div>
               
              </Typography>
            )}
            {details.maxParticipants!= null && (
              <Typography variant="body2" sx={{ fontSize: 14,fontSize: 14,display: 'flex',
              justifyContent: 'flex-start', alignItems: 'center' }}>
                <h4>Max. Participants</h4>
                <div style={{marginLeft:'1rem'}}>{details.maxParticipants}</div>
                
              </Typography>
            )}
             {details?.totalParticipants!= null && (
              <Typography variant="body2" sx={{ fontSize: 14,fontSize: 14,display: 'flex',
              justifyContent: 'flex-start', alignItems: 'center' }}>
                <h4>Total Participants</h4>
                <div style={{marginLeft:'1rem'}}>{details.totalParticipants}</div>
               
              </Typography>
            )} 
            {details.status && (
              <Typography variant="body2" sx={{ fontSize: 14,fontSize: 14,display: 'flex',
              justifyContent: 'flex-start', alignItems: 'center' }}>
                <h4>Status</h4>
                <div style={{marginLeft:'1rem'}}>{details.status}</div>
                
              </Typography>
            )}
          </CardContent>

          <CardActions>
            {/* <Button size="small" onClick={handleDetail} >
            Learn More
          </Button> */}
          </CardActions>
        </Card>
      </Box>
      <Box
        sx={{
          marginTop: 2,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
          <Typography variant="h6" component="div" sx={{ textTransform: 'capitalize'}}>Sign Up Forum</Typography>
        <Comments
          forumData={signupForumData}
          disableInput={disableInputSignUpForum}
          setForumData={setSignupForumData}
          isOrg={isOrg}
          title={'Sign Up Forum'}
          isSignUp={true}
          eventId={eventId}
          status={signupForumStatus}
          endDate={details.endDateTime}
        />

        {
          !hideInputParticipantForum &&
          (
            <>
            <Typography variant="h6" component="div" sx={{ textTransform: 'capitalize' }}>Participation Forum</Typography>
            <Comments
              forumData={participantForumData}
              disableInput={disableInputParticipantForum}
              title={'Participant Forum'}
              isSignUp={false}
              isOrg={isOrg}
              eventId={eventId}
              status={participantForumStatus}
              endDate={details.endDateTime}
              loadForum={loadParticpantForum}
            />
            </>
          )
        }



      </Box>
      {isPopUp && (
        <AlertModal open={isPopUp} msg={errMsg} modal={setPopUpVal} />
      )}
    </div>
  );
};


