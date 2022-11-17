import React, { useEffect, useState } from "react";
import axios from "../common/axiosInstance";
import moment from "moment";
import { BarChart } from "../components/BarChart";
import Typography from '@mui/material/Typography';

import Divider from '@mui/material/Divider'

export const Reports = () => {
    const [systemData, setSystemData] = useState({})
    const [participationReportData, setParticipationReportData] = useState({})
    const [organizerReportData, setOrganizerReportData] = useState({})
    useEffect(() => {
        loadSystemReport()
        loadUserReport()

    }, []);
    const loadSystemReport = () => {
        let req = {
            sysDate: localStorage.getItem('sysDate') ? localStorage.getItem('sysDate') : moment(new Date()).format('YYYY-MM-DD HH:mm')
        }
        axios
            .post(`/report/system`, req)
            .then((res) => {
                if (res.status === 200) {
                    console.log(res.data);
                    setSystemData(res.data);
                }
            })
            .catch((err) => {
                console.log("in catch", err);
                //   showPopUp(err.response.data);
            })
    }
    const loadUserReport = () => {
        let req = {
            userId: localStorage.getItem('userId') || null,
            sysDate: localStorage.getItem('sysDate') ? localStorage.getItem('sysDate') : moment(new Date()).format('YYYY-MM-DD HH:mm')
        }
        axios
            .post(`/report/user`, req)
            .then((res) => {
                if (res.status === 200) {
                    console.log(res.data);
                    setParticipationReportData(res.data?.participationReport);
                    setOrganizerReportData(res.data?.organizerReport)
                }
            })
            .catch((err) => {
                console.log("in catch", err);
                //   showPopUp(err.response.data);
            })
    }



    return (
        <div>
            <Typography variant="h5" mt={2} mb={2} ml={2}>System Reports</Typography>
            <BarChart data={systemData} color={'yellow'} />
            <Divider />
            <Typography variant="h5" mt={2} mb={2} ml={2}>User Reports</Typography>
            <Typography variant="body1" mt={2} mb={2} ml={2}>Organizer</Typography>
            <BarChart data={organizerReportData} color={'orange'} />
            <Divider />
            <Typography variant="body1" mt={2} mb={2} ml={2}>Participant</Typography>
            <BarChart data={participationReportData} color={'pink'} />

        </div>


    );
};
