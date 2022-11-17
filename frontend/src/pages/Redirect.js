import React, { useEffect, useState } from "react";
import axios from "../common/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";
import NavBar from "../components/NavBar";

export const Redirect = () => {
  const navigate = useNavigate();
  const { emailId } = useParams();
  useEffect(() => {
    //   if(localStorage.getItem('authProvider') == null || localStorage.getItem('authProvider') =='GOOGLE'){
    axios
      .get(`/user/${emailId}`)
      .then(async (res) => {
        if (res.status == 200) {
          if (res && res?.data) {
            localStorage.setItem("name", res?.data?.user?.name);
            localStorage.setItem("email", res?.data?.user?.email);
            localStorage.setItem("persona", res?.data?.user?.role);
            localStorage.setItem("screenName", res?.data?.user?.screenName);
            localStorage.setItem("token", res?.data?.token);
            localStorage.setItem("sysDate", res?.data?.sysDate);
            localStorage.setItem(
              "authProvider",
              JSON.stringify(res?.data?.user?.authProvider)
            );
            localStorage.setItem("isAuth", true);
            localStorage.setItem('city',res?.data?.user?.address?.city)
            localStorage.setItem('state',res?.data?.user?.address?.state)
            localStorage.setItem('zip',res?.data?.user?.address?.zip)
            localStorage.setItem('userId',res?.data?.user?.id)
            navigate(`/home/${res?.data?.user?.email}`);
          }
        }
      })
      .catch((err) => {
        console.log("in catch", err);
        invalidUser();
      });
    //   }
  }, []);

  const invalidUser = () => {
    localStorage.clear();
    sessionStorage.clear();
    localStorage.setItem("isAuth", false);
    navigate("/");
  };

  return <span>Loading...</span>;
};
