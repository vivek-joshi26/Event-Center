import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import AlertModal from "../common/Alert";
import axios from "axios";
const theme = createTheme();

export const SignUp = () => {
  const [account, setAccount] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");
  const [gender, setGender] = useState("female");
  const [screenName, setScreenName] = useState("");
  const [description, setDescription] = useState("");
  const [street, setStreet] = useState("");
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [zip, setZip] = useState("");
  const [isGSignUp, setGSignUp] = useState(false);
  const [isPopUp, setPopUp] = useState(false);
  const [errMsg, setErrMsg] = useState("");
  const navigate = useNavigate();
  const [disabled, setDisabled] = useState(false);

  const { emailId } = useParams();
  useEffect(() => {
    if (emailId !== "default" && emailId.includes("@")) {
      console.log(emailId);
      setEmail(emailId);
      setDisabled(true);
      setGSignUp(true);
    }
  }, [emailId]);


  const isScreenNameValid = () => {
    if (screenName.includes(fullName)) return true;
    else {
      showPopUp("Screen Name should match Full Name");
      return false;
    }
  };

  const isRequiredPresent = () => {
    if (fullName !== "" && email !== "") return true;
    else {
      showPopUp("full Name and email are required");
      return false;
    }
  };

  const onGoogleSignUp = (event) => {
    event.preventDefault();
    setGSignUp(true);
    localStorage.setItem('isAuth',false)
    window.open("http://ec2-52-27-45-62.us-west-2.compute.amazonaws.com:8080/oauth2/authorization/google", "_self");
  };

  const onDefaultSignUp = (event) => {
    localStorage.setItem('isAuth',false)
    event.preventDefault();
    // if (!isScreenNameValid()) return;
    if (!isRequiredPresent()) return;
    axios
      .post("/register", {
        email: email,
        name: fullName,
        role: account,
        screenName: screenName,
        gender: gender,
        description: description,
        password: password,
        address: {
          street: street,
          city: city,
          state: state,
          zip: zip,
        },
      })
      .then((res) => {
        localStorage.setItem("persona", JSON.stringify(account));
        localStorage.setItem("email", JSON.stringify(email));
        if (res.status == 201) {
          localStorage.setItem('authProvider','LOCAL')
          navigate(`/`);
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        //showPopUp("Error signin up");
        showPopUp(`${err?.response?.data}`);
      });
  };

  const onGSignUpRegister = (event) => {
    event.preventDefault();
    // if (!isScreenNameValid()) return;
    if (!isRequiredPresent()) return;
    axios
      .post("/registerGoogle", {
        email: email,
        name: fullName,
        role: account,
        screenName: screenName,
        gender: gender,
        description: description,
        address: {
          street: street,
          city: city,
          state: state,
          zip: zip,
        },
      })
      .then((res) => {
        console.log("success", res);
        localStorage.setItem("persona", JSON.stringify(account));
        localStorage.setItem("email", JSON.stringify(email));
        if (res.status == 201) {
          localStorage.setItem('authProvider','GOOGLE');
          navigate(`/`);
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        showPopUp(`${err?.response?.data}`);
      });
  };

  const showPopUp = (msg) => {
    setPopUpVal();
    setErrMsg(msg);
  };

  const setPopUpVal=()=>{
    setPopUp(!isPopUp);
  }

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <Box
            component="form"
            noValidate
            // onSubmit={handleSubmit}
            sx={{ mt: 3 }}
          >
            {isPopUp && <AlertModal open={isPopUp} msg={errMsg} modal={setPopUpVal}/>}
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  disabled={disabled}
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value);
                  }}
                />
              </Grid>
              {!isGSignUp && (
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    name="password"
                    label="Password"
                    type="password"
                    id="password"
                    onChange={(e) => {
                      setPassword(e.target.value);
                    }}
                  />
                </Grid>
              )}

              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="fullName"
                  label="Full Name"
                  type="fullName"
                  id="fullName"
                  onChange={(e) => {
                    setFullName(e.target.value);
                  }}
                />
              </Grid>
              
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="screenName"
                  label="Screen Name"
                  type="screenName"
                  id="screenName"
                  onChange={(e) => {
                    setScreenName(e.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <FormControl variant="standard" sx={{ minWidth: 400 }}>
                  <InputLabel id="demo-simple-select-standard-label">
                    Account Type
                  </InputLabel>
                  <Select
                    labelId="demo-simple-select-standard-label"
                    id="demo-simple-select-standard"
                    value={account}
                    onChange={(e) => {
                      setAccount(e.target.value);
                    }}
                    sx={{ minWidth: 400 }}
                    label="Account Type"
                  >
                    <MenuItem value={"PEOPLE"}>Person</MenuItem>
                    <MenuItem value={"ORG"}>Organization</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  name="description"
                  label="Description"
                  type="description"
                  id="description"
                  onChange={(e) => {
                    setDescription(e.target.value);
                  }}
                />
              </Grid>
              {account=="PEOPLE" &&(
                <Grid item xs={12}>
                <FormControl variant="standard" sx={{ minWidth: 400 }}>
                  <InputLabel id="gender-label">Gender</InputLabel>
                  <Select
                    labelId="gender-label"
                    id="gender"
                    value={gender}
                    onChange={(e) => {
                      setGender(e.target.value);
                    }}
                    sx={{ minWidth: 400 }}
                    label="Gender"
                  >
                    <MenuItem value={"female"}>Female</MenuItem>
                    <MenuItem value={"male"}>Male</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              )}
              <Grid item xs={12}>
                <InputLabel id="address">Address </InputLabel>
              </Grid>
              <Grid item xs={12}>
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
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="city"
                  label="City"
                  type="city"
                  id="city"
                  onChange={(e) => {
                    setCity(e.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="state"
                  label="State"
                  type="state"
                  id="state"
                  onChange={(e) => {
                    setState(e.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
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
            {!disabled && (
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={onDefaultSignUp}
              >
                Sign Up
              </Button>
            )}
            {disabled && (
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={onGSignUpRegister}
              >
                Register
              </Button>
            )}
          </Box>
          {!disabled && (
            <>
              <hr />
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={onGoogleSignUp}
              >
                Sign Up with Google
              </Button>
            </>
          )}

          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link href="/" variant="body2">
                Already have an account? Sign in
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Container>
    </ThemeProvider>
  );
};
