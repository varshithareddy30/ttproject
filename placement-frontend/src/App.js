import { BrowserRouter, Routes, Route } from "react-router-dom"

import Register from "./components/Register"
import Login from "./components/Login"
import Profile from "./components/Profile"
import Domain from "./components/Domain"
import Goal from "./components/Goal"
import Dashboard from "./components/Dashboard"
import Test from "./components/Test"
import AdminDashboard from "./components/AdminDashboard"

function App() {

return (

<BrowserRouter>

<Routes>

<Route path="/" element={<Register/>} />

<Route path="/login" element={<Login/>} />

<Route path="/profile" element={<Profile/>} />

<Route path="/domain" element={<Domain/>} />

<Route path="/goal" element={<Goal/>} />

<Route path="/dashboard" element={<Dashboard/>} />
<Route path="/test/:goalId" element={<Test/>}/>
<Route path="/admin" element={<AdminDashboard />} />

</Routes>

</BrowserRouter>

)

}

export default App