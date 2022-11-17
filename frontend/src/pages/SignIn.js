import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import axios from "axios";
import AlertModal from "../common/Alert";

const theme = createTheme();

export const SignIn = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isGSignIn, setGSignIn] = useState(false);
  const [isPopUp, setPopUp] = useState(false);
  const [errMsg, setErrMsg] = useState("");
  const navigate = useNavigate();

  useEffect(()=>{
    console.log(localStorage.getItem('isAuth'))
    if(localStorage.getItem('isAuth')===false){
    
      setPopUp(true);
      showPopUp('Pls verify your account or give right credentials!');
    }
    // console.log(localStorage.getItem('isAuth'))
    //   showPopUp('Pls verify your email address first')
  },[])

  const onGoogleSignIn = (event) => {
    event.preventDefault();
    // setGSignIn(true);
    window.open("http://ec2-52-27-45-62.us-west-2.compute.amazonaws.com:8080/oauth2/authorization/google", "_self");
  };

  const onDefaultSignIn = (event) => {
    event.preventDefault();
    setGSignIn(false);
    axios
      .post("/login", {
        email: email,
        password: password,
      })
      .then(async (res) => {
        console.log("success", res);
        localStorage.setItem("email", JSON.stringify(email));
        if (res.status == 200) {
          if (res) {
            localStorage.setItem("name", res?.data?.user?.name);
            localStorage.setItem("email", res?.data?.user?.email);
            localStorage.setItem("screenName", res?.data?.user?.screenName);
            localStorage.setItem("isAuth", true);
            localStorage.setItem('city',res?.data?.user?.address?.city)
            localStorage.setItem('state',res?.data?.user?.address?.state)
            localStorage.setItem('zip',res?.data?.user?.address?.zip)
            localStorage.setItem('userId',res?.data?.user?.id)
            localStorage.setItem("persona", res?.data?.user?.role);
            localStorage.setItem("token", res?.data?.token);
            localStorage.setItem('authProvider',JSON.stringify(res?.data?.user?.authProvider))
            localStorage.setItem('isAuth',true)
            localStorage.setItem('userId',res?.data?.user?.id)
            localStorage.setItem("sysDate", res?.data?.sysDate);
          }
          navigate(`/home/${email}`);
          
          // const userData = await axios.get(`/user/${email}`);
          // console.log(us)
        }
      })
      .catch((err) => {
        showPopUp(`${err?.response?.data}`);
        console.log("in catch", err?.response?.data);
        localStorage.setItem('isAuth',false)
        
      });
  };
  const showPopUp = (msg) => {
    setPopUpVal();
    setErrMsg(msg);
  };

  const setPopUpVal=()=>{
    setPopUp(true);
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
            Sign in
          </Typography>
          <Box
            component="form"
            // onSubmit={handleSubmit}
            noValidate
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
                  onChange={(e) => {
                    setEmail(e.target.value);
                  }}
                />
              </Grid>

              <Grid item xs={12}>
                {!isGSignIn && (
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
                )}
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              onClick={onDefaultSignIn}
            >
              Sign In
            </Button>
            <hr />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              onClick={onGoogleSignIn}
            >
              Sign In with Google
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link href={`/register/default`} variant="body2">
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
};
