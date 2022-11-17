import React, { useEffect, useState } from "react";
import axios from "../common/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";
import Paper from '@mui/material/Paper';
import moment from "moment";

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Rating from '@mui/material/Rating';
import Typography from '@mui/material/Typography';
import { Button } from "@mui/material";
export const Review = (props) => {
    const CHARACTER_LIMIT = 120;
    const [reviewStar, setReviewStar] = React.useState(props?.reviewStar || null);
    const [eventId, setEventId] = useState(props?.eventId || null)
    const [reviewGivenBy, setReviewGivenBy] = useState(props?.reviewGivenBy || null)
    const [reviewGivenTo, setReviewGivenTo] = useState(props?.reviewGivenTo || null)
    const [reviewText, setReviewText] = useState(props?.reviewText || "")
    const [forOrg, setForOrg] = useState(props?.forOrg || false)
    const [isEditable, setIsEditable] = useState(false)
    useEffect(() => {
        console.log(props)
        if (!props?.reviewText || props?.reviewText.length === 0 || !props?.reviewStar) setIsEditable(true)
    }, [props]);
    const style = {
        display: 'flex',
        flexDirection: 'column',
        // margin: '2rem',
        width: '400px',
        pointerEvents: isEditable ? 'all' : 'none',
    }

    const handleSubmit = (event) => {
        event.preventDefault()
        let api = null;
        if (forOrg) api = 'reviewForOrganizer';
        else api = 'reviewForParticipants';
        const req = {
            "eventId": +eventId,
            "reviewGivenBy": +reviewGivenBy,
            "reviewGivenTo": reviewGivenTo,
            "reviewText": reviewText,
            "reviewStar":reviewStar,
            "sysDate": localStorage.getItem('sysDate') ? localStorage.getItem('sysDate') : moment(new Date()).format('YYYY-MM-DD HH:mm')
        }
        axios
            .post(`/${api}`, req)
            .then((res) => {
                console.log(res.data)
                if (res.status == 200){
                   setIsEditable(false)
                } 
            })
            .catch((err) => {
                console.log("in catch", err);
                // showPopUp(err.response.data);
            })
    }


    return (
        <div style={{ ...style }}>
            <Box
                sx={{
                    '& > legend': { mt: 2 },
                }}
            >
                <Paper elevation={16} sx={{ display: 'flex',
                        flexDirection: 'column',}}>
                    <div style={{
                        display: 'inline-flex',
                        flexDirection: 'column',
                        margin: '0.2rem',
                        padding: '2px'
                    }}>
                        <Typography variant="caption" display="block" gutterBottom>Rating</Typography>
                        <Rating
                            name="simple-controlled"
                            value={reviewStar}
                            precision={0.1}
                            onChange={(event, newValue) => {
                                setReviewStar(newValue);
                            }}
                            sx={{
                                'span': { ml: 0 }
                            }}
                        />
                        <TextField
                            label="Review"
                            sx={{
                                maxWidth: '120ch',
                            }}
                            inputProps={{
                                maxlength: CHARACTER_LIMIT
                            }}
                            multiline
                            value={reviewText}
                            helperText={`${reviewText.length}/${CHARACTER_LIMIT}`}
                            onChange={e => { setReviewText(e.target.value) }}
                            margin="normal"
                            variant="standard"
                        />
                    </div>
                    {
                        (isEditable) && (
                            <Button color="secondary" size="small" onClick={e => handleSubmit(e)}
                            disabled={!reviewStar  || !reviewText || reviewText.length ===0 }>Submit</Button>
                        )
                    }
                </Paper>
            </Box>
        </div>
    );
};