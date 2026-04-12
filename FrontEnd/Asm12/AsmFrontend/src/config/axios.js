import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api", // URL backend của bạn
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor để thêm token nếu có
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

export default axiosInstance;
