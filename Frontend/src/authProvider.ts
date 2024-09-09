import { AuthBindings } from "@refinedev/core";

import axios, { AxiosInstance } from "axios";
import { API_URL, API_URL_AUTH } from "./constants";

export const authProvider = (axiosInstance: AxiosInstance): AuthBindings => {
  return {
    login: async ({ username, email, password }) => {
      if ((username || email) && password) {
        const formData = {
          "username": email,
          "password": password
        };
        let loginSuccess = false; // Variable to store login success state  
        try {
          const res = await axios.post(API_URL_AUTH, formData);
          localStorage.removeItem("USER_INFO");
          localStorage.removeItem("TOKEN_KEY");
          localStorage.setItem("TOKEN_KEY", res?.data?.jwt);
          localStorage.setItem("USER_INFO", JSON.stringify(res?.data));
          loginSuccess = true; // Set login success state to true
        } catch (err) {
          return {
            success: false,
            error: {
              name: "LoginError",
              message: "Invalid username or password",
            },
          };
        }
        if(loginSuccess===true){
          return {
            success: true,
            redirectTo:"/"
          };
        }
      }
    
      return {
        success: false,
        error: {
          name: "LoginError",
          message: "Missing username/email or password",
        },
      };
    },
    logout: async (props) => {
      localStorage.removeItem("USER_INFO");
      localStorage.removeItem("TOKEN_KEY");
      return {
        success: true,
        redirectTo: props?.redirectPath || "/login",
      };
    },
    onError: async (error: any) => {
      if (error?.response?.status === 401) {
        return {
          redirectTo: "/register",
          error,
        };
      }

      return {
        error,
      };
    },
    check: async () => {
      const token = localStorage.getItem("TOKEN_KEY");
      if (!token) {
        return {
          authenticated: false,
          error: new Error("No token found"),
          redirectTo: "/login",
        };
      }
      return {
        authenticated: true,
      };
    },
    getPermissions: async () => null,
    getIdentity: async () => {
      const token = localStorage.getItem("TOKEN_KEY");
      if (!token) {
        return null;
      }
      try {
        const userInfo = await axiosInstance.get(`${API_URL}/user`);
        return userInfo.data.user;
      } catch (error) {
        console.warn(error);
        return null;
      }
    },
  };
};
