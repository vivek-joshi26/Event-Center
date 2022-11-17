import React, { useState, useEffect } from "react";
import * as FaIcons from "react-icons/fa";
import * as AiIcons from "react-icons/ai";
import { Link } from "react-router-dom";
import "./NavBar.css";
import { IconContext } from "react-icons";
import { useNavigate } from "react-router-dom";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DateTimePicker } from "@mui/x-date-pickers/DateTimePicker";
import TextField from "@mui/material/TextField";
import moment from "moment";
import axios from "axios";
import Button from "@mui/material/Button";


function NavBar() {
  const navigate = useNavigate();
  const [sysDate, setSysDate] = useState(localStorage.getItem('sysDate') || new Date())
  const email = localStorage.getItem('email') || null;


  const handleDate = (e) => {
    e.preventDefault()
    console.log(sysDate)
    let obj = {
      sysDate: moment(sysDate).format('YYYY-MM-DD HH:mm')
    }
    axios
      .post(`/update-time`, obj)
      .then((res) => {
        console.log(res)
        if (res.status == 200) {
          localStorage.setItem('sysDate', moment(obj.sysDate).format('YYYY-MM-DD HH:mm'));
          window.location.reload();
        }
      })
      .catch((err) => {
        console.log("in catch", err);
      })
  }

  const handleReset = (e) => {
    e.preventDefault()
    axios
      .put(`/update-time`)
      .then((res) => {
        console.log(res)
        if (res.status == 200) {
          localStorage.setItem('sysDate', moment(new Date()).format('YYYY-MM-DD HH:mm'));
          setSysDate(new Date())
          window.location.reload();
        }
      })
      .catch((err) => {
        console.log("in catch", err);
      })
  }
  const SidebarData = [
    {
      title: "Home",
      path: `/home/${email}`,
      icon: <AiIcons.AiFillHome />,
      cName: "nav-text",
    },
    {
      title: "My Events & Reviews",
      path: "/home/my-events",
      icon: <AiIcons.AiFillCalendar />,
      cName: "nav-text",
    },
    {
      title: "Search Events",
      path: "/home/search-event",
      icon: <AiIcons.AiFillCalendar />,
      cName: "nav-text",
    },
    {
      title: "Create Events",
      path: "/home/create-event",
      icon: <AiIcons.AiFillCalendar />,
      cName: "nav-text",
    },
    {
      title: "Reports",
      path: "/home/reports",
      icon: <AiIcons.AiFillCalendar />,
      cName: "nav-text",
    },
  ];
  const [sidebar, setSidebar] = useState(false);

  const showSidebar = () => setSidebar(!sidebar);

  const logout = () => {
    localStorage.clear();
    sessionStorage.clear();
    navigate("/");
  };

  return (
    <>
      <IconContext.Provider value={{ color: "#fff" }}>
        <div className="navbar">
          <Link to="#" className="menu-bars">
            <FaIcons.FaBars onClick={showSidebar} />
          </Link>
          <div style={{
            display: 'inline-flex',
            marginLeft: '29rem',
          }}>
            <div style={{
              background: 'white'
            }}>
              <LocalizationProvider dateAdapter={AdapterDateFns}>
                <DateTimePicker
                  renderInput={(props) => <TextField {...props} />}
                  label="System Time"
                  value={sysDate}
                  onChange={e => {
                    setSysDate(e)
                  }}
                  minDateTime={new Date()}
                  maxDateTime={new Date().setFullYear(new Date().getFullYear()+1)}
                />
              </LocalizationProvider>
            </div>

            <Button  size="small" sx={{color:"white"}} onClick={(e) => { handleDate(e) }}> Update</Button>
            <Button size="small" sx={{color:"white"}}  onClick={(e) => { handleReset(e) }}> Reset</Button>
          </div>
          <span
            className="menu-bars"
            style={{ cursor: "pointer" }}
            onClick={logout}
          >
            <AiIcons.AiOutlineLogout />
          </span>

        </div>
        <nav className={sidebar ? "nav-menu active" : "nav-menu"}>
          <ul className="nav-menu-items" onClick={showSidebar}>
            <li className="navbar-toggle">
              <Link to="#" className="menu-bars">
                <AiIcons.AiOutlineClose />
              </Link>
            </li>
            {SidebarData.map((item, index) => {
              return (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    {item.icon}
                    <span>{item.title}</span>
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>
      </IconContext.Provider>
    </>
  );
}

export default NavBar;
