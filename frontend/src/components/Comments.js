import React, { useEffect, useState } from "react";
import Paper from "@mui/material/Paper";
import { TextInput } from "./TextInput.js";
import { MessageLeft, MessageRight } from "./Message";
import "./Comments.css";
import Button from "@mui/material/Button";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import moment from "moment";
import { Typography } from "@mui/material";
import InputLabel from "@mui/material/InputLabel";
import axios from "../common/axiosInstance"
export const Comments = (props) => {
  const [disableInput, setDisableInput] = useState(props.disableInput || false);
  const [forumData, setForumData] = useState(props.forumData || []);
  const isSignUp = props?.isSignUp ? props?.isSignUp : false;
  const [isOrg, setIsOrg] = useState(props.isOrg || false)
  const [status, setStatus] = useState(props.status || null)
  const [error, setErrMsg] = useState(null)
  const eventId = props?.eventId || null;
  const [isOpen, setIsOpen] = useState(false)
  const [disableIsFinish, setDisabledIsFinish] = useState(false)
  const endDate = props.endDate;
  useEffect(() => {
    console.log(props)
    setForumData(props.forumData)
    setDisableInput(props.disableInput)
    setIsOrg(props.isOrg)
    setStatus(props.status)
    setErrMsg(FORUM_STATUS_TYPE[status])
    /**
     * ! only for btn
     */
    if (props.status.includes('OPEN')) {
      setIsOpen(true)
    }
    if (!isSignUp && status.includes('CLOSED') || !isSignUp && status.includes('CANCELLED')
    ) {
      setDisabledIsFinish(true)
      setErrMsg(FORUM_STATUS_TYPE[status])
    }

  }, [props])

 

  const closeParticipantForum = (e) => {
    e.preventDefault();
    axios
      .get(`/event/participant-forum-status/${eventId}?participantForumStatus=CLOSED_BY_ORGANIZER`)
      .then((res) => {
        if (res.status == 200) {
          setErrMsg(FORUM_STATUS_TYPE['CLOSED_BY_ORGANIZER'])
          setDisableInput(true)
          setIsOpen(false)
        }
      })
      .catch((err) => {
        console.log("in catch", err);
      })
  }

  return (

    <div className="comment-parent-div">

      <div className="org-actions">
        <>
          <Typography variant="h5" mb={2} className="typography">{isOpen}</Typography>
          {
            isOrg && !isSignUp && isOpen && (
              <Button variant="contained" size="small" mb={2} sx={{ width: '15%' }} onClick={closeParticipantForum} disabled={disableIsFinish}>
                Close Forum
              </Button>
            )
          }
        </>
      </div>
      <Paper variant="outlined" className="paper-one">
        <Paper id="style-1" className="paper-two">

          {forumData && forumData.map((data, i) => (
            <MessageLeft key={i}
              message={data.message}
              timestamp={moment(data.messageDateTime).format("LLL")}
              photoURL=""
              displayName={data.sentBy}
              avatarDisp={true}
              isEventOwner={data.eventOwner}
              imageURL={data.imageURL}
            />
          ))}
        </Paper>
        {
          error && (
            <Typography>{error}</Typography>
          )
        }

        <TextInput disableInput={disableInput} setForumData={setForumData} isSignUp={isSignUp} />

      </Paper>
    </div>
  );
};

export const FORUM_STATUS_TYPE = {
  CLOSED: "Deadline has passed to sign Up",
  CLOSED_BY_ORGANIZER: "Closed By Organizer of this event",
  CANCELLED_NOT_ENOUGH_PARTICIPANTS: "Cancelled due to not having enough participants",
  FINISHED: "Event is Over",
  CLOSED_TILL_REGISTRATION_DEADLINE: "Accessible after the Sign Up for this event is over",
  CLOSED: "Deadline has passed to sign Up",

}