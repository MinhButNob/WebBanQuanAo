import axios from "axios";

const API = "http://localhost:8080/api/categories";

export const getCategories = () => axios.get(API);

export const getActiveCategories = () => axios.get(`${API}/active`);

export const createCategory = (data) => axios.post(API, data);

export const updateCategory = (id, data) => axios.put(`${API}/${id}`, data);

export const deleteCategory = (id) => axios.delete(`${API}/${id}`);
