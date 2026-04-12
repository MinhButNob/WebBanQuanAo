// src/api/auth.js
import axios from "axios";

const API = "http://localhost:8080/api/auth";

export const loginAPI = (data) => axios.post(`${API}/login`, data);
export const registerAPI = (data) => axios.post(`${API}/register`, data);
