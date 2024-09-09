import { Authenticated, Refine } from "@refinedev/core";
import { RefineKbar, RefineKbarProvider } from "@refinedev/kbar";

import {
  ErrorComponent,
  notificationProvider,
  RefineSnackbarProvider,
  ThemedLayoutV2,
} from "@refinedev/mui";

import CssBaseline from "@mui/material/CssBaseline";
import GlobalStyles from "@mui/material/GlobalStyles";
import routerBindings, {
  CatchAllNavigate,
  DocumentTitleHandler,
  NavigateToResource,
  UnsavedChangesNotifier,
} from "@refinedev/react-router-v6";

import { ForgotPassword } from "pages/forgotPassword";
import { Login } from "pages/login";
import { Register } from "pages/register";
import { BrowserRouter, Outlet, Route, Routes } from "react-router-dom";
import { authProvider } from "./authProvider";
import { Header } from "./components/header";
import { ColorModeContextProvider } from "./contexts/color-mode";

import axios, { AxiosInstance, AxiosRequestConfig } from "axios";
import { dataProvider } from "dataProvider";
import { UserList } from "pages/users/list";
import { UserCreate } from "pages/users/create";

const axiosInstance:AxiosInstance = axios.create();

axiosInstance.interceptors.request.use((request: AxiosRequestConfig) => {
  let token =localStorage.getItem("TOKEN_KEY");
  if (token!==null) {
    if (request.headers) {
      request.headers["Authorization"] = `Bearer ${token}`;
    } else {
      token = "";
      request.headers = {
        Authorization: `Bearer ${token}`,
      };
    }
  }

  return request;
});


function App() {
  return (
    <BrowserRouter>
      <RefineKbarProvider>
        <ColorModeContextProvider>
          <CssBaseline />
          <GlobalStyles styles={{ html: { WebkitFontSmoothing: "auto" } }} />
          <RefineSnackbarProvider>
            <Refine
              routerProvider={routerBindings}
              dataProvider={dataProvider(axiosInstance)}
              authProvider={authProvider(axiosInstance)}
              notificationProvider={notificationProvider}
              Title={({ collapsed }) => (
                <div>
                  {collapsed ? (
                    <h1 style={{ marginTop: "1rem" }}>
                    </h1>
                  ) : (
                    <h3 style={{ fontFamily: "sans-serif" }}>
                      Log Management{" "}
                    </h3>
                  )}
                </div>
              )} 
              
              resources={[
                {
                  name: "users",
                  list: "/users",
                  create:"/users/create",
                  meta: {
                    canDelete: false,
                  },
                }
              ]}
              options={{
                syncWithLocation: true,
                warnWhenUnsavedChanges: true,
              }}
            >
              <Routes>
                <Route
                  element={
                    <Authenticated fallback={<CatchAllNavigate to="/login" />}>
                      <ThemedLayoutV2 Header={() => <Header isSticky={true} />}>
                        <Outlet />
                      </ThemedLayoutV2>
                    </Authenticated>
                  }
                >
                  <Route
                    index
                    element={<NavigateToResource resource="users" />}
                  />
                  <Route path="/users">
                    <Route index element={<UserList />} />
                  </Route>
                  <Route path="/users/create">
                    <Route index element={<UserCreate />} />
                  </Route>
                  
                  <Route path="*" element={<ErrorComponent />} />
                </Route>
                <Route
                  element={
                    <Authenticated fallback={<Outlet />}>
                      <NavigateToResource />
                    </Authenticated>
                  }
                >
                  <Route path="/login" element={<Login />} />
                  <Route path="/register" element={<Register />} />
                  <Route path="/forgot-password" element={<ForgotPassword />} />
                </Route>
              </Routes>

              <RefineKbar />
              <UnsavedChangesNotifier />
              <DocumentTitleHandler />
            </Refine>
          </RefineSnackbarProvider>
        </ColorModeContextProvider>
      </RefineKbarProvider>
    </BrowserRouter>
  );
}

export default App;
