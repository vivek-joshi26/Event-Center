import React, { useEffect, useState } from "react";
import axios from "../common/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import EventOutlinedIcon from "@mui/icons-material/EventOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import AlertModal from "../common/Alert";
import InputLabel from "@mui/material/InputLabel";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DateTimePicker } from "@mui/x-date-pickers/DateTimePicker";
import Select from "@mui/material/Select";
import OutlinedInput from "@mui/material/OutlinedInput";
import InputAdornment from "@mui/material/InputAdornment";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import FormHelperText from "@mui/material/FormHelperText";
import moment from 'moment'
const theme = createTheme();
export const CreateEvent = () => {
  const [title, setTitle] = useState("");
  const [startDateTime, setStartDateTime] = useState(new Date());
  const [endDateTime, setEndDateTime] = useState(new Date());
  const [deadline, setDeadline] = useState(new Date());
  const [admPolicy, setAdmPolicy] = useState(null);
  const [description, setDescription] = useState("");
  const [street, setStreet] = useState(null);
  const [city, setCity] = useState(null);
  const [state, setState] = useState(null);
  const [zip, setZip] = useState(null);
  const [minParticipants, setMinParticipants] = useState(0);
  const [maxParticipants, setMaxParticipants] = useState(0);
  const [fee, setFee] = useState(null);
  const isOrg = localStorage.getItem("persona") == "ORG" ? true : false;
  const user = localStorage.getItem("userId") || null;
  const [isPopUp, setPopUp] = useState(false);
  const [errMsg, setErrMsg] = useState("");
  const navigate = useNavigate();

  const setPopUpVal = () => {
    setPopUp(!isPopUp);
  };
  const showPopUp = (msg) => {
    setPopUpVal();
    setErrMsg(msg);
  };

  const validateFormValues=()=>{
    if (deadline >= startDateTime) {
      showPopUp("Deadline shouldn't be later than Start Time");
      return false;
    }
    if (startDateTime >= endDateTime) {
      showPopUp("Start Time shouldn't be later than End Time");
      return false;
    }
    if(isNaN(parseInt(minParticipants))) {
      showPopUp("Minimum Participants should be a Number");
      return false;
    }
    
    if(isNaN(parseInt(maxParticipants))) {
      showPopUp("Maximum Participants should be a Number");
      return false;
    }
    if(fee && isOrg && isNaN(parseInt(fee))) {
      showPopUp("Fees should be a Number");
      return false;
    }
    if (parseInt(minParticipants) > parseInt(maxParticipants)) {
      showPopUp("Minimum Particpants should be less than Maximum Participants");
      return false;
    }
    if(zip && isNaN(parseInt(zip))) {
      showPopUp("Zip should be a Number");
      return false;
    }
    if(!admPolicy) {
      showPopUp("Select Admission Policy");
      return false;
    }
    if(!title || title.length == 0) {
      showPopUp("Set Event Title");
      return false;
    }
    return true;
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    if(!validateFormValues()) return;

    const obj ={
      "title" : title,
      "description" : description,
      "minParticipants" : +minParticipants,
      "maxParticipants"   : +maxParticipants,
      "address": {
        "street": street,
        "city": city,
        "state": state,
        "zip": zip
      },
      "admissionPolicy": admPolicy,
      "startDateTime": moment(startDateTime).format('YYYY-MM-DD HH:mm') ,
      "endDateTime": moment(endDateTime).format('YYYY-MM-DD HH:mm'),
      "deadline": moment(deadline).format('YYYY-MM-DD HH:mm'),
      "userId": parseInt(user),  
      "sysDate":localStorage.getItem('sysDate')? localStorage.getItem('sysDate'): moment(new Date()).format('YYYY-MM-DD HH:mm')
    }
    if(isOrg) obj.fee = +fee

    axios
      .post("/createEvent", obj)
      .then((res) => {
        if (res.status >= 200) {
          navigate(-1)
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        showPopUp(`${err?.response?.data}`);
      });
    
  };
  return (
    <ThemeProvider theme={theme}>
      <Container component="main">
        <Box
          sx={{
            marginTop: 2,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <EventOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Create Event
          </Typography>
        </Box>
        <Box
          component="form"
          onSubmit={handleSubmit}
          noValidate
          sx={{
            mt: 3,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          {isPopUp && (
            <AlertModal open={isPopUp} msg={errMsg} modal={setPopUpVal} />
          )}
          <Grid container spacing={2}>
            <Grid item xs={12} md={4} lg={3}>
              <TextField
                required
                fullWidth
                id="titleEvent"
                label="Title"
                name="titleEvent"
                onChange={(e) => {
                  setTitle(e.target.value);
                }}
              />
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <TextField
                required
                fullWidth
                id="description"
                label="Description"
                name="description"
                onChange={(e) => {
                  setDescription(e.target.value);
                }}
              />
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <TextField
                required
                fullWidth
                id="minParticipants"
                label="Minimum Participants"
                name="minParticipants"
                value={minParticipants}
                onChange={(e) => {
                  setMinParticipants(e.target.value);
                }}
              />
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <TextField
                required
                fullWidth
                id="maxParticipants"
                label="Maximum Participants"
                name="maxParticipants"
                value={maxParticipants}
                onChange={(e) => {
                  setMaxParticipants(e.target.value);
                }}
              />
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <FormControl variant="standard">
                <InputLabel id="admission-label">Admission Policy</InputLabel>
                <Select
                  labelId="admission-label"
                  id="admission"
                  value={admPolicy}
                  onChange={(e) => {
                    setAdmPolicy(e.target.value);
                  }}
                  sx={{ minWidth: 270 }}
                  label="Admission Policy"
                >
                  <MenuItem value={"first-come-first-served"}>
                    First Come First Served
                  </MenuItem>
                  <MenuItem value={"approval-required"}>Approval Required</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <LocalizationProvider dateAdapter={AdapterDateFns}>
                <DateTimePicker
                  renderInput={(props) => <TextField {...props} />}
                  label="Start Time"
                  value={startDateTime}
                  onChange={(newValue) => {
                    setStartDateTime(newValue);
                  }}
                  minDateTime={new Date()}
                />
              </LocalizationProvider>
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <LocalizationProvider dateAdapter={AdapterDateFns}>
                <DateTimePicker
                  renderInput={(props) => <TextField {...props} />}
                  label="End Time"
                  value={endDateTime}
                  onChange={(newValue) => {
                    setEndDateTime(newValue);
                  }}
                  minDateTime={startDateTime}
                />
              </LocalizationProvider>
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <LocalizationProvider dateAdapter={AdapterDateFns}>
                <DateTimePicker
                  renderInput={(props) => <TextField {...props} />}
                  label="Deadline"
                  value={deadline}
                  onChange={(newValue) => {
                    setDeadline(newValue);
                  }}
                  maxDateTime={startDateTime}
                />
              </LocalizationProvider>
            </Grid>
            {isOrg && (
              <Grid item xs={12} md={4} lg={3}>
                <FormControl fullWidth sx={{ m: 1 }}>
                  <InputLabel htmlFor="fee">Fee</InputLabel>
                  <OutlinedInput
                    id="fee"
                    onChange={(e) => {
                      setFee(e.target.value);
                    }}
                    startAdornment={
                      <InputAdornment position="start">$</InputAdornment>
                    }
                    label="Fee"
                  />
                  <FormHelperText>give 0 for free event</FormHelperText>
                </FormControl>
              </Grid>
            )}

            <Grid item xs={12}>
              <InputLabel id="address">Address </InputLabel>
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <TextField
                fullWidth
                name="street"
                label="Street"
                type="street"
                id="street"
                onChange={(e) => {
                  setStreet(e.target.value);
                }}
              />
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <TextField
                fullWidth
                required
                name="city"
                label="City"
                type="city"
                id="city"
                onChange={(e) => {
                  setCity(e.target.value);
                }}
              />
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <TextField
                fullWidth
                required
                name="state"
                label="State"
                type="state"
                id="state"
                onChange={(e) => {
                  setState(e.target.value);
                }}
              />
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
              <TextField
                fullWidth
                required
                name="zip"
                label="Zip Code"
                type="zip"
                id="zip"
                onChange={(e) => {
                  setZip(e.target.value);
                }}
              />
            </Grid>
          </Grid>

          <Button
            type="submit"
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
            // onClick={onDefaultSignIn}
          >
           Submit
          </Button>
        </Box>
      </Container>
    </ThemeProvider>
  );
};
