import axios from "axios";

const API = "http://localhost:8080/api/products";

export const getProducts = () => axios.get(API);

export const getActiveProducts = () => axios.get(`${API}/active`);

export const createProduct = (data) => axios.post(API, data);

export const updateProductAPI = (id, data) => axios.put(`${API}/${id}`, data);

export const deleteProductAPI = (id) => axios.delete(`${API}/${id}`);
