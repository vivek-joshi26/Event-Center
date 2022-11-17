import React, { useEffect, useState } from "react";
import axios from "../common/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";
import NavBar from "../components/NavBar";
import { CreateEvent } from "./createEvent";
import { SearchEvent } from "./SearchEvent";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { EventDetail } from "../pages/EventDetail";
import { MyEvents } from "./MyEvents";
import { UserDetails } from "./UserDetails";
import {Reports} from "./reports"
export const Dashboard = () => {
  useEffect(() => { }, []);

  return (
    <div>
      <NavBar />
      <div
        sx={{ position: 'absolute', "z-index": 1 }}>
        <Routes>
        <Route path="/:emailId" exact element={<UserDetails />} />
          <Route path="/search-event" exact element={<SearchEvent />} />
          <Route path="/create-event"  exact element={<CreateEvent />} />
          <Route path="/search-event/detail"  exact element={<EventDetail />} />
          <Route path="/my-events" exact element={<MyEvents />} />
          <Route path="/reports" exact element={<Reports />} />
        </Routes>
      </div>
    </div>
  );
};
