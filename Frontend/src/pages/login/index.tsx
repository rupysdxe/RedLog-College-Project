import { AuthPage } from "@refinedev/mui";

export const Login = () => {
  return (
    <AuthPage
    title={<h2 style={{color:"blue"}}></h2>}
      formProps={{
        defaultValues: { email: "", password: "" },
      }}
    />
  );
};
