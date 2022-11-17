import React, { useState, useEffect } from "react";
import SendIcon from "@mui/icons-material/Send";
import "./TextInput.css";
import moment from "moment";
import axios from "../common/axiosInstance";
import AttachFileIcon from "@mui/icons-material/AttachFile";
import Paper from "@mui/material/Paper";
import InputBase from "@mui/material/InputBase";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";

export const TextInput = (props) => {
  const [disableInput, setDisableInput] = useState(props.disableInput || false);
  const [msg, setMsg] = useState(null);
  const user = localStorage.getItem("userId") || null;
  const eventId =
    JSON.parse(localStorage.getItem("eventDetails")).data.id || null;
  const url = props?.isSignUp ? "/signup-forum" : "/participant-forum";
  const [file, setFile] = useState(null);
  useEffect(() => {
    console.log(props);
    setDisableInput(props.disableInput);
  }, [props]);

  const postMsg = (event) => {
    event.preventDefault();
    if (
      user == null ||
      eventId == null ||
      url === "" ||
      (file == null && msg == null)
    )
      return;

    const config = {
      headers: {
        "content-type": "multipart/form-data",
      },
    };

    let formData = new FormData();
    let sysDate=localStorage.getItem('sysDate')? localStorage.getItem('sysDate'): moment(new Date()).format('YYYY-MM-DD HH:mm')
    formData.append("file", file);
    formData.append("eventId", +eventId);
    formData.append("userId", user);
    formData.append("sysDate",sysDate);
    formData.append("message", msg);
    axios
      .post(url, formData, config)
      .then((res) => {
        if (res.status == 200) {
          const obj = {
            sentBy: res?.data?.sentBy,
            message: res?.data?.message,
            eventOwner: res?.data?.eventOwner,
            imageURL: res?.data?.imageURL,
            messageDateTime: res?.data?.messageDateTime,
          };
          props.setForumData((oldArray) => [...oldArray, obj]);
        }
        setMsg("")
      })
      .catch((err) => {
        console.log("in catch", err);
        setMsg("")
        // localStorage.setItem('participant-forum-status','CANCELLED')
        setDisableInput(true)
      });
  };

  const handleFile = (e) => {
    let file = e.target.files[0];
    setFile(file);
  };
  return (
    <>
      <Paper
        component="form"
        sx={{
          p: "2px 4px",
          display: "flex",
          alignItems: "center",
          width: "100%",
        }}
      >
        <InputBase
          sx={{ ml: 1, flex: 1 }}
          placeholder="Type your message"
          inputProps={{ "aria-label": "Type your message" }}
          disabled={disableInput}
          value={msg}
          onChange={(e) => {
            setMsg(e.target.value);
          }}
        />

        <div>
          <label>
            <AttachFileIcon  sx={{cursor: 'pointer' }}/>
            <span>
              <input
                type="file"
                id="image-attach"
                name="image-attach"
                disabled={disableInput}
                onChange={(e) => handleFile(e)}
              />
            </span>
          </label>
        </div>
        <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
        <IconButton
          color="primary"
          sx={{ p: "10px", cursor: 'pointer' }}
          aria-label="directions"
          disabled={disableInput}
          onClick={postMsg}
        >
          <SendIcon />
        </IconButton>
      </Paper>
    </>
  );
};
