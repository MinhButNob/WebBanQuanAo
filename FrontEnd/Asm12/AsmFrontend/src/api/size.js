import axios from "axios";

const API = "http://localhost:8080/api/sizes";

export const getSizes = () => axios.get(API);

export const getActiveSizes = () => axios.get(`${API}/active`);

export const createSizes = (data) => axios.post(API, data);

export const updateSizes = (id, data) => axios.put(`${API}/${id}`, data);

export const deleteSizes = (id) => axios.delete(`${API}/${id}`);
