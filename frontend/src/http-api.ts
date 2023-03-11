import axios, { AxiosResponse, AxiosError } from 'axios';
import { ImageType } from '@/image';

const instance = axios.create({
  baseURL: "/",
  timeout: 15000,
});

const responseBody = (response: AxiosResponse) => response.data;

const requests = {
  get: (url: string, param: {}) => instance.get(url, param).then(responseBody),
  post: (url: string, body: {}) => instance.post(url, body, { headers: { "Content-Type": "multipart/form-data" }, }).then(responseBody),
  put: (url: string, body: {}) => instance.put(url, body).then(responseBody),
  delete: (url: string) => instance.delete(url).then(responseBody)
};
export const api = {
  getImageList: (): Promise<ImageType[]> => {
    return new Promise((resolve, reject) => {
      instance.get('images')
        .then((response) => resolve(responseBody(response)))
        .catch((error) => {
          if (error.response && error.response.status === 404) {
            instance.get('images/search')
              .then((response) => resolve(responseBody(response)))
              .catch((error) => {
                if (error.response && error.response.status === 404) {
                  reject(new Error("Le dossier 'images' n'a pas été trouvé."));
                } else {
                  reject(error);
                }
              });
          } else {
            reject(error);
          }
        });
    });
  },
  getImage: (id: number): Promise<Blob> => requests.get(`images/${id}`, { responseType: "blob" }),
  createImage: (form: FormData): Promise<ImageType> => requests.post('images', form),
  deleteImage: (id: number): Promise<void> => requests.delete(`images/${id}`),
};
