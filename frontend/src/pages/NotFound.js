import React from "react";
import { Link } from 'react-router-dom';
// import PageNotFound from "../assets/images/404"
export const NotFound = () => {
  return (
    <div style={{display:'flex', flexDirection:'column-reverse',justifyContent:'center'}}>
    {/* <img src="../"  /> */}
    <p style={{textAlign:"center"}}>
      <Link to="/home">Go to home </Link>
    </p>
  </div>
  )
};
