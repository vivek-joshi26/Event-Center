import axios from "axios";

const instance = axios.create();

instance.interceptors.request.use(
  (config) => {
    config.headers = {
			'Access-Control-Allow-Origin': '*',
			'Access-Control-Allow-Methods': '*',
			'Access-Control-Allow-Headers': '*',
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		}
    const token = localStorage.getItem("token");
    config.headers.Authorization = token ? `${token}` : "";
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);

export default instance;
