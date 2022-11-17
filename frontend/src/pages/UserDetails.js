import React, { useEffect, useState } from "react";
import axios from "../common/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";
import NavBar from "../components/NavBar";
import { CreateEvent } from "./createEvent";
import { SearchEvent } from "./SearchEvent";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { EventDetail } from "../pages/EventDetail";
import { Comments } from "../components/Comments";
import { MyEvents } from "./MyEvents";
import Typography from '@mui/material/Typography';
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
export const UserDetails = () => {
    const { emailId } = useParams();
    const [name, setName] = useState(localStorage.getItem('name') || "")
    const [email, setEmaile] = useState(localStorage.getItem('email') || "")
    const [screenName, setScreenName] = useState(localStorage.getItem('screenName') || "")
    const [city, setCity] = useState(localStorage.getItem('city') || "")
    const [zip, setZip] = useState(localStorage.getItem('zip') || "")
    const [state, setState] = useState(localStorage.getItem('state') || "")

    useEffect(() => {
    }, []);

    return (
        <div style={{display: 'flex', justifyContent: 'space-evenly'}}>
            <Card sx={{ width: "60%", borderRadius: "2%", mt:4 }} variant="outlined">
                <CardContent>
                    {/* <Typography
                        sx={{
                            mb: 1.5,
                            display: "flex",
                            flexDirection: "row ",
                            justifyContent: "space-between",
                        }}
                        color="text.primary"
                        gutterBottom
                    > */}
                        {name && (
                            <Typography sx={{ fontSize: 14,display: 'flex',
                            justifyContent: 'flex-start', alignItems: 'center'}} color="text.primary">
                                <AccountCircleIcon/>
                                <div style={{fontSize: 'large', fontWeight: 'bold', marginLeft:'1rem'}}>{name}</div>
                            </Typography>
                    // </Typography>
                     )}

                    {city && (
                        <Typography sx={{ fontSize: 14,display: 'flex',
                        justifyContent: 'flex-start', alignItems: 'center',marginLeft:'2.5rem'}} color="text.primary">
                            <h4>City</h4>
                            <div style={{marginLeft:'1rem'}}> {city}</div>
                        </Typography>
                    )}
                    {state && (
                        <Typography variant="body2" sx={{ fontSize: 14,display: 'flex',
                            justifyContent: 'flex-start', alignItems: 'center',marginLeft:'2.5rem'}}>
                             <h4>State</h4>
                            <div style={{marginLeft:'1rem'}}> {state}</div>
                        </Typography>
                    )}
                    {zip && (
                        <Typography variant="body2" sx={{ fontSize: 14,display: 'flex',
                        justifyContent: 'flex-start', alignItems: 'center',marginLeft:'2.5rem'}}>
                              <h4>Zip</h4>
                            <div style={{marginLeft:'1rem'}}> {zip}</div>
                        </Typography>
                    )}
                    {screenName && (
                        <Typography variant="body2" sx={{ fontSize: 14,display: 'flex',
                        justifyContent: 'flex-start', alignItems: 'center',marginLeft:'2.5rem'}}>
                              <h4>Screen Name</h4>
                            <div style={{marginLeft:'1rem'}}> {screenName}</div>
                        </Typography>
                    )}
                    {email && (
                        <Typography variant="body2" sx={{ fontSize: 14,display: 'flex',
                        justifyContent: 'flex-start', alignItems: 'center',marginLeft:'2.5rem'}}>
                              <h4>Email</h4>
                            <div style={{marginLeft:'1rem', color:'blue'}}> {email}</div>
                        </Typography>
                    )}
                </CardContent>


            </Card>
        </div>
    );
};
