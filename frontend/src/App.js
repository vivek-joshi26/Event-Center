import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { SignIn } from "./pages/SignIn";
import { SignUp } from "./pages/SignUp";
import { Dashboard } from "./pages/Dashboard";
import { Redirect } from "./pages/Redirect";
import { CreateEvent } from "./pages/createEvent";
import { NotFound } from "./pages/NotFound";
import ProtectedRoutes from "./common/ProtectedRoutes";


function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" exact element={<SignIn />} />
          <Route path="register/:emailId" element={<SignUp />} />
          <Route path="redirect/:emailId" element={<Redirect />} />
          <Route path="*" element={<NotFound/>} />
          <Route element={<ProtectedRoutes />}>
          <Route path="home/*" element={<Dashboard />}>
          </Route>
          </Route>
        </Routes>
      </Router>
    </>
  );
}

export default App;
