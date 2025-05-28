import axios from 'axios';
import type { AxiosRequestConfig } from 'axios';

export const customInstance = async <T>(config: AxiosRequestConfig): Promise<T> => {
  const instance = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
      'Content-Type': 'application/json',
    },
  });

  const { data } = await instance(config);
  return data;
}; 