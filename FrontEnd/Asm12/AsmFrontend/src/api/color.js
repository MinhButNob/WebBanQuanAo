import axios from "axios";

const API = "http://localhost:8080/api/colors";

export const getColors = () => axios.get(API);

export const getActiveColors = () => axios.get(`${API}/active`);

export const createColors = (data) => axios.post(API, data);

export const updateColors = (id, data) => axios.put(`${API}/${id}`, data);

export const deleteColors = (id) => axios.delete(`${API}/${id}`);
