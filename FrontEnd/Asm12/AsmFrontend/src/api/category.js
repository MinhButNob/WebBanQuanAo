import axios from "axios";

const API = "http://localhost:8080/api/categories";

export const getCategorys = () => axios.get(API);

export const getActiveCategorys = () => axios.get(`${API}/active`);

export const createCategorys = (data) => axios.post(API, data);

export const upadteCategorys = (id, data) => axios.put(`${API}/${id}`, data);

export const deleteCategorys = (id) => axios.put(`${API}/${id}`);