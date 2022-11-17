import React, { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import Chart from 'chart.js/auto';
export const BarChart = (props) => {
    const [chartData, setChartData] = useState({})
    const [color, setColor] = useState('blue')
    useEffect(() => {
        console.log(props)
        setChartData(props?.data)
        setColor(props?.color)
    }, [props]);

    return (
        <div style={{margin:'20px'}}>
            <Bar id="report"
                data={{
                    labels: Object.entries(chartData).map((key, value) => key[0].charAt(0).toUpperCase() + key[0].slice(1)),
                    datasets: [
                        {
                            label: 'Count',
                            data: Object.entries(chartData).map((key, value) => key[1]),
                            backgroundColor: color,
                            borderColor: 'black',
                            borderWidth: 1,
                        },

                    ],
                }}
                height={400}
                width={600}
                options={{
                    maintainAspectRatio: false,
                    scales: {

                        y: {
                            beginAtZero: true,
                        },

                    },
                    legend: {
                        labels: {
                            fontSize: 25,
                        },
                    },
                }}
            />


        </div>


    );
};
