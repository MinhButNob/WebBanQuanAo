import axios from "axios";

const API = "http://localhost:8080/api/customers";

export const getCustomers = () => axios.get(API);
