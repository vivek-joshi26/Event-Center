import React from "react";
import Avatar from "@mui/material/Avatar";
import "./TextInput.css";

export const MessageLeft = (props) => {

  const message = props.message ? props.message : "";
  const timestamp = props.timestamp ? props.timestamp : "";
  const photoURL = props.photoURL ? props.photoURL : "dummy.js";
  const displayName = props.displayName ? props.displayName : "User";
  const isEventOwner = props.isEventOwner ? props.isEventOwner : false;
  const imageURL = props.imageURL ? props.imageURL : null;
  const color = "#" + Math.floor(Math.random()*16777215).toString(16).padStart(6, '0').toUpperCase();

  return (
    <>
      <div className="msg-parent-div">
        <Avatar className="avatar" alt={displayName} src={photoURL} sx={{backgroundColor:color}}></Avatar>

        <div className="msg-div">
          <div className="display-msg">
            <b>{displayName}</b>
            {isEventOwner && <div style={{fontSize:'13px',fontWeight:'800'}}>{"Organizer"}</div>}
          </div>
          <div className="display-msg">
            <p styles={{ padding: 0, margin: 0 }}>
              <p>{message}</p>
              {
                imageURL && (
                  <img className="image-forum" src={imageURL} alt=""/>
                )
              }
              </p>
          </div>
          <div className="display-msg"
            styles={{
              position: "absolute",
              fontSize: ".85em",
              fontWeight: "300",
              marginTop: "10px",
              bottom: "-3px",
              right: "5px",
            }}
          >
            {timestamp}
          </div>
        </div>
      </div>
      <hr />
    </>
  );
};
